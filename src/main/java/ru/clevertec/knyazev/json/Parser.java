package ru.clevertec.knyazev.json;

import java.util.Map;

/**
 * 
 * Interface for parsing any String to get some data.
 * 
 * @author Vitya Knyazev
 *
 */
public interface Parser {
	/**
	 * 
	 * @param can be any String or simpleJSON String like {"data": "1", "some" : 2}
	 * @return Map<String, String> key - value pairs.
	 */
	Map<String, String> parseSimpleFields(String anyString);
}
