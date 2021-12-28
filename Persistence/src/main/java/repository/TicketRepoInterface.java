package repository;

import domain.Ticket;

public interface TicketRepoInterface extends RepositoryInterface<Long, Ticket>{
    Long getSoldSeats(Long festival_id);
}
