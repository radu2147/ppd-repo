package service;

import domain.Spectacol;
import domain.validators.FestivalValidator;
import domain.validators.ValidationException;
import domain.validators.Validator;
import repository.ArtistRepo;
import repository.FestivalRepo;

import java.sql.Date;

public class FestivalService {
    private FestivalRepo festivalRepo;
    private ArtistRepo artistRepo;
    private Validator<Spectacol> validator=new FestivalValidator();
    public FestivalService(FestivalRepo festivalRepo, ArtistRepo artistRepo) {
        this.festivalRepo = festivalRepo;
        this.artistRepo = artistRepo;
    }
    public Iterable<Spectacol> getAll() {
        return festivalRepo.getAll();
    }
    public Iterable<Spectacol> getByDate(Date date){
        return festivalRepo.findByDate(date);
    }

}
