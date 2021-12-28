package repository;

import domain.Spectacol;

import java.sql.Date;

public interface FestivalRepoInterface extends RepositoryInterface<Long, Spectacol> {
    Iterable<Spectacol> findByDate(Date date);
}
