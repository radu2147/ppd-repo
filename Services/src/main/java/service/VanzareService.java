package service;

import domain.Vanzare;
import domain.validators.VanzareValidator;
import domain.validators.ValidationException;
import domain.validators.Validator;
import repository.FestivalRepo;
import repository.VanzareRepo;

import java.sql.Date;

public class VanzareService {
    private final VanzareRepo VanzareRepo;
    private final FestivalRepo festivalRepo;
    private final Validator<Vanzare> validator=new VanzareValidator();
    public VanzareService(VanzareRepo VanzareRepo, FestivalRepo festivalRepo) {
        this.VanzareRepo = VanzareRepo;
        this.festivalRepo = festivalRepo;
    }

    public Vanzare addVanzare(Long festival_id, Date date) throws ValidationException {
        Vanzare vanzare =new Vanzare(0L, festivalRepo.getOne(festival_id), date);
        validator.validate(vanzare);
        return VanzareRepo.add(vanzare);
    }

    public Long getSoldSeats(Long festival_id) {
        return VanzareRepo.getSoldSeats(festival_id);
    }
}
