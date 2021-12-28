package service;

import domain.Employee;
import repository.EmployeeRepoInterface;

public class EmployeeService {
    private EmployeeRepoInterface employeeRepo;

    public EmployeeService(EmployeeRepoInterface employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    public Employee getAgent(Long id){
        return employeeRepo.getOne(id);
    }
}
