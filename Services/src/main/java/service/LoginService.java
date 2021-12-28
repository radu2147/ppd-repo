package service;

import domain.Account;
import repository.AccountRepo;

public class LoginService {
    private AccountRepo accountRepo;

    public LoginService(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }
    private boolean checkPassword(Account account,String password){
        if(!account.getPassword().equals(password))
            return false;
        return true;
    }
    public Account getAccount(String username,String password) throws BadCredentialsException {
        Account account=accountRepo.getOne(username);
        if(account==null || checkPassword(account,password)==false){
            throw new BadCredentialsException("Username sau parola incorecte!");
        }
        return account;
    }
}
