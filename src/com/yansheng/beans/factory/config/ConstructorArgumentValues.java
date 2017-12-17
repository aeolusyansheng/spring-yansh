package com.yansheng.beans.factory.config;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

import com.yansheng.beans.BeanMetadataElement;
import com.yansheng.beans.Mergeable;

public class ConstructorArgumentValues {

	private final Map<Integer, ValueHolder> indexedArgumentsValues = new LinkedHashMap<Integer, ValueHolder>(0);
	private final List<ValueHolder> genericArgumentsValues = new LinkedList<ValueHolder>();

	public ConstructorArgumentValues() {

	}

	public ConstructorArgumentValues(ConstructorArgumentValues original) {
		addArgumentValues(original);
	}

	public void addIndexedArgumentValue(int index, Object value) {
		addIndexedArgumentValue(index, new ValueHolder(value));
	}

	public void addIndexedArgumentValue(int index, Object value, String type) {
		addIndexedArgumentValue(index, new ValueHolder(value, type));
	}

	public void addIndexedArgumentValue(int index, ValueHolder newValue) {
		Assert.isTrue(index >= 0, "index不能为负数。");
		Assert.notNull(newValue, "ValueHolder不能为null。");
		addOrMergeIndexedArgumentValue(index, newValue);
	}

	public void addArgumentValues(ConstructorArgumentValues other) {
		if (other != null) {
			// 将other中的内容拷贝到当前类中,不改变other
			for (Map.Entry<Integer, ValueHolder> entry : other.indexedArgumentsValues.entrySet()) {
				addOrMergeIndexedArgumentValue(entry.getKey(), entry.getValue().copy());
			}
			for (ValueHolder valueHolder : other.genericArgumentsValues) {
				if (!this.genericArgumentsValues.contains(valueHolder)) {
					addOrMergeGenericArgumentValue(valueHolder.copy());
				}
			}
		}
	}

	private void addOrMergeIndexedArgumentValue(Integer key, ValueHolder newValue) {
		ValueHolder currentValue = this.indexedArgumentsValues.get(key);
		if (currentValue != null && newValue.getValue() instanceof Mergeable) {
			Mergeable mergeable = (Mergeable) newValue.getValue();
			if (mergeable.isMergeEnable()) {
				newValue.setValue(mergeable.merge(currentValue.getValue()));
			}
		}
		this.indexedArgumentsValues.put(key, newValue);
	}

	public boolean hasIndexArgumentValue(int index) {
		return this.indexedArgumentsValues.containsKey(index);
	}

	@SuppressWarnings("rawtypes")
	public ValueHolder getIndexedArgumentValue(int index, Class requiredType, String requiredName) {
		Assert.isTrue(index >= 0, "index不能为负数。");
		ValueHolder valueHolder = this.indexedArgumentsValues.get(index);
		if (valueHolder != null
				&& (valueHolder.getType() == null
						|| requiredType != null && ClassUtils.matchesTypeName(requiredType, valueHolder.getType()))
				&& (valueHolder.getName() == null
						|| requiredName != null && requiredName.equals(valueHolder.getName()))) {
			return valueHolder;
		}
		return null;
	}

	public Map<Integer, ValueHolder> getIndexedArgumentValues() {
		return Collections.unmodifiableMap(this.indexedArgumentsValues);
	}

	public void addGenericArgumentsValue(Object value) {
		this.genericArgumentsValues.add(new ValueHolder(value));
	}

	public void addGenericArgumentsValue(Object value, String type) {
		this.genericArgumentsValues.add(new ValueHolder(value, type));
	}

	public void addGenericArgumentsValue(ValueHolder newValue) {
		Assert.notNull(newValue, "ValueHolder 不能为null。");
		if (!this.genericArgumentsValues.contains(newValue)) {
			addOrMergeGenericArgumentValue(newValue);
		}
	}

	private void addOrMergeGenericArgumentValue(ValueHolder newValue) {
		if (newValue.getName() != null) {
			for (Iterator<ValueHolder> it = this.genericArgumentsValues.iterator(); it.hasNext();) {
				ValueHolder currentValue = (ValueHolder) it.next();
				if (newValue.getName().equals(currentValue.getName())) {
					if (newValue.getValue() instanceof Mergeable) {
						Mergeable mergeable = (Mergeable) newValue.getValue();
						if (mergeable.isMergeEnable()) {
							newValue.setValue(mergeable.merge(currentValue.getValue()));
						}
					}
					it.remove();
				}
			}
		}
		this.genericArgumentsValues.add(newValue);
	}

	@SuppressWarnings("rawtypes")
	public ValueHolder getGenericArgumentValue(Class requiredType) {
		return getGenericArgumentValue(requiredType, null, null);
	}

	@SuppressWarnings("rawtypes")
	public ValueHolder getGenericArgumentValue(Class requiredType, String requiredName) {
		return getGenericArgumentValue(requiredType, requiredName, null);
	}

