package com.example.demo.controller;

import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    @GetMapping("/employees")
    public ResponseEntity getAllEmployees() {
        return ResponseEntity.status(HttpStatus.OK).body(employeeRepository.findAll());
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity getEmployee(@PathVariable Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);

        if (optionalEmployee.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.status(HttpStatus.OK).body(employeeRepository.findById(id).get());
    }

    @PostMapping("/employees")
    public ResponseEntity createEmployee(@RequestBody @Validated Employee employee, BindingResult result) {
        return ResponseEntity.status(HttpStatus.OK).body(employeeRepository.save(employee));
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity updateEmployee(@PathVariable Long id, @RequestBody Employee newEmployee) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);

        if (optionalEmployee.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.status(HttpStatus.OK).body(optionalEmployee.map(employee -> {
            employee.setFirstName(newEmployee.getFirstName());
            employee.setLastName(newEmployee.getLastName());
            employee.setEmailId(newEmployee.getEmailId());
            return employeeRepository.save(employee);
        }));
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity deleteEmployee(@PathVariable Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);

        if (optionalEmployee.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        employeeRepository.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body("deleted!");
    }
}
