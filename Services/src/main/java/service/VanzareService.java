package service;

import domain.Spectacol;
import domain.Vanzare;
import domain.VanzareLocuri;
import domain.validators.ValidationException;
import domain.validators.Validator;
import domain.validators.VanzareValidator;
import repository.FestivalRepo;
import repository.VanzareLocuriRepo;
import repository.VanzareRepo;

import java.sql.Date;
import java.util.ArrayList;
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
        var locuriDisponibile = VanzareLocuriRepo.getNrLocuri();
        var locuriVanzare = seats.size();

        if (locuriVanzare > locuriDisponibile ) {
            throw new ValidationException("Vanzare nereusita! Locuri insuficiente!");
        }

        List<Integer> locuri = VanzareLocuriRepo.getLocuri();
        List<Integer> duplicatesSeats = checkSeats(locuri, seats);

        if (duplicatesSeats.size() > 0) {
            throw new ValidationException("Vanzare nereusita! Locuri duplicate!");
        }

        Spectacol spectacol = festivalRepo.getOne(festival_id);
        Integer suma = Math.toIntExact(spectacol.getPriceVanzare() * seats.size());

        Vanzare vanzare = new Vanzare(0L, spectacol, date);
        vanzare.setSuma(suma);
        validator.validate(vanzare);

        var saved = VanzareRepo.add(vanzare);
        seats.stream()
                .map(x -> new VanzareLocuri(0L, saved, x))
                .forEach(it -> VanzareLocuriRepo.add(it));

        VanzareLocuriRepo.updateLocuri(locuriDisponibile - locuriVanzare);

        spectacol.setSold(spectacol.getSold() + saved.getSuma());
        festivalRepo.update(spectacol);

        return saved;
    }

    private List<Integer> checkSeats(List<Integer> allSeats, List<Integer> vanzareSeats) {
        List<Integer> duplicates = new ArrayList<>();

        for (int i = 0; i < vanzareSeats.size(); i++) {
            if (allSeats.contains(vanzareSeats.get(i))) {
                duplicates.add(vanzareSeats.get(i));
            }
        }

        return duplicates;
    }

    public boolean check() {
        return true;
    }
}
