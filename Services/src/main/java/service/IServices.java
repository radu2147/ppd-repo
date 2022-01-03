package service;

import java.sql.Date;
import java.util.List;

public interface IServices {
    void addVanzare(Integer festivalID, Date date, List<Integer> seats) throws VanzareException, ServiceException;
    void verificari();
}
