package com.javainuse.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.javainuse.model.Employee;
import com.javainuse.model.SearchCriteria;

public class EmployeeSpecification<T> implements Specification<T> {

	private SearchCriteria criteria;
	private List<SearchCriteria> params = new ArrayList<SearchCriteria>();
	
	public EmployeeSpecification with(String key, String operation, Object value) {
		params.add(new SearchCriteria(key, operation, value));
		return this;
	}
	
	public EmployeeSpecification() {
	}

	public EmployeeSpecification(SearchCriteria searchCriteria) {
		this.criteria = searchCriteria;
	}

	
	public <T> Specification<T> build() {
		if (params.isEmpty()) {
			return null;
		}

		List<Specification> specs = params.stream().map(EmployeeSpecification::new).collect(Collectors.toList());

		Specification result = specs.get(0);

		for (int i = 1; i < params.size(); i++) {
			//result = params.get(i).isOrPredicate() ? Specification.where(result).or(specs.get(i))
			result = Specification.where(result).and(specs.get(i));
		}
		return result;
	}
	
	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		if (criteria.getOperation().equalsIgnoreCase(":")) {
			if (root.get(criteria.getKey()).getJavaType() == String.class) {
				return builder.like(root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
			} else {
				return builder.equal(root.get(criteria.getKey()), criteria.getValue());
			}
		}
		return null;
	}

	
}