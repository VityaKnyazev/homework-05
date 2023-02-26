package ru.clevertec.knyazev.json;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * This interface should be using for parsing object to JSON and vice versa
 * 
 * @author Vitya Knyazev
 *
 * @param <T> is an object that can be represented in JSON format
 */
public interface JsonParser {
//	<T> T parseToObject(String jSon, Class<T> objClass);

	<T> String parseToJson(T object);

	default public <T> boolean isCompositeOject(T object) {
		if (object == null) {
			return false;
		}

		return !(object instanceof Number || object instanceof String || object instanceof Boolean
				|| object.getClass().isPrimitive());
	}

	/**
	 * 
	 * Check if a field has not primitive type, not extends Number and String.
	 * 
	 * @param field is a refletion field representation of some class.
	 * @return true if field is instance of object that has not primitive type and
	 *         not extends Number and String, otherwise return false
	 */
	default boolean isCompositeField(Field field) {
		if (field == null)
			return false;

		return !(field.getType().isPrimitive() || Number.class.isAssignableFrom(field.getType())
				|| String.class.isAssignableFrom(field.getType()) || Boolean.class.isAssignableFrom(field.getType()));
	}

	/**
	 * 
	 * Check if field has array type
	 * 
	 * @param field is a refletion field representation of some class.
	 * @return true if field has array type, otherwise false.
	 */
	default boolean isArrayField(Field field) {
		if (field == null)
			return false;

		return field.getType().isArray();
	}

	default boolean isCollectionField(Field field) {
		if (field == null)
			return false;

		return Collection.class.isAssignableFrom(field.getType());
	}

//	default public <T> List<String> getCompositeObjectsNames(T object) {
//		List<String> objNames = new ArrayList<>();
//
//		Field[] fields = object.getClass().getDeclaredFields();
//
//		Stream.of(fields)
//				.filter(field -> !field.getType().isPrimitive() && !Number.class.isAssignableFrom(field.getType())
//						&& !String.class.isAssignableFrom(field.getType()))
//				.forEach(field -> objNames.add(field.getName()));
//		return objNames;
//	}

//	default public <T> Field getCompositeObject(T object, String fieldName)
//			throws NoSuchFieldException, SecurityException {
//		return object.getClass().getDeclaredField(fieldName);
//	}
}
