package service;

import domain.Artist;
import domain.validators.ArtistValidator;
import domain.validators.ValidationException;
import domain.validators.Validator;
import repository.ArtistRepo;

public class ArtistService {
    private ArtistRepo artistRepo;
    private Validator<Artist> validator=new ArtistValidator();
    public ArtistService(ArtistRepo artistRepo) {
        this.artistRepo = artistRepo;
    }

    public Artist addArtist(String name,String genre) throws ValidationException {
        Artist artist=new Artist(0l,name,genre);
        validator.validate(artist);
        return artistRepo.add(artist);
    }
}
