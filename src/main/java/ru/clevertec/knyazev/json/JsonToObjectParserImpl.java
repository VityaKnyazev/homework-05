package ru.clevertec.knyazev.json;

import java.util.List;
import java.util.Map;

import ru.clevertec.knyazev.json.exception.ParserException;

public class JsonToObjectParserImpl implements JsonToObjectParser {
	private JSONStringParser jsonStringParser;
	
	public JsonToObjectParserImpl() {
		jsonStringParser = new JSONStringParser();
	}

	@Override
	public <T> T parseToObject(String jSON, Class<T> objClass) throws ParserException {
		T objInstance = instantiateObject(objClass);

		List<String> compositeFieldsNames = getCompositeObjectFields(objInstance);

		Map<String, String> compositeFields = jsonStringParser.findCompositeFields(jSON, compositeFieldsNames);
		
		//TODO
//		jsonStringParser.parseArrayFields(compositeFields.get("strArr"));		
		
		String simpleJSON = jsonStringParser.removeCompositeFieldsFromJSON(jSON, compositeFields);

		if (jsonStringParser.hasJSONSimpleData(simpleJSON)) {
			Map<String, String> simpleFields = jsonStringParser.parseSimpleFields(simpleJSON);
			setSimpleObjectFields(objInstance, simpleFields);
		}
		
		return objInstance;
	}
}
