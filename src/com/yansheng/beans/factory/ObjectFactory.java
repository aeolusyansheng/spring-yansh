package com.yansheng.beans.factory;

import com.yansheng.beans.BeansException;

public interface ObjectFactory<T> {
	T getObject() throws BeansException;
}
