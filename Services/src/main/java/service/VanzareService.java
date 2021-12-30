package service;

import domain.Vanzare;
import domain.VanzareLocuri;
import domain.validators.ValidationException;
import domain.validators.Validator;
import domain.validators.VanzareValidator;
import repository.FestivalRepo;
import repository.VanzareLocuriRepo;
import repository.VanzareRepo;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class VanzareService {
    private final VanzareRepo VanzareRepo;
    private final VanzareLocuriRepo VanzareLocuriRepo;
    private final FestivalRepo festivalRepo;
    private final Validator<Vanzare> validator=new VanzareValidator();
    public VanzareService(VanzareRepo VanzareRepo, FestivalRepo festivalRepo, VanzareLocuriRepo VanzareLocuriRepo) {
        this.VanzareRepo = VanzareRepo;
        this.festivalRepo = festivalRepo;
        this.VanzareLocuriRepo = VanzareLocuriRepo;
    }

    public Vanzare addVanzare(Long festival_id, Date date, List<Integer> seats) throws ValidationException {
        var spectacol = festivalRepo.getOne(festival_id);
        Vanzare vanzare = new Vanzare(0L,spectacol , date);
        validator.validate(vanzare);
        vanzare.setSuma((int) (seats.size() * vanzare.getFestival().getPriceVanzare()));
        var saved = VanzareRepo.add(vanzare);
        seats.stream()
                .map(x -> new VanzareLocuri(0L, saved, x))
                .forEach(it -> VanzareLocuriRepo.add(it));
        spectacol.setSold(spectacol.getSold() + saved.getSuma());
        festivalRepo.update(spectacol);
        return saved;
    }

    public boolean check() {
        return true;
    }
}
