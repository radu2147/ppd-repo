package service;

import domain.Customer;
import domain.validators.CustomerValidator;
import domain.validators.Validator;
import repository.CustomerRepo;

public class CustomerService {
    private CustomerRepo customerRepo;
    private Validator<Customer> validator=new CustomerValidator();
    public CustomerService(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    public Customer getCustomer(Long idUser) {
        return customerRepo.getOne(idUser);
    }
}
