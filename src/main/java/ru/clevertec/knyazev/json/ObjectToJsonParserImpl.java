package ru.clevertec.knyazev.json;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ObjectToJsonParserImpl implements ObjectToJsonParser {
	private static final Logger logger = Logger.getLogger(ObjectToJsonParserImpl.class.getName());

	@Override
	public <T> String parseToJson(T baseObject) {
		if (baseObject == null) {
			return "{}";
		}

		Field[] fields = baseObject.getClass().getDeclaredFields();

		if (fields == null || fields.length == 0) {
			return "{}";
		}

		final String lastFieldName = fields[fields.length - 1].getName();

		String json = Stream.of(fields).peek(field -> field.setAccessible(true)).map(field -> {
			String result = "";

			if (isCompositeField(field)) {
				if (isArrayField(field)) {
					result = parseArray(baseObject, field);
				} else if (isCollectionField(field)) {
					result = parseCollection(baseObject, field);
				} else if (isMapField(field)) {
					result = parseMap(baseObject, field);
				} else {
					try {
						result = "\"" + field.getName() + "\"" + ":" + parseToJson(field.get(baseObject)) + ",";
					} catch (IllegalArgumentException | IllegalAccessException e) {
						logger.log(Level.SEVERE, "Error when parsing inner composite object!", e);
					}
				}
			} else {
				result = parseSimpleField(baseObject, field);

				if (lastFieldName.equals(field.getName())) {
					result = result.substring(0, result.length() - 1);
				}
			}
			return result;
		}).collect(Collectors.joining("", "{", "}"));

		json = json.replaceFirst(",}$", "}");

		return json;
	}

	private String parseSimpleField(Object baseObject, Field... baseField) {
		String result = null;

		if (baseObject == null)
			return "";

		if (baseField != null && baseField.length == 1) {
			try {
				Object value = baseField[0].get(baseObject);
				if (value instanceof String) {
					result = "\"" + baseField[0].getName() + "\"" + ":" + "\"" + value + "\",";
				} else {
					result = "\"" + baseField[0].getName() + "\"" + ":" + value + ",";
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.log(Level.SEVERE, "Error when parsing simple field!", e);
			}
		} else {
			result = "\"" + baseObject + "\",";
		}

		return result;
	}

	private String parseArray(Object baseObject, Field... baseArrField) {
		String result = null;
		Object array = null;

		if (baseObject == null)
			return "";

		if (baseArrField != null && baseArrField.length == 1) {

			try {
				array = baseArrField[0].get(baseObject);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.log(Level.SEVERE, "Error when parsing array!", e);
			}

			result = "\"" + baseArrField[0].getName() + "\"" + ":[";

		} else {
			array = baseObject;
			result = "[";
		}

		if (array == null)
			return "";

		int arrLength = Array.getLength(array);
		if (arrLength == 0)
			return "";

		for (int i = 0; i < arrLength; i++) {
			Object arrObject = Array.get(array, i);

			result += parseByCase(arrObject);
		}

		result = result.substring(0, result.length() - 1);
		result += "],";

		return result;
	}

	private String parseCollection(Object baseObject, Field... baseCollField) {
		String result = null;
		Collection<?> collectionObject = null;

		if (baseObject == null)
			return "";

		if (baseCollField != null && baseCollField.length == 1) {
			try {
				collectionObject = (Collection<?>) baseCollField[0].get(baseObject);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.log(Level.SEVERE, "Error when parsing Collection!", e);
			}
			result = "\"" + baseCollField[0].getName() + "\"" + ":[";
		} else {
			collectionObject = (Collection<?>) baseObject;
			result = "[";
		}

		if (collectionObject == null || collectionObject.size() == 0)
			return result;

		for (Object innerCollObject : collectionObject) {
			result += parseByCase(innerCollObject);
		}

		result = result.substring(0, result.length() - 1);
		result += "],";

		return result;
	}

	private String parseMap(Object baseObject, Field... baseMapField) {
		String result = null;
		Map<?, ?> mapObject = null;

		if (baseObject == null)
			return "";

		if (baseMapField != null && baseMapField.length == 1) {
			try {
				mapObject = (Map<?, ?>) baseMapField[0].get(baseObject);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.log(Level.SEVERE, "Error when parsing Map!", e);
			}

			result = "\"" + baseMapField[0].getName() + "\"" + ":{";
		} else {
			mapObject = (Map<?, ?>) baseObject;
			result = "{";
		}

		if (mapObject == null || mapObject.isEmpty())
			return "";

		for (Map.Entry<?, ?> kvMap : mapObject.entrySet()) {
			Object keyKvMap = kvMap.getKey();
			Object valueKvMap = kvMap.getValue();

			result += parseByCase(keyKvMap);

			result = result.substring(0, result.length() - 1);
			result += ":";

			result += parseByCase(valueKvMap);

			result = result.substring(0, result.length() - 1);
			result += ",";
		}

		result = result.substring(0, result.length() - 1);
		result += "},";

		return result;
	}

	private String parseByCase(Object parsingObject) {
		String result = "";

		if (isCompositeOject(parsingObject)) {
			if (parsingObject.getClass().isArray()) {
				result += parseArray(parsingObject);
			} else if (parsingObject instanceof Collection) {
				result += parseCollection(parsingObject);
			} else if (parsingObject instanceof Map) {
				result += parseMap(parsingObject);
			} else {
				result += parseToJson(parsingObject) + ",";
			}
		} else {
			result += parseSimpleField(parsingObject);
		}

		return result;
	}

}
