package com.javainuse.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;

import com.javainuse.model.Employee;
import com.javainuse.model.SearchCriteria;

public class SpecificationsBuilder {
	private List<SearchCriteria> params;

	public SpecificationsBuilder() {
		params = new ArrayList<SearchCriteria>();
	}

	public SpecificationsBuilder with(String key, String operation, Object value) {
		params.add(new SearchCriteria(key, operation, value));
		return this;
	}

	public <T> Specification<T> build() {
		if (params.isEmpty()) {
			return null;
		}

		List<Specification> specs = params.stream().map(EmployeeSpecification::new).collect(Collectors.toList());

		Specification result = specs.get(0);

		for (int i = 1; i < params.size(); i++) {
			//result = params.get(i).isOrPredicate() ? Specification.where(result).or(specs.get(i))
//			result = params.get(i).isOrPredicate() ? Specification.where(result).or(specs.get(i)): Specification.where(result).and(specs.get(i));
			result = Specification.where(result).and(specs.get(i));
		}
		return result;
	}
}
