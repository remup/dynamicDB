package com.example.remya.dynamicDBOne;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value="transactionManager")
public interface EmployeeRepo extends CrudRepository<Employee, Integer>{

}
