package com.yansheng.beans;

import java.io.PrintStream;
import java.io.PrintWriter;

import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

@SuppressWarnings("serial")
public class PropertyBatchUpdateException extends BeansException {

	private PropertyAccessException[] propertyAccessExceptions;

	public PropertyBatchUpdateException(PropertyAccessException[] propertyAccessExceptions) {
		super(null);
		Assert.notEmpty(propertyAccessExceptions, "propertyAccessExceptions至少需要一个元素。");
		this.propertyAccessExceptions = propertyAccessExceptions;
	}

	public final int getExceptionCount() {
		// 该方法不可覆盖
		// 返回值为0时，代表没有异常
		return this.propertyAccessExceptions.length;
	}

	public final PropertyAccessException[] getPropertyAccessExceptions() {
		return this.propertyAccessExceptions;
	}

	public PropertyAccessException getPropertyAccessException(String propertyName) {
		for (PropertyAccessException pae : this.propertyAccessExceptions) {
			if (ObjectUtils.nullSafeEquals(propertyName, pae.getPropertyName())) {
				return pae;
			}
		}
		return null;
	}

	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("解析失败的属性：");
		for (int i = 0; i < this.propertyAccessExceptions.length; i++) {
			sb.append(this.propertyAccessExceptions[i].getMessage());
			if (i < this.propertyAccessExceptions.length - 1) {
				sb.append(";");
			}
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getName());
		sb.append(";内置的属性解析异常PropertyAccessExceptions（");
		sb.append(getExceptionCount()).append(")are:");
		for (int i = 0; i < this.propertyAccessExceptions.length; i++) {
			sb.append('\n');
			sb.append("propertyAccessExceptions ");
			sb.append(i + 1).append(":");
			sb.append(this.propertyAccessExceptions[i]);
		}
		return sb.toString();
	}

	@Override
	public void printStackTrace(PrintStream ps) {
		synchronized (ps) {
			ps.println(getClass().getName() + ";内置的所有异常详细(" + getExceptionCount() + ") 为:");
			for (int i = 0; i < this.propertyAccessExceptions.length; i++) {
				ps.println("propertyAccessExceptions " + (i + 1) + ":");
				this.propertyAccessExceptions[i].printStackTrace();
			}
		}
	}

	@Override
	public void printStackTrace(PrintWriter pw) {
		synchronized (pw) {
			pw.println(getClass().getName() + ";内置的所有异常详细(" + getExceptionCount() + ") 为:");
			for (int i = 0; i < this.propertyAccessExceptions.length; i++) {
				pw.println("propertyAccessExceptions " + (i + 1) + ":");
				this.propertyAccessExceptions[i].printStackTrace();
			}
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean contains(Class exType) {
		if (exType == null) {
			return false;
		}
		if (exType.isInstance(this)) {
			return true;
		}
		for (PropertyAccessException pae : this.propertyAccessExceptions) {
			if (pae.contains(exType)) {
				return true;
			}
		}
		return false;
	}

}
