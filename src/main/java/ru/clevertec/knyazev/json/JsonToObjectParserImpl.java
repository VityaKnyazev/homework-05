package ru.clevertec.knyazev.json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.clevertec.knyazev.json.exception.ParserException;

public class JsonToObjectParserImpl implements JsonToObjectParser {

	@Override
	public <T> T parseToObject(String jSON, Class<T> objClass) throws ParserException {
		T objInstance = instantiateObject(objClass);

		List<String> compositeFieldsNames = getCompositeObjectFields(objInstance);

		Map<String, String> compositeFields = findCompositeFields(jSON, compositeFieldsNames);
		String simpleJSON = removeCompositeFieldsFromJSON(jSON, compositeFields);

		System.out.println(hasJSONSimpleData(simpleJSON));
		return objInstance;
	}

	/**
	 * 
	 * Removing composite fields from JSON and returns only simple JSON fields :
	 * values
	 * 
	 * @param jSON            input String
	 * @param compositeFields Map<>
	 * @return
	 */
	private String removeCompositeFieldsFromJSON(String jSON, Map<String, String> compositeFields) {
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

	private boolean hasJSONSimpleData(String jSON) {
		Matcher simpleJSONMatcher = Pattern.compile("^\\{(\\s*?\"\\w+?\"\\s*?:\\s*?[\\d\\w\"]+,?)+\\s*?\\}$")
				.matcher(jSON);
		return simpleJSONMatcher.matches();
	}

	/**
	 * 
	 * @param jSON                 input String
	 * @param compositeFieldsNames List<String> with names of composite fields in
	 *                             instantiating Object
	 * @return Map<String, String> with key is composite field name and value JSON
	 *         String representation of object field
	 */
	private Map<String, String> findCompositeFields(String jSON, List<String> compositeFieldsNames) {
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

	private Integer findStartIndex(String fieldName, String jSON) {
		Integer startIndex = null;
		String compositeFieldPattern = "\\s{0,}" + "\"" + fieldName + "\"" + "\\s{0,}:\\s{0,}[\\[\\{]";

		Matcher fieldMathcer = Pattern.compile(compositeFieldPattern).matcher(jSON);

		if (fieldMathcer.find())
			startIndex = fieldMathcer.start();

		return startIndex;
	}

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
