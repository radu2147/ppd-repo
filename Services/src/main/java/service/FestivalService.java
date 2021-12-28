package service;

import domain.Artist;
import domain.Festival;
import domain.validators.FestivalValidator;
import domain.validators.ValidationException;
import domain.validators.Validator;
import repository.ArtistRepo;
import repository.FestivalRepo;

import java.sql.Date;

public class FestivalService {
    private FestivalRepo festivalRepo;
    private ArtistRepo artistRepo;
    private Validator<Festival> validator=new FestivalValidator();
    public FestivalService(FestivalRepo festivalRepo, ArtistRepo artistRepo) {
        this.festivalRepo = festivalRepo;
        this.artistRepo = artistRepo;
    }
    public Iterable<Festival> getAll() {
        return festivalRepo.getAll();
    }
    public Iterable<Festival> getByDate(Date date){
        return festivalRepo.findByDate(date);
    }

    public Festival addFestival(Date date,String location,String name,String genre,Long seats,Long artist_id) throws ValidationException {
        Artist artist=artistRepo.getOne(artist_id);
        if(artist==null){
            throw new ValidationException("Artistul cu id-ul introdus nu exista!");
        }
        Festival festival=new Festival(0l,date,location,name,genre,seats,artist);
        validator.validate(festival);
        return festivalRepo.add(festival);
    }
}
