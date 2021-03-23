package com.amituofo.cmdlet.core;

public class Argument {
	private int index;
	private String name;
	private String[] values;

	public Argument(int index, String name, String[] values) {
		super();
		this.index = index;
		this.name = name;
		this.values = values;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean hasValue(String value, boolean caseSensitve) {
		if (values == null || values.length == 0) {
			return false;
		}

		if (caseSensitve) {
			for (String v : values) {
				if (v.equals(value)) {
					return true;
				}
			}
		} else {
			for (String v : values) {
				if (v.equalsIgnoreCase(value)) {
					return true;
				}
			}
		}
		return false;
	}

	public String[] getValues() {
		return values;
	}

	public void setValues(String[] values) {
		this.values = values;
	}

}
