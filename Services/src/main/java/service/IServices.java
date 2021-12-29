package service;

//TODO User=username, password, name + delete customer
import java.sql.Date;
import java.util.List;

public interface IServices {
    void addVanzare(Integer festivalID, Date date, List<Integer> seats) throws ServiceException;
}
