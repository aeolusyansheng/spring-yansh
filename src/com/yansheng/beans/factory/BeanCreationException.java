package com.yansheng.beans.factory;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import org.springframework.core.NestedRuntimeException;

import com.yansheng.beans.FatalBeanException;

@SuppressWarnings("serial")
public class BeanCreationException extends FatalBeanException {

	private String beanName;
	private String resourceDescription;
	private List<Throwable> relatedCauses;

	public BeanCreationException(String msg) {
		super(msg);
	}

	public BeanCreationException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public BeanCreationException(String beanName, String msg) {
		super("创建bean失败。bean名:" + beanName + "消息:" + msg);
		this.beanName = beanName;
	}

	public BeanCreationException(String beanName, String msg, Throwable cause) {
		this(beanName, msg);
		initCause(cause);
	}

	public BeanCreationException(String resourceDescription, String beanName, String msg) {
		super("创建bean失败。bean名:" + beanName + (resourceDescription != null ? "资源描述:" + resourceDescription : "") + ",消息:"
				+ msg);
		this.beanName = beanName;
		this.resourceDescription = resourceDescription;
	}

	public BeanCreationException(String resourceDescription, String beanName, String msg, Throwable cause) {
		this(resourceDescription, beanName, msg);
		initCause(cause);
	}

	public String getBeanName() {
		return this.beanName;
	}

	public String getResourceDescription() {
		return this.resourceDescription;
	}

	public void addRelatedCause(Throwable cause) {
		if (this.relatedCauses == null) {
			// 元素不可能太多，且会频繁增加元素，用LinkedList
			this.relatedCauses = new LinkedList<Throwable>();
		}
		this.relatedCauses.add(cause);
	}

	public Throwable[] getRelatedCauses() {
		if (this.relatedCauses == null) {
			return null;
		}
		return this.relatedCauses.toArray(new Throwable[this.relatedCauses.size()]);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		if (this.relatedCauses != null) {
			for (Throwable cause : this.relatedCauses) {
				sb.append("¥n相关原因:");
				sb.append(cause);
			}
		}
		return sb.toString();
	}

	@Override
	public void printStackTrace(PrintStream ps) {
		synchronized (ps) {
			super.printStackTrace(ps);
			if (this.relatedCauses != null) {
				for (Throwable cause : this.relatedCauses) {
					ps.println("相关原因:");
					cause.printStackTrace(ps);
				}
			}
		}
	}

	@Override
	public void printStackTrace(PrintWriter pw) {
		synchronized (pw) {
			super.printStackTrace(pw);
			if (this.relatedCauses != null) {
				for (Throwable cause : this.relatedCauses) {
					pw.println("相关原因:");
					cause.printStackTrace(pw);
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean contains(Class exClass) {
		if (super.contains(exClass)) {
			return true;
		}
		if (this.relatedCauses != null) {
			for (Throwable cause : this.relatedCauses) {
				if (cause instanceof NestedRuntimeException && ((NestedRuntimeException) cause).contains(exClass)) {
					return true;
				}
			}
		}
		return false;
	}
}
