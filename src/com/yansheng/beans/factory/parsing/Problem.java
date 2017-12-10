package com.yansheng.beans.factory.parsing;

import org.springframework.util.Assert;

public class Problem {

	private final String message;
	private final Location location;
	private final ParseState parseState;
	private final Throwable rootCause;

	public Problem(String message, Location location) {
		this(message, location, null, null);
	}

	public Problem(String message, Location location, ParseState parseState) {
		this(message, location, parseState, null);
	}

	public Problem(String message, Location location, ParseState parseState, Throwable rootCause) {
		Assert.notNull(message, "message不能为null");
		Assert.notNull(location, "location不能为null");
		this.message = message;
		this.location = location;
		this.parseState = parseState;
		this.rootCause = rootCause;
	}

	public String getMessage() {
		return this.message;
	}

	public Location getLocation() {
		return this.location;
	}

	public String getResourceDescription() {
		return getLocation().getResource().getDescription();
	}

	public ParseState getParseState() {
		return this.parseState;
	}

	public Throwable getRootCause() {
		return this.rootCause;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Configuration problem: ");
		sb.append(getMessage());
		sb.append('\n');
		sb.append("Offending resource: ");
		sb.append(getResourceDescription());
		if (getParseState() != null) {
			sb.append('\n');
			sb.append(getParseState());
		}
		return sb.toString();
	}

}
