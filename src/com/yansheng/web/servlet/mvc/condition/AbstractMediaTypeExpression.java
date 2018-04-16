package com.yansheng.web.servlet.mvc.condition;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeException;

public abstract class AbstractMediaTypeExpression
		implements Comparable<AbstractMediaTypeExpression>, MediaTypeExpression {

	protected final Log logger = LogFactory.getLog(getClass());
	private final MediaType mediaType;
	private final boolean isNegated;

	AbstractMediaTypeExpression(String expression) {
		if (expression.startsWith("!")) {
			this.isNegated = true;
			expression = expression.substring(1);
		} else {
			this.isNegated = false;
		}
		this.mediaType = MediaType.parseMediaType(expression);
	}

	AbstractMediaTypeExpression(MediaType mediaType, boolean isNegated) {
		this.isNegated = isNegated;
		this.mediaType = mediaType;
	}

	@Override
	public MediaType getMediaType() {
		return this.mediaType;
	}

	@Override
	public boolean isNegated() {
		return this.isNegated;
	}

	public boolean match(HttpServletRequest request) {
		try {
			boolean match = matchMediaType(request);
			return !isNegated ? match : !match;
		} catch (HttpMediaTypeException ex) {
			return false;
		}
	}

	protected abstract boolean matchMediaType(HttpServletRequest request) throws HttpMediaTypeException;

	@Override
	public int compareTo(AbstractMediaTypeExpression other) {
		return MediaType.SPECIFICITY_COMPARATOR.compare(this.mediaType, other.mediaType);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj != null && getClass().equals(obj.getClass())) {
			AbstractMediaTypeExpression other = (AbstractMediaTypeExpression) obj;
			return (this.mediaType.equals(other.mediaType)) && (this.isNegated == other.isNegated);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return mediaType.hashCode();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (isNegated) {
			builder.append('!');
		}
		builder.append(mediaType.toString());
		return builder.toString();
	}

}
