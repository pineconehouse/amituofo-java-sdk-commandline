package com.amituofo.cmdlet.core;

public class ArgumentParseException extends Exception {
	private static final long serialVersionUID = 5244518939462514059L;

	public ArgumentParseException() {
	}

	public ArgumentParseException(String msg) {
		super(msg);
	}

	public ArgumentParseException(Throwable cause) {
		super(cause);
	}

	public ArgumentParseException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
