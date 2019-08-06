package com.javainuse.controllers;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javainuse.data.EmployeeRepository;
import com.javainuse.model.Employee;
import com.javainuse.model.PageDetails;
import com.javainuse.model.SearchCriteria;
import com.javainuse.specification.EmployeeSpecification;
import com.javainuse.specification.SpecificationImpl;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class EmployeeController {

	@Autowired
	private EmployeeRepository employeeData;

	@RequestMapping(value = "/listPageable", method = RequestMethod.POST)
	public Page<Employee> employeesPageable(@RequestBody Map<String, String> seachData) {

		EmployeeSpecification spec1 = null;
		EmployeeSpecification spec2 = null;
		PageRequest pageable = null;

		JSONObject jsonData = new JSONObject(seachData);
		int page = Integer.parseInt(String.valueOf(jsonData.get("page")));
		int size = Integer.parseInt(String.valueOf(jsonData.get("size")));
		String sortOrder = seachData.get("sortOrder");
		String sortBy = String.valueOf(jsonData.get("sortBy"));
		JSONObject serarchJson = new JSONObject(String.valueOf(jsonData.get("filter")));

		// Filter as per data
		if (StringUtils.hasLength(String.valueOf(serarchJson.get("name")))) {
			spec1 = new EmployeeSpecification(new SearchCriteria("name", ":", String.valueOf(serarchJson.get("name"))));
		}

		if (StringUtils.hasLength(String.valueOf(serarchJson.get("dept")))) {
			spec2 = new EmployeeSpecification(new SearchCriteria("dept", ":", String.valueOf(serarchJson.get("dept"))));
		}

		if (StringUtils.hasLength(seachData.get("sortOrder")) && StringUtils.hasLength(seachData.get("sortBy"))) {
			if (sortOrder.equalsIgnoreCase("ASC")) {
				pageable = PageRequest.of(page, size, Direction.ASC, (sortBy));
			} else {
				pageable = PageRequest.of(page, size, Direction.DESC, sortBy);
			}
		} else {
			pageable = PageRequest.of(page, size);
		}
		return employeeData.findAll(Specification.where(spec1).and(spec2), pageable);
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<Employee> getEmployees() {
		System.out.println("list");
		return employeeData.findAll();
	}

	@RequestMapping(value = "/save", method = RequestMethod.GET)
	public void saveEmp() {
		System.out.println("Call save");
		Employee employee = null;
		int cnt = 10;
		for (int i = 0; i < 30; i++) {
			employee = new Employee();
			employee.setName(++cnt + "_Kuldeep");
			employee.setDept(cnt + "_IT Department");
			employeeData.save(employee);

			employee = new Employee();
			employee.setName(++cnt + "_Pradip");
			employee.setDept(cnt + "_IT Department");
			employeeData.save(employee);

			employee = new Employee();
			employee.setName(++cnt + "_Sakil");
			employee.setDept(cnt + "_IT Department");
			employeeData.save(employee);

		}
	}

	@PostMapping("/genericList")
	public Page<Employee> getPageable(@RequestParam Map<String, String> seachData) {
		SpecificationImpl<Employee> spec = new SpecificationImpl<>();
		return getListPageData(seachData, spec);
	}

	private Page<Employee> getListPageData(Map<String, String> seachData, SpecificationImpl<Employee> spec) {
		PageRequest pageable = null;
		PageDetails pageDetails = new PageDetails();
		SearchCriteria searchCriteria = new SearchCriteria();
		JSONObject jsonData = new JSONObject(seachData);
		JSONArray serarchJsonArr = new JSONArray(String.valueOf(jsonData.get("filter")));
	
		/* Get Pagedetails information */
		pageable = pageDetails.getPageDetails(seachData, jsonData);

		/* Get Conditional Data */
		Specification<Employee> empSpec = searchCriteria.getSpecificationDetails(spec, serarchJsonArr);
		
		return employeeData.findAll(empSpec, pageable);
	}

	

}
