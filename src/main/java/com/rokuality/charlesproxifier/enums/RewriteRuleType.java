package com.rokuality.charlesproxifier.enums;

public enum RewriteRuleType {

	ADD_HEADER("AddHeader"), MODIFY_HEADER("ModifyHeader"), REMOVE_HEADER("RemoveHeader"), HOST("Host"), PATH("Path"),
	URL("URL"), ADD_QUERY_PARAM("AddQueryParam"), MODIFY_QUERY_PARAM("ModifyQueryParam"),
	REMOVE_QUERY_PARAM("RemoveQueryParam"), RESPONSE_STATUS("ResponseStatus"), BODY("Body");

	private final String value;

	private RewriteRuleType(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public static RewriteRuleType getEnumByString(String value) {
		for (RewriteRuleType type : RewriteRuleType.values()) {
			if (value.equalsIgnoreCase(type.value)) {
				return type;
			}
		}
		return null;
	}

}
