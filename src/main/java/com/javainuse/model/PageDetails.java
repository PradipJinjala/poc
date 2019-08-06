package com.javainuse.model;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.util.StringUtils;

public class PageDetails {

	private int pageIndex;
	private int pageElement;
	private String sortByColumn;
	private String sortOrder;

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageElement() {
		return pageElement;
	}

	public void setPageElement(int pageElement) {
		this.pageElement = pageElement;
	}

	public String getSortByColumn() {
		return sortByColumn;
	}

	public void setSortByColumn(String sortByColumn) {
		this.sortByColumn = sortByColumn;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public PageRequest getPageDetails(Map<String, String> seachData, JSONObject jsonData) {
		PageRequest pageable;
		int page = Integer.parseInt(String.valueOf(jsonData.get("page")));
		int size = Integer.parseInt(String.valueOf(jsonData.get("size")));
		String sortOrder = seachData.get("sortOrder");
		String sortBy = String.valueOf(jsonData.get("sortBy"));
		
		if (StringUtils.hasLength(seachData.get("sortOrder")) && StringUtils.hasLength(seachData.get("sortBy"))) {
			if (sortOrder.equalsIgnoreCase("ASC")) {
				pageable = PageRequest.of(page, size, Direction.ASC, (sortBy));
			} else {
				pageable = PageRequest.of(page, size, Direction.DESC, sortBy);
			}
		} else {
			pageable = PageRequest.of(page, size);
		}
		return pageable;
	}
}
