package com.yansheng.beans.factory.parsing;

import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

public class Location {

	private final Resource resouce;
	private final Object source;

	public Location(Resource resouce) {
		this(resouce, null);
	}

	public Location(Resource resouce, Object source) {
		Assert.notNull(resouce, "resouce不能为NUll");
		this.resouce = resouce;
		this.source = source;
	}

	public Resource getResource() {
		return this.resouce;
	}

	public Object getSource() {
		return this.source;
	}
}
