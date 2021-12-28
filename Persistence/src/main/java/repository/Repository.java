package repository;

import domain.Entity;

import java.sql.ResultSet;

public abstract class Repository<ID,E extends Entity<ID>> implements RepositoryInterface<ID,E>{


    @Override
    public E getOne(ID id) {
        return null;
    }

    @Override
    public Iterable<E> getAll() {
        return null;
    }

    protected abstract E extractEntity(ResultSet resultSet);

    protected abstract String getEntityAsString(E entity);

    @Override
    public E add(E entity) {
        return null;
    }

    @Override
    public E delete(ID id) {
        return null;
    }

    @Override
    public E update(E entity) {
        return null;
    }
}
