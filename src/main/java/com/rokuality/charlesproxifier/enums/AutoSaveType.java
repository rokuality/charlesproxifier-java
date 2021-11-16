package com.rokuality.charlesproxifier.enums;

public enum AutoSaveType {

	CHARLES_SESSION_FILE("CharlesSessionFile"), COMMA_SEPARATED_FILE("CommaSeparatedFile"),
	HTTP_TRACE_FILE("HttpTraceFile"), XML_SUMMARY_FILE("XMLSummaryFile"), XML_SESSION_FILE("XMLSessionFile"),
	JSON_SUMMARY_FILE("JSONSummaryFile"), JSON_SESSION_FILE("JSONSessionFile"), HTTP_ARCHIVE("HttpArchive");

	private final String value;

	private AutoSaveType(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public static AutoSaveType getEnumByString(String value) {
		for (AutoSaveType type : AutoSaveType.values()) {
			if (value.equalsIgnoreCase(type.value)) {
				return type;
			}
		}
		return null;
	}

}
