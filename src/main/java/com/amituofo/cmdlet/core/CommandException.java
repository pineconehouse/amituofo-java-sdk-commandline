package com.amituofo.cmdlet.core;

public class CommandException extends Exception {
	private static final long serialVersionUID = -7754266545442392781L;
	private int exitCode = ExitResult.TERMINATE.exitCode;

	public CommandException() {
	}

	public CommandException(String msg) {
		super(msg);
	}

	public CommandException(Throwable cause) {
		super(cause);
	}

	public CommandException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public CommandException(int exitCode, String msg) {
		super(msg);
		this.exitCode = exitCode;
	}

	public CommandException(int exitCode, Throwable cause) {
		super(cause);
		this.exitCode = exitCode;
	}

	public CommandException(int exitCode, String msg, Throwable cause) {
		super(msg, cause);
		this.exitCode = exitCode;
	}

	public int getExitCode() {
		return this.exitCode;
	}

	public void setExitCode(int exitCode) {
		this.exitCode = exitCode;
	}
}
