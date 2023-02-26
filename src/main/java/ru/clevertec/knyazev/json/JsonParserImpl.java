package ru.clevertec.knyazev.json;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonParserImpl implements JsonParser {

	@Override
	public <T> String parseToJson(T baseObject) {
		if (baseObject == null) {
			return "{}";
		}

		Field[] fields = baseObject.getClass().getDeclaredFields();

		// if object doesn't contain fields
		if (fields == null || fields.length == 0) {
			return "{}";
		}

		final String lastFieldName = fields[fields.length - 1].getName();

		String json = Stream.of(fields).peek(field -> field.setAccessible(true)).map(field -> {
			String result = "";

			if (isCompositeField(field)) {
				if (isArrayField(field)) {
					result = parseArray(field, baseObject);
				} else if (isCollectionField(field)) {
					result = parseCollection(field, baseObject);
				} else { // Parse composite Object class like Simple.class
					try {
						result = "\"" + field.getName() + "\"" + " : " + parseToJson(field.get(baseObject)) + ",";
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			} else {
				try {
					result = "\"" + field.getName() + "\"" + " : " + "\"" + field.get(baseObject) + "\",";

					if (lastFieldName.equals(field.getName())) {
						result = result.substring(0, result.length() - 1);
					}
				} catch (IllegalAccessException | IllegalArgumentException e) {
					e.printStackTrace();
				}
			}
			return result;
		}).collect(
				Collectors.joining(System.lineSeparator(), "{" + System.lineSeparator(), System.lineSeparator() + "}"));

		json = json.replaceFirst(",\r\n}$", "\r\n}");

		return json;
	}

//	@Override
//	public <T> T parseToObject(String jSon, Class<T> objClass) {
//		T objInstance = null;
//
//		try {
//			objInstance = objClass.getConstructor().newInstance();
//		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
//				| NoSuchMethodException | SecurityException e) {
//			e.printStackTrace();
//		}
//
//		return objInstance;
//	}

	private String parseArray(Field baseArrField, Object baseObject) {
		String result = "";

		try {
			Object array = baseArrField.get(baseObject);

			if (array == null)
				return result;

			int arrLength = Array.getLength(array);

			result = "\"" + baseArrField.getName() + "\"" + " : [" + System.lineSeparator();

			for (int i = 0; i < arrLength; i++) {
				Object arrObject = Array.get(array, i);
				if (isCompositeOject(arrObject)) {
					if (arrObject.getClass().isArray()) { // When array[]
						result += parseArray(arrObject);
					} else { // When composite Object like simple.class
						result += parseToJson(arrObject) + "," + System.lineSeparator();
					}
				} else { // When simple value
					result += "\"" + arrObject + "\"," + System.lineSeparator();
				}
			}

			result = result.substring(0, result.length() - 3);
			result += System.lineSeparator() + "],";
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}

		return result;
	}

	private String parseArray(Object arrObject) {
		String result = "";

		if (arrObject == null)
			return result;

		int innerArrLength = Array.getLength(arrObject);

		if (innerArrLength == 0)
			return result;

		result += "[" + System.lineSeparator();

		for (int j = 0; j < innerArrLength; j++) {
			Object innerArrObject = Array.get(arrObject, j);
			if (isCompositeOject(innerArrObject)) {
				if (innerArrObject.getClass().isArray()) {
					result += parseArray(innerArrObject);
				} else { // When composite Object like Simple.class
					result += parseToJson(innerArrObject) + "," + System.lineSeparator();
				}
			} else {
				result += "\"" + innerArrObject + "\"," + System.lineSeparator();
			}

		}

		result = result.substring(0, result.length() - 3);
		result += System.lineSeparator() + "]," + System.lineSeparator();

		return result;

	}

	private String parseCollection(Field baseCollField, Object baseObject) {
		String result = "";

		try {
			Collection<?> collectionObject = (Collection<?>) baseCollField.get(baseObject);

			if (collectionObject == null || collectionObject.size() == 0)
				return result;

			result = "\"" + baseCollField.getName() + "\"" + " : [" + System.lineSeparator();

			for (Object innerCollObject : collectionObject) {

				if (isCompositeOject(innerCollObject)) {
					if (innerCollObject.getClass().isArray()) {
						result += parseArray(innerCollObject);
					} else if (innerCollObject instanceof Collection) {
						result += parseCollection(innerCollObject);
					} else { // When composite Object like Simple.class
						result += parseToJson(innerCollObject) + "," + System.lineSeparator();
					}
				} else {// Simple field
					result += "\"" + innerCollObject + "\"," + System.lineSeparator();
				}
			}

			result = result.substring(0, result.length() - 3);
			result += System.lineSeparator() + "],";

		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}

		return result;
	}

	private String parseCollection(Object collObject) {
		String result = "";

		if (collObject == null)
			return result;

		Collection<?> innerCollObject = (Collection<?>) collObject;

		if (innerCollObject == null || innerCollObject.size() == 0)
			return result;

		result += "[" + System.lineSeparator();

		for (Object innerObject : innerCollObject) {

			if (isCompositeOject(innerObject)) {
				if (innerObject.getClass().isArray()) {
					result += parseArray(innerObject);
				} else if (innerObject instanceof Collection) {
					result += parseCollection(innerObject);
				} else { // When composite Object like Simple.class
					result += parseToJson(innerObject) + "," + System.lineSeparator();
				}
			} else {// Simple field
				result += "\"" + innerObject + "\"," + System.lineSeparator();
			}
		}

		result = result.substring(0, result.length() - 3);
		result += System.lineSeparator() + "]," + System.lineSeparator();

		return result;
	}

	private String parseMap(Field baseMapField, Object baseObject) {
		String result = "";

		try {
			Map<?, ?> mapObject = (Map<?, ?>) baseMapField.get(baseObject);

			if (mapObject == null || mapObject.size() == 0)
				return result;

			result = "\"" + baseMapField.getName() + "\"" + " : [" + System.lineSeparator();
			
			for (Map.Entry<?, ?> kvMap : mapObject.entrySet()) {
				Object keyKvMap = kvMap.getKey();
				Object valueKvMap = kvMap.getValue();
			}
			
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}

		return result;
	}

}
