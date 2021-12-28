package repository;

import domain.Artist;

public interface ArtistRepoInterface extends RepositoryInterface<Long, Artist>{
    Iterable<Artist> findArtistByGenre(String genre);
}
