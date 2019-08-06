package com.javainuse.model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.jpa.domain.Specification;

import com.javainuse.specification.SpecificationImpl;

public class SearchCriteria {
	private String key;
	private String operation;
	private Object value;

	public SearchCriteria(String key, String operation, Object value) {
		this.key = key;
		this.operation = operation;
		this.value = value;
	}

	public SearchCriteria() {
		// TODO Auto-generated constructor stub
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Specification<Employee> getSpecificationDetails(SpecificationImpl<Employee> spec, JSONArray serarchJsonArr) {
		for (Object serarchJson : serarchJsonArr) {
			JSONObject searchJsonData = new JSONObject(String.valueOf(serarchJson));
			spec.with(String.valueOf(searchJsonData.get("key")), ":", String.valueOf(searchJsonData.get("value")));
		}

		Specification<Employee> empSpec = spec.build();
		return empSpec;
	}
}
