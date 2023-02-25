package com.catalog.prjCatalog.resources.exceptions;

import java.io.Serializable;

public class FieldMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String fieldName;
	private String messge;
	
	public FieldMessage() { 
	}

	public FieldMessage(String fieldName, String messge) {
		super();
		this.fieldName = fieldName;
		this.messge = messge;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getMessge() {
		return messge;
	}

	public void setMessge(String messge) {
		this.messge = messge;
	}
}
