package com.amituofo.cmdlet.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

public abstract class CommandArguments {
	protected String version = "0.1";
	private Arguments args;

	public CommandArguments(String version) {
		this.version = version;
	}

	public Arguments getArguements() {
		return args;
	}

	public void setArguements(Arguments args) {
		this.args = args;
	}

	public abstract void inject(Arguments args) throws ArgumentParseException;

	public abstract void validate(Arguments args) throws ArgumentParseException;

	public void printHelp(PrintStream out) {
		try {
			InputStream is = this.getClass().getResourceAsStream(this.getClass().getSimpleName() + ".help");
			if (is != null) {
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String line;
				while ((line = br.readLine()) != null) {
					out.println(line);
				}
				br.close();
				isr.close();
				is.close();
			} else {
				out.println("Doc not found!");
			}
		} catch (IOException e) {
			// e.printStackTrace();
		}
	}

	public void printVersion(PrintStream out) {
		out.println("Version " + version);
	}
}
