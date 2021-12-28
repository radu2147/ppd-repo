package service;

import domain.Account;
import domain.SpetacolDTO;
//TODO User=username, password, name + delete customer
import java.sql.Date;

public interface IServices {
    Account login(Account user,IObserver client) throws ServiceException, BadCredentialsException;
    Iterable<SpetacolDTO> searchByDate(Date date) throws ServiceException;
    void sellVanzare(Integer festivalID, Date date) throws ServiceException;
    void logout(Account user, IObserver client) throws ServiceException;
}
