package ru.clevertec.knyazev.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONStringParser implements Parser{
	
	@Override
	public Map<String, String> parseSimpleFields(String simpleJSON) {
		Map<String, String> simpleFields = new HashMap<>();
		
		Matcher simpleFieldMatcher = Pattern.compile("(\"\\w+?\"\\s*?:\\s*?[\\d\\w\"]+)").matcher(simpleJSON);		
		while (simpleFieldMatcher.find()) {
			String simpleFieldsGroup = simpleFieldMatcher.group();
			simpleFieldsGroup = simpleFieldsGroup.replaceAll("\"", "");
			String[] keyValue = simpleFieldsGroup.split(":");
			simpleFields.put(keyValue[0], keyValue[1]);
		}
		
		return simpleFields;
	}
	
	//TODO
	public List<Map<String, String>> parseArrayFields(String arrayJSON) {
		Matcher arrayMatcher = Pattern.compile("(\\[[.\\s\\w\"\\{\\}:,]*\\],?)").matcher(arrayJSON);
		
		List<Map<String, String>> simpleArrayResults = new ArrayList<>();
		
		int deepSize = 0;	
		List<String> groupsResult = new ArrayList<>();
		while (arrayMatcher.find()) {
			groupsResult.add(arrayMatcher.group());
			deepSize++;
		}
		
		if (deepSize > 1) {
			
		} else { //	it can be [] and [][] size ???
			groupsResult.stream().forEach(simpleArrJson -> {
				simpleArrayResults.add(parseSimpleFields(simpleArrJson));
			});
		}
		
		return simpleArrayResults;
	}
	
	/**
	 * 
	 * Check if jSON String contains only simple data like {"data": "1", "some" : 2} or ["data": "1", "some" : 2]
	 * 
	 * @param jSON String
	 * @return boolean
	 */
	public boolean hasJSONSimpleData(String jSON) {
		Matcher simpleJSONMatcher = Pattern.compile("^[\\[\\{](\\s*?\"\\w+?\"\\s*?:\\s*?[\\d\\w\"]+,?)+\\s*?[\\]\\}]$")
				.matcher(jSON);
		return simpleJSONMatcher.matches();
	}
	
	/**
	 * 
	 * Removing composite fields from JSON and returns only simple JSON fields :
	 * values
	 * 
	 * @param jSON input String
	 * @param compositeFields Map<>
	 * @return
	 */
	public String removeCompositeFieldsFromJSON(String jSON, Map<String, String> compositeFields) {
		String simpleFieldsJSON = jSON;

		for (String compositeField : compositeFields.values()) {
			if (simpleFieldsJSON.contains(compositeField)) {
				int endIndex = simpleFieldsJSON.indexOf(compositeField);
				int beginIndex = endIndex + compositeField.length();

				simpleFieldsJSON = simpleFieldsJSON.substring(0, endIndex)
						+ simpleFieldsJSON.substring(beginIndex, simpleFieldsJSON.length());
			}
		}

		simpleFieldsJSON = simpleFieldsJSON.replaceFirst(",}$", "}");

		return simpleFieldsJSON;
	}
	
	/**
	 * 
	 * @param jSON                 input String
	 * @param compositeFieldsNames List<String> with names of composite fields in
	 *                             instantiating Object
	 * @return Map<String, String> with key is composite field name and value JSON
	 *         String representation of object field
	 */
	public Map<String, String> findCompositeFields(String jSON, List<String> compositeFieldsNames) {
		Map<String, String> compositeFields = new HashMap<>();

		if (compositeFieldsNames.size() == 0)
			return compositeFields;

		for (String fieldName : compositeFieldsNames) {
			String compositeJson = getCompositeJSON(fieldName, jSON);
			compositeFields.put(fieldName, compositeJson);
		}

		return compositeFields;
	}
	
	/**
	 * 
	 * Get JSON String for giving composite field name like "compositeFieldName":{[
	 * complex value ]}
	 * 
	 * @param fieldName String
	 * @param jSON      String
	 * @return JSON String for giving composite field name
	 */
	private String getCompositeJSON(String fieldName, String jSON) {
		Integer startIndex = null;
		Integer endIndex = null;

		startIndex = findStartIndex(fieldName, jSON);

		if (startIndex == null) {
			return null;
		}

		endIndex = findEndIndex(jSON.substring(startIndex, jSON.length()).strip());

		if (endIndex == null) {
			return null;
		}

		endIndex = startIndex + endIndex + 1;

		return jSON.substring(startIndex, endIndex);
	}

	/**
	 * 
	 * Searching start index in composite JSON String
	 * 
	 * @param fieldName
	 * @param jSON String
	 * @return Integer end index
	 */
	private Integer findStartIndex(String fieldName, String jSON) {
		Integer startIndex = null;
		String compositeFieldPattern = "\\s{0,}" + "\"" + fieldName + "\"" + "\\s{0,}:\\s{0,}[\\[\\{]";

		Matcher fieldMathcer = Pattern.compile(compositeFieldPattern).matcher(jSON);

		if (fieldMathcer.find())
			startIndex = fieldMathcer.start();

		return startIndex;
	}

	/**
	 * 
	 * Searching end index in composite JSON String
	 * 
	 * @param startCompositeJsonObject JSON String that starts with "key" : { or [
	 * @return Integer end index
	 */
	private Integer findEndIndex(String startCompositeJsonObject) {
		Integer endIndex = null;

		Matcher beginJsonMatcher = Pattern.compile("^\"\\w+?\"\\s*?:\\s*?[\\[\\{]").matcher(startCompositeJsonObject);

		Integer bracketBeginIndex = null;
		if (beginJsonMatcher.find()) {
			bracketBeginIndex = beginJsonMatcher.end();
		}

		if (bracketBeginIndex == null) {
			return endIndex;
		}

		char bracketChar = startCompositeJsonObject.charAt(bracketBeginIndex - 1);

		int counter = 1;

		for (int i = bracketBeginIndex; i < startCompositeJsonObject.length(); i++) {
			char charCompositeJsonObject = startCompositeJsonObject.charAt(i);

			if (charCompositeJsonObject == bracketChar) {
				counter++;
			}

			if (charCompositeJsonObject == bracketChar + 2) {
				counter--;
			}

			if (counter == 0) {
				if (i == startCompositeJsonObject.length()) {
					endIndex = i;
				}

				if (startCompositeJsonObject.charAt(i + 1) == ',') {
					endIndex = i + 1;
				} else {
					endIndex = i;
				}
				break;
			}
		}

		return endIndex;
	}
}