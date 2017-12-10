package com.yansheng.beans.factory.parsing;

import java.util.Stack;

public class ParseState {

	// 反斜杠的输入方法：按下option+¥键
	private static final char TAB = '\t';

	@SuppressWarnings("rawtypes")
	private final Stack state;

	@SuppressWarnings("rawtypes")
	public ParseState() {
		this.state = new Stack();
	}

	@SuppressWarnings("rawtypes")
	public ParseState(ParseState other) {
		this.state = (Stack) other.state.clone();
	}

	@SuppressWarnings("unchecked")
	public void push(Entry entry) {
		this.state.push(entry);
	}

	public void pop() {
		// 删除栈顶元素
		this.state.pop();
	}

	public Entry peek() {
		if (this.state.isEmpty()) {
			return null;
		}
		return (Entry) this.state.peek();
	}

	public ParseState snapshot() {
		return new ParseState(this);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int x = 0; x < this.state.size(); x++) {
			if (x > 0) {
				sb.append('\n');
				for (int y = 0; y < x; y++) {
					sb.append(TAB);
				}
				sb.append("->");
			}
			sb.append(this.state.get(x));
		}
		return sb.toString();
	}

	public interface Entry {

	}
}
