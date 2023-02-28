package ru.clevertec.knyazev.json;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

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
						&& !String.class.isAssignableFrom(field.getType()) && !Boolean.class.isAssignableFrom(field.getType()))
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
						|| String.class.isAssignableFrom(field.getType()) || Boolean.class.isAssignableFrom(field.getType()))
				.forEach(field -> objNames.add(field.getName()));
		return objNames;
	}
	
	default <T> void setSimpleObjectFields(T object, String simpleJSON) {
		
	}

}