	@SuppressWarnings("rawtypes")
	public ValueHolder getGenericArgumentValue(Class requiredType, String requiredName,
			Set<ValueHolder> usedValueHolders) {

		for (ValueHolder valueHolder : this.genericArgumentsValues) {
			if (usedValueHolders != null && usedValueHolders.contains(valueHolder)) {
				continue;
			}
			if (valueHolder.getName() != null
					&& (requiredName == null || !valueHolder.getName().equals(requiredName))) {
				continue;
			}
			if (valueHolder.getType() != null
					&& (requiredType == null && !ClassUtils.matchesTypeName(requiredType, valueHolder.getType()))) {
				continue;
			}
			if (requiredType != null && valueHolder.getType() == null && valueHolder.getName() == null
					&& !ClassUtils.isAssignableValue(requiredType, valueHolder.getValue())) {
				continue;
			}
			return valueHolder;
		}
		return null;
	}

	public List<ValueHolder> getGenericArgumentValues() {
		return Collections.unmodifiableList(this.genericArgumentsValues);
	}

	@SuppressWarnings("rawtypes")
	public ValueHolder getArgumentValue(int index, Class requiredType, String requiredName,
			Set<ValueHolder> usedValueHolders) {
		Assert.isTrue(index >= 0, "index不能为负数。");
		// 先从有参数索引里找，找不到就在无参数索引里找
		ValueHolder valueHolder = getIndexedArgumentValue(index, requiredType, requiredName);
		if (valueHolder == null) {
			valueHolder = getGenericArgumentValue(requiredType, requiredName, usedValueHolders);
		}
		return valueHolder;
	}

	public int getArgumentCount() {
		return (this.indexedArgumentsValues.size() + this.genericArgumentsValues.size());
	}

	public boolean isEmpty() {
		return (this.indexedArgumentsValues.isEmpty() && this.genericArgumentsValues.isEmpty());
	}

	public void clear() {
		this.indexedArgumentsValues.clear();
		this.genericArgumentsValues.clear();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ConstructorArgumentValues)) {
			return false;
		}
		ConstructorArgumentValues another = (ConstructorArgumentValues) other;
		// 比较个数
		if (another.indexedArgumentsValues.size() != this.indexedArgumentsValues.size()
				|| another.genericArgumentsValues.size() != this.indexedArgumentsValues.size()) {
			return false;
		}
		// 比较无索引参数
		Iterator<ValueHolder> it1 = this.genericArgumentsValues.iterator();
		Iterator<ValueHolder> it2 = another.genericArgumentsValues.iterator();
		while (it1.hasNext() && it2.hasNext()) {
			ValueHolder vh1 = (ValueHolder) it1.next();
			ValueHolder vh2 = (ValueHolder) it2.next();
			if (!vh1.contentEquals(vh2)) {
				return false;
			}
		}
		// 比较有索引参数
		for (Map.Entry<Integer, ValueHolder> entry : this.indexedArgumentsValues.entrySet()) {
			ValueHolder vh1 = entry.getValue();
			ValueHolder vh2 = another.indexedArgumentsValues.get(entry.getKey());
			if (!vh1.contentEquals(vh2)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hashCode = 7;
		for (ValueHolder valueHolder : this.genericArgumentsValues) {
			hashCode = 31 * hashCode + valueHolder.contentHashCode();
		}
		hashCode = 29 * hashCode;
		for (Map.Entry<Integer, ValueHolder> entry : this.indexedArgumentsValues.entrySet()) {
			hashCode = 31 * hashCode + (entry.getValue().contentHashCode() ^ entry.getKey().hashCode());
		}
		return hashCode;
	}

	public static class ValueHolder implements BeanMetadataElement {

		private Object value;
		private String type;
		private String name;
		private Object source;
		private boolean converted = false;
		private Object conertedValue;

		public ValueHolder(Object value) {
			this.value = value;
		}

		public ValueHolder(Object value, String type) {
			this.value = value;
			this.type = type;
		}

		public ValueHolder(Object value, String type, String name) {
			this.value = value;
			this.type = type;
			this.name = name;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setSource(Object source) {
			this.source = source;
		}

		@Override
		public Object getSource() {
			return this.source;
		}

		/*
		 * 返回该Holder里的value是否已经被转换
		 */
		public synchronized boolean isConverted() {
			return this.converted;
		}

		public synchronized void setConvertedValue(Object value) {
			this.conertedValue = value;
			this.converted = true;
		}

		public synchronized Object getConvertedValue() {
			return this.conertedValue;
		}

		private boolean contentEquals(ValueHolder other) {
			return ((this == other) || (ObjectUtils.nullSafeEquals(this.value, other.value)
					&& ObjectUtils.nullSafeEquals(this.type, other.type)));
		}

		private int contentHashCode() {
			return ObjectUtils.nullSafeHashCode(this.value) * 29 + ObjectUtils.nullSafeHashCode(this.type);
		}

		public ValueHolder copy() {
			ValueHolder copy = new ValueHolder(this.value, this.type, this.name);
			copy.setSource(this.source);
			return copy;
		}

	}

}
