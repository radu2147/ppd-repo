package repository;

import domain.Vanzare;

public interface VanzareRepoInterface extends RepositoryInterface<Long, Vanzare>{
    Long getSoldSeats(Long festival_id);
}
