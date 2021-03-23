package com.amituofo.cmdlet.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.security.InvalidParameterException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public abstract class Command<T extends CommandArguments> {
	public static final PrintStream console = System.out;

	private final Class<T> argumentClass;

	protected ExitResult exitResult = ExitResult.COMPLETE;

	public Command(Class<T> commandArguments) {
		super();
		this.argumentClass = commandArguments;
	}

	public T createCommandArgument() {
		try {
			return argumentClass.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public void printHelp(PrintStream out) {
		createCommandArgument().printHelp(out);
	}

	public void printVersion(PrintStream out) {
		createCommandArgument().printVersion(out);
	}

	public ExitResult start(String[] args, boolean oneInstance) throws CommandException {

		T cmdArg = this.createCommandArgument();

		try {
			Arguments argument = Arguments.parse(args, false);
			cmdArg.setArguements(argument);

			if (argument.hasArgument("h") || argument.hasArgument("help")) {
				cmdArg.printHelp(Command.console);
				return ExitResult.START_FAIL;
			}

			if (argument.hasArgument("version")) {
				cmdArg.printVersion(Command.console);
				return ExitResult.START_FAIL;
			}

			initialize(cmdArg);

			// System.setProperty("log4j2.debug", "false");
			if (argument.hasArgument("debug")) {
				System.setProperty("log4j2.debug", "true");
				printArgs(args);
			}

			String configFilename = argument.getValue("log4j", 0);
			// ConfigurationSource source;
			if (configFilename == null) {
				// InputStream inputStream = CommandExecutor.class.getResourceAsStream("/log4j2.xml");
				// if (inputStream != null) {
				// source = new ConfigurationSource(inputStream);
				// Configurator.initialize(null, source);
				// }
			} else {
				File configFile = new File(configFilename);
				if (!configFile.exists()) {
					throw new InvalidParameterException("Log4j configuration file not exist! " + configFilename);
				} else {
					org.apache.logging.log4j.core.config.ConfigurationSource source = new org.apache.logging.log4j.core.config.ConfigurationSource(new FileInputStream(configFile));
					org.apache.logging.log4j.core.config.Configurator.initialize(null, source);

					System.setProperty("config.log4j", configFile.getAbsolutePath());
				}
			}

			cmdArg.validate(argument);
			cmdArg.inject(argument);
		} catch (Throwable e) {
			e.printStackTrace();
			throw new CommandException("Exception occured when parsing arguments! ", e);
		}

		return start(cmdArg, oneInstance);
	}

	public ExitResult start(T arg, boolean oneInstance) throws CommandException {
		if (oneInstance && CommandExecutor.isRunning(this.getClass())) {
			console.println("Only one instance can be running at same time!");

			this.exitResult = ExitResult.START_FAIL;
			return ExitResult.START_FAIL;
		}

		return start(arg);
	}

	protected abstract ExitResult initialize(T arg) throws CommandException;

	public abstract ExitResult start(T arg) throws CommandException;

	public abstract ExitResult stop() throws CommandException;

	private static void printArgs(String[] args) {
		console.println("-System Env-----------------------------------------------------------------------------------------------------------------------");
		Map<String, String> env = System.getenv();
		for (Iterator<String> iterator = env.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			Object val = env.get(key);
			if (val != null) {
				val = val.toString().replace("\r", "\\r").replace("\n", "\\n");
			}
			console.println(key + "=" + val);
		}
		console.println("-System Properties----------------------------------------------------------------------------------------------------------------");
		Properties props = System.getProperties();
		for (Iterator<Object> iterator = props.keySet().iterator(); iterator.hasNext();) {
			Object key = (Object) iterator.next();
			Object val = props.get(key);
			if (val != null) {
				val = val.toString().replace("\r", "\\r").replace("\n", "\\n");
			}
			console.println(key + "=" + val);
		}
		File directory = new File(".");
		console.println("work.dir=" + directory.getAbsolutePath());
		console.println("-Parameters-----------------------------------------------------------------------------------------------------------------------");
		if (args != null) {
			String[] arrayOfString = args;
			int j = args.length;
			for (int i = 0; i < j; i++) {
				String arg = arrayOfString[i];
				console.println(arg);
			}
		}
		console.println("----------------------------------------------------------------------------------------------------------------------------------");
	}

}
