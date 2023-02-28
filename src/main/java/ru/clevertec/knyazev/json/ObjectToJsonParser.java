package ru.clevertec.knyazev.json;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

import ru.clevertec.knyazev.json.exception.ParserException;

/**
 * This interface should be using for parsing object to JSON
 * 
 * @author Vitya Knyazev
 *
 *
 */
public interface ObjectToJsonParser {

	/**
	 * 
	 * @param <T> is an object type that can be represented in JSON format
	 * @param object is some object, which field should be represented in JSON format
	 * @return String JSON representation of object fields
	 * @throws can throw ParserException
	 */
	<T> String parseToJson(T object) throws ParserException;

	/**
	 * 
	 * Check if object has not primitive type, not extends Boolean, Number and String.
	 * 
	 * @param <T> type of given object
	 * @param object that should be checking
	 * @return true if object is instance of object that has not primitive type and
	 *         not extends Number, Boolean and String, otherwise return false
	 */
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
	 * @param field is a reflection field representation of some class.
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
	 * @param field is a reflection field representation of some class.
	 * @return true if field has array type, otherwise false.
	 */
	default boolean isArrayField(Field field) {
		if (field == null)
			return false;

		return field.getType().isArray();
	}

	/**
	 * 
	 * Check if field has Collection type
	 * 
	 * @param field is a reflection field representation of some class.
	 * @return  true if field has Collection type, otherwise false.
	 */
	default boolean isCollectionField(Field field) {
		if (field == null)
			return false;

		return Collection.class.isAssignableFrom(field.getType());
	}
	
	/**
	 * 
	 * Check if field has Map type
	 * 
	 * @param field is a reflection field representation of some class.
	 * @return  true if field has Map type, otherwise false.
	 */
	default boolean isMapField(Field field) {
		if (field == null)
			return false;

		return Map.class.isAssignableFrom(field.getType());
	}

}
