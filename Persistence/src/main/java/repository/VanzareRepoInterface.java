package repository;

import domain.Vanzare;

public interface VanzareRepoInterface extends RepositoryInterface<Long, Vanzare>{
    Iterable<Vanzare> getBySpectacolId(Long id);
}
