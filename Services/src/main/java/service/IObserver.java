package service;

import domain.TicketDTO;

public interface IObserver {
    void ticketsSold(TicketDTO ticket) throws ServiceException;
}
