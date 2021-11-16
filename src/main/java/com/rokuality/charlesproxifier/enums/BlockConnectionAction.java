package com.rokuality.charlesproxifier.enums;

public enum BlockConnectionAction {

	DROP_CONNECTION("DropConnection"), RETURN_403("Return403");

	private final String value;

	private BlockConnectionAction(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public static BlockConnectionAction getEnumByString(String value) {
		for (BlockConnectionAction action : BlockConnectionAction.values()) {
			if (value.equalsIgnoreCase(action.value)) {
				return action;
			}
		}
		return null;
	}

}
