package com.xhc.model;

/**
 * �����������豸�Զ����һ��ֵ�򣬰���ֵ������ƣ�ֵ��ĵ�λ��ֵ��ı���С����λ��
 * @author Fan
 *
 */
public class Value_Config
{
	
	private String value_name;     //����
	private String unit;           //��λ
	private int decimal;           //����С����λ��
	
	
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