package ru.clevertec.knyazev.json.exception;

public class ParserException extends Exception {
	private static final long serialVersionUID = 111541251L;
	
	public ParserException(String message) {
		super(message);
	}

	public ParserException(String message, Throwable cause) {
		super(message, cause);
	}	
}