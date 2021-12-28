package service;

import domain.Sala;
import domain.validators.ArtistValidator;
import domain.validators.ValidationException;
import domain.validators.Validator;
import repository.ArtistRepo;

public class ArtistService {
    private ArtistRepo artistRepo;
    private Validator<Sala> validator=new ArtistValidator();
    public ArtistService(ArtistRepo artistRepo) {
        this.artistRepo = artistRepo;
    }
}
