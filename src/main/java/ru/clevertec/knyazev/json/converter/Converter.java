package ru.clevertec.knyazev.json.converter;

import java.util.logging.Level;
import java.util.logging.Logger;

public interface Converter {
	static final Logger logger = Logger.getLogger(Converter.class.getName());
	
	public static Boolean convertToBoolean(String value) {
		Boolean converted = null;

		if (value == null) {
			return converted;
		} 
		
		if (value.equalsIgnoreCase("true")) {
			converted = true;
		}
		
		if (value.equalsIgnoreCase("false")) {
			converted = false;
		}		

		return converted;
	}
	
	public static Byte convertToByte(String value) {
		Byte converted = null;

		try {
			converted = Byte.valueOf(value);
		} catch (NumberFormatException e) {
			logger.log(Level.WARNING, "Error when parsing String value=" + value + " to Short");
		}

		return converted;
	}
	
	public static Short convertToShort(String value) {
		Short converted = null;

		try {
			converted = Short.valueOf(value);
		} catch (NumberFormatException e) {
			logger.log(Level.WARNING, "Error when parsing String value=" + value + " to Short");
		}

		return converted;
	}
	
	public static Integer convertToInteger(String value) {
		Integer converted = null;

		try {
			converted = Integer.valueOf(value);
		} catch (NumberFormatException e) {
			logger.log(Level.WARNING, "Error when parsing String value=" + value + " to Integer");
		}

		return converted;
	}
	
	public static Long convertToLong(String value) {
		Long converted = null;

		try {
			converted = Long.valueOf(value);
		} catch (NumberFormatException e) {
			logger.log(Level.WARNING, "Error when parsing String value=" + value + " to Long");
		}

		return converted;
	}
	
	public static Float convertToFloat(String value) {
		Float converted = null;

		try {
			converted = Float.valueOf(value);
		} catch (NumberFormatException e) {
			logger.log(Level.WARNING, "Error when parsing String value=" + value + " to Float");
		}

		return converted;
	}
	
	public static Double convertToDouble(String value) {
		Double converted = null;

		try {
			converted = Double.valueOf(value);
		} catch (NumberFormatException e) {
			logger.log(Level.WARNING, "Error when parsing String value=" + value + " to Double");
		}

		return converted;
	}
	
	public static Character convertToCharacter(String value) {
		Character converted = null;
		
		if (value == null || value.length() != 1) {
			return converted;
		} else {
			converted = Character.valueOf(value.charAt(0));
		}

		return converted;
	}
}
