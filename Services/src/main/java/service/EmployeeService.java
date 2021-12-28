package service;

import domain.VanzareLocuri;
import repository.EmployeeRepoInterface;

public class EmployeeService {
    private EmployeeRepoInterface employeeRepo;

    public EmployeeService(EmployeeRepoInterface employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    public VanzareLocuri getAgent(Long id){
        return employeeRepo.getOne(id);
    }
}
