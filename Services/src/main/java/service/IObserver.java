package service;

import domain.VanzareDTO;

public interface IObserver {
    void VanzaresSold(VanzareDTO Vanzare) throws ServiceException;
}
