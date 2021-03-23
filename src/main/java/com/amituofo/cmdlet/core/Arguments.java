package com.amituofo.cmdlet.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Arguments {
	private boolean argNameCaseSensetive = true;
	private final Map<String, Argument> arguments = new HashMap<String, Argument>();
	private String[] orginalArguments;

	public Arguments() {
	}

	public Arguments(boolean argNameCaseSensetive) {
		this.argNameCaseSensetive = argNameCaseSensetive;
	}

	public String[] getOrginalArguments() {
		return orginalArguments;
	}

	public void setOrginalArguments(String[] orginalArguments) {
		this.orginalArguments = orginalArguments;
	}

	public void put(String argName, String[] values) {
		if (!this.argNameCaseSensetive) {
			argName = argName.toLowerCase();
		}

		Argument arg = new Argument(this.arguments.size(), argName, values);

		this.arguments.put(argName, arg);
		// this.argumentsList.add(new String[] {});
	}

	public void appendValue(String argName, String value) {
		if (!this.argNameCaseSensetive) {
			argName = argName.toLowerCase();
		}

		Argument arg = this.arguments.get(argName);
		String[] valuesOld = (String[]) arg.getValues();
		String[] valuesNew = null;

		value = value.replaceAll("\"", "");

		if (valuesOld == null) {
			valuesNew = new String[] { value };
		} else {
			valuesNew = new String[valuesOld.length + 1];
			System.arraycopy(valuesOld, 0, valuesNew, 0, valuesOld.length);
			valuesNew[(valuesOld.length - 1)] = value;
		}

		arg.setValues(valuesNew);

		this.arguments.put(argName, arg);
	}

	public boolean hasArgument(String argName) {
		if (!this.argNameCaseSensetive) {
			argName = argName.toLowerCase();
		}

		return this.arguments.containsKey(argName);
	}

	public boolean hasArgumentValue(String argName) {
		if (!hasArgument(argName)) {
			return false;
		}

		String[] vals = getValues(argName);
		return vals != null && vals.length > 0 && vals[0].length() > 0;
	}

	public String[] getValues(String argName) {
		return getValues(argName, null);
	}

	public Argument getArgument(int index) {
		Collection<Argument> c = this.arguments.values();
		for (Argument argument : c) {
			if (argument.getIndex() == index) {
				return argument;
			}
		}

		return null;
	}

	public String getOrginalArgument(int index) {
		if (orginalArguments != null) {
			if (index >= 0 && index < orginalArguments.length) {
				return orginalArguments[index];
			}
		}

		return null;
	}

	public String[] getValue(int index, String[] defaultValues) {
		Argument argument = getArgument(index);
		if (argument != null) {
			return argument.getValues();
		}

		return defaultValues;
	}

	public String[] getValues(String argName, String[] defaultValues) {
		if (!this.argNameCaseSensetive) {
			argName = argName.toLowerCase();
		}

		Argument arg = this.arguments.get(argName);
		if (arg != null) {
			String[] vs = arg.getValues();

			if (vs != null && vs.length > 0) {
				return vs;
			}
		}

		return defaultValues;
	}
	
	public String[] getAllValues() {
		List<String> values = new ArrayList<String>();
		 Collection<Argument> argValues = this.arguments.values();
		if (argValues != null) {
			for (Argument argument : argValues) {
				String[] vs = argument.getValues();

				if (vs != null && vs.length > 0) {
					for (String string : vs) {
						values.add(string);
					}
				}
			}
			
		}

		return values.toArray(new String[values.size()]);
	}

	public String getValue(String argName, int valueIndex) {
		return getValue(argName, valueIndex, null);
	}

	public String getValue(String argName, int valueIndex, String defaultValue) {
		if (!this.argNameCaseSensetive) {
			argName = argName.toLowerCase();
		}

		Argument arg = this.arguments.get(argName);
		if (arg != null) {
			String[] values = arg.getValues();
			if ((values != null) && (valueIndex <= values.length - 1) && (values[valueIndex].length() > 0)) {
				return values[valueIndex];
			}
		}

		return defaultValue;
	}

	public int getIntValue(String argName, int valueIndex) {
		return Integer.parseInt(getValue(argName, valueIndex, "0"));
	}

	public int getIntValue(String argName, int valueIndex, int defaultValue) {
		return Integer.parseInt(getValue(argName, valueIndex, String.valueOf(defaultValue)));
	}

	public boolean getBooleanValue(String argName, int valueIndex) {
		return Boolean.parseBoolean(getValue(argName, valueIndex, "false"));
	}

	public boolean getBooleanValue(String argName, int valueIndex, boolean defaultValue) {
		String v = getValue(argName, valueIndex, String.valueOf(defaultValue));

		return ("1".equals(v)) || ("true".equalsIgnoreCase(v));
	}

	public int getValueCount(String argName) {
		if (!this.argNameCaseSensetive) {
			argName = argName.toLowerCase();
		}

		return getValues(argName).length;
	}

	// public void parse(String[] args, boolean argNameCaseSensetive) throws ArgumentParseException {
	//
	// }

	public static Arguments parse(final String[] args, final boolean argNameCaseSensetive) throws ArgumentParseException {
		Arguments argument = new Arguments(argNameCaseSensetive);

		if (args.length >= 1) {
			String[] orgArgs = new String[args.length - 1];
			for (int i = 0; i < orgArgs.length; i++) {
				orgArgs[i] = args[i + 1];
			}
			argument.setOrginalArguments(orgArgs);
		}

		if (args != null && args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				String arg = args[i].trim();

				if (!arg.startsWith("-") && !arg.startsWith("--")) {
					continue;
				}

				String argName = arg.substring(arg.startsWith("--") ? 2 : 1).trim();

				List<String> argValues = new ArrayList<String>();

				boolean hasNextValue = false;
				do {
					i++;
					int index = i;
					if (index >= args.length) {
						break;
					}

					arg = args[index];
					if (!arg.startsWith("-")) {
						String argValue = arg.replaceAll("\"", "");
						argValues.add(argValue);
						hasNextValue = true;
					} else {
						i--;
						hasNextValue = false;
					}
				} while (hasNextValue);

				argument.put(argName, (String[]) argValues.toArray(new String[argValues.size()]));
			}
		}

		return argument;
	}

	public static String[] parse(String commandLine) {
		int groupFlg = 0;
		StringBuilder sb = new StringBuilder();
		List<String> argList = new ArrayList<String>();
		char[] chars = commandLine.toCharArray();
		for (char c : chars) {
			if (c == '"') {
				if (groupFlg == 1) {
					groupFlg--;
				} else {
					groupFlg++;
				}

				// find "ddd"
				if (groupFlg == 0) {
					if (sb.length() > 0) {
						argList.add(sb.toString());
						sb = new StringBuilder();
						continue;
					}
				} else {
//					sb.append(c);
					continue;
				}
			} else if (c == ' ' && groupFlg == 0) {
				if (sb.length() > 0) {
					argList.add(sb.toString());
					sb = new StringBuilder();
					continue;
				}
			} else {
				sb.append(c);
			}
		}

		if (sb.length() > 0) {
			argList.add(sb.toString());
		}

		return argList.toArray(new String[argList.size()]);
	}
}
