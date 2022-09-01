package com.example.remya.dynamicDBOne;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DynamicDBOneController {
	@Autowired
	EmployeeRepo repo;
	@GetMapping("/test")
	public String test() {
		return "MultitenancySchema";
	}
	
	@GetMapping("/getAllEmployee")
	public List<Employee> getAllEmployee() {
		
		return (List<Employee>) repo.findAll();
	}
	
	@GetMapping("/getAllEmployee/{tenant}")
	public List<Employee> getAllEmployeeRemya(@PathVariable("tenant") String schemaName ) {
		CurrentTenantName.setCurrentTenant(schemaName);
		System.out.println("tenent in controller "+CurrentTenantName.getCurrentTenant());
		return (List<Employee>) repo.findAll();
	}

}
