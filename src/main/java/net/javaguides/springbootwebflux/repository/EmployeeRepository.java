package net.javaguides.springbootwebflux.repository;

import com.mongodb.reactivestreams.client.MongoCollection;
import net.javaguides.springbootwebflux.entity.Employee;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface EmployeeRepository extends ReactiveCrudRepository<Employee,String>{
}
