package com.xhc.model;

/**
 * 该类描述了设备自定义的一个值域，包括值域的名称，值域的单位，值域的保留小数点位数
 * @author Fan
 *
 */
public class Value_Config
{
	
	private String value_name;     //名称
	private String unit;           //单位
	private int decimal;           //保留小数点位数
	
	
	public Value_Config(String value_name, String unit, int decimal) {
		super();
		this.value_name = value_name;
		this.unit = unit;
		this.decimal = decimal;
	}
	public String getValue_name() {
		return value_name;
	}
	public void setValue_name(String value_name) {
		this.value_name = value_name;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public int getDecimal() {
		return decimal;
	}
	public void setDecimal(int decimal) {
		this.decimal = decimal;
	}
	
}