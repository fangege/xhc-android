package com.xhc.model;

/**
 * 描述一个类型，包含类型的名称
 * @author Fan
 *
 */
public class Type {
	private String typename;
	private int mark;
	public Type(String typename, int mark) {
		super();
		this.typename = typename;
		this.mark = mark;
	}
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	public int getMark() {
		return mark;
	}
	public void setMark(int mark) {
		this.mark = mark;
	}
}
