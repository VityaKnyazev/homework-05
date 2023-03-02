package ru.clevertec.knyazev.json;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import ru.clevertec.knyazev.json.converter.Converter;
import ru.clevertec.knyazev.json.exception.ParserException;

/**
 * 
 * This interface should be using for parsing JSON to object
 * 
 * @author Vitya Knyazev
 *
 */
public interface JsonToObjectParser {
	Logger logger = Logger.getLogger(JsonToObjectParser.class.getName());

	/**
	 * @param <T>      is an object type that might be creating from JSON format
	 * @param jSON     string in JSON format
	 * @param objClass class for creating T object
	 * @return T object from parsed JSON string
	 * @throws ParserException can be thrown when error on parsing process
	 */
	<T> T parseToObject(String jSON, Class<T> objClass) throws ParserException;

	default <T> T instantiateObject(Class<T> objClass) throws ParserException {
		T objInstance = null;

		if (objClass == null)
			return objInstance;

		try {
			objInstance = objClass.getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			logger.log(Level.SEVERE, "Error. Can't instantiate object from class constructor!", e);
			throw new ParserException("Error. Can't instantiate object from class constructor!", e);
		}

		return objInstance;
	}

	default <T> List<String> getCompositeObjectFields(T object) {
		List<String> objNames = new ArrayList<>();

		Field[] fields = object.getClass().getDeclaredFields();

		if (fields == null || fields.length == 0)
			return objNames;

		Stream.of(fields)
				.filter(field -> !field.getType().isPrimitive() && !Number.class.isAssignableFrom(field.getType())
						&& !String.class.isAssignableFrom(field.getType())
						&& !Boolean.class.isAssignableFrom(field.getType()))
				.forEach(field -> objNames.add(field.getName()));
		return objNames;
	}

	default <T> List<String> getSimpleObjectFields(T object) {
		List<String> objNames = new ArrayList<>();

		Field[] fields = object.getClass().getDeclaredFields();

		if (fields == null || fields.length == 0)
			return objNames;

		Stream.of(fields)
				.filter(field -> field.getType().isPrimitive() || Number.class.isAssignableFrom(field.getType())
						|| String.class.isAssignableFrom(field.getType())
						|| Boolean.class.isAssignableFrom(field.getType()))
				.forEach(field -> objNames.add(field.getName()));
		return objNames;
	}

	
	default <T> void setSimpleObjectFields(T object, Map<String, String> fieldsValues) {
		List<String> simpleFieldsNames = getSimpleObjectFields(object);
		Field[] fields = object.getClass().getDeclaredFields();

		Arrays.stream(fields).forEach(field -> {

			if (simpleFieldsNames.contains(field.getName())) {
				field.setAccessible(true);
				
				try {
					if (field.getType() == boolean.class || field.getType() == Boolean.class) {
						Boolean value = Converter.convertToBoolean(fieldsValues.get(field.getName()));
						if (field.getType() == boolean.class) {
							value = (value == null) ? false : value;
						}							
						field.set(object, value);
					}
					if (field.getType() == byte.class || field.getType() == Byte.class) {
						Byte value = Converter.convertToByte(fieldsValues.get(field.getName()));
						if (field.getType() == byte.class) {
							value = (value == null) ? 0 : value;
						}
						field.set(object, value);
					}
					if (field.getType() == short.class || field.getType() == Short.class) {
						Short value = Converter.convertToShort(fieldsValues.get(field.getName()));
						if (field.getType() == short.class) {
							value = (value == null) ? 0 : value;
						}
						field.set(object, value);
					}
					if (field.getType() == int.class || field.getType() == Integer.class) {
						Integer value = Converter.convertToInteger(fieldsValues.get(field.getName()));
						if (field.getType() == int.class) {
							value = (value == null) ? 0 : value;
						}
						field.set(object, value);
					}
					if (field.getType() == long.class || field.getType() == Long.class) {
						Long value = Converter.convertToLong(fieldsValues.get(field.getName()));
						if (field.getType() == long.class) {
							value = (value == null) ? 0L : value;
						}
						field.set(object, value);
					}					
					if (field.getType() == float.class || field.getType() == Float.class) {
						Float value = Converter.convertToFloat(fieldsValues.get(field.getName()));
						if (field.getType() == float.class) {
							value = (value == null) ? 0F : value;
						}
						field.set(object, value);
					}
					if (field.getType() == double.class || field.getType() == Double.class) {
						Double value = Converter.convertToDouble(fieldsValues.get(field.getName()));
						if (field.getType() == double.class) {
							value = (value == null) ? 0d : value;
						}
						field.set(object, value);
					}
					if (field.getType() == char.class || field.getType() == Character.class) {
						Character value = Converter.convertToCharacter(fieldsValues.get(field.getName()));
						if (field.getType() == char.class) {
							value = (value == null) ? '\u0000' : value;
						}
						field.set(object, value);
					}
					if (field.getType() == String.class) {
						field.set(object, fieldsValues.get(field.getName()));
					}
					
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}

		});

	}

}
