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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class VanzareService {
    private final VanzareRepo vanzareRepo;
    private final VanzareLocuriRepo vanzareLocuriRepo;
    private final FestivalRepo festivalRepo;
    private final Validator<Vanzare> validator=new VanzareValidator();
    public VanzareService(VanzareRepo vanzareRepo, FestivalRepo festivalRepo, VanzareLocuriRepo vanzareLocuriRepo) {
        this.vanzareRepo = vanzareRepo;
        this.festivalRepo = festivalRepo;
        this.vanzareLocuriRepo = vanzareLocuriRepo;
    }

    public Vanzare addVanzare(Long festival_id, Date date, List<Integer> seats) throws ValidationException {
        var locuriDisponibile = vanzareLocuriRepo.getNrLocuri();
        var locuriVanzare = seats.size();

        if (locuriVanzare > locuriDisponibile ) {
            throw new ValidationException("Vanzare nereusita! Locuri insuficiente!");
        }

        List<Integer> locuri = vanzareLocuriRepo.getLocuri();
        List<Integer> duplicatesSeats = checkSeats(locuri, seats);

        if (duplicatesSeats.size() > 0) {
            throw new ValidationException("Vanzare nereusita! Locuri duplicate!");
        }

        Spectacol spectacol = festivalRepo.getOne(festival_id);
        Integer suma = Math.toIntExact(spectacol.getPriceVanzare() * seats.size());

        Vanzare vanzare = new Vanzare(0L, spectacol, date);
        vanzare.setSuma(suma);
        validator.validate(vanzare);

        var saved = vanzareRepo.add(vanzare);
        seats.stream()
                .map(x -> new VanzareLocuri(0L, saved, x))
                .forEach(it -> vanzareLocuriRepo.add(it));

        vanzareLocuriRepo.updateLocuri(locuriDisponibile - locuriVanzare);

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

    public String checkLocuri() {
        String message = "";

        Integer locuriVandute = vanzareLocuriRepo.getLocuriVandute();

        Integer locuriDisponibile = vanzareLocuriRepo.getNrLocuri();

        if(locuriDisponibile == 100 - locuriVandute) {
            message = "corect";
        }
        else {
            message = "incorect";
        }

        return message;
    }

    public String checkSold() {
        String message = "";

        Integer equals = 0;

        List<Spectacol> totalSpectacole = (List<Spectacol>) festivalRepo.getAll();

        List<Spectacol> soldSpectacole = (List<Spectacol>) vanzareRepo.getFestivalsSold();

        for (Spectacol spectacol:  totalSpectacole) {
            for (Spectacol soldSpectacol: soldSpectacole) {
                if(spectacol.getId().equals(soldSpectacol.getId()) && spectacol.getSold().equals(soldSpectacol.getSold())) {
                    equals ++;
                }
            }
        }

        if(equals.equals(soldSpectacole.size())) {
            message = "corect";
        }
        else {
            message = "incorect";
        }

        return message;
    }

    public void periodicCheck() {

        File fname = new File("C:\\Users\\barza\\source\\repos\\An3-Sem1\\PPD\\P1-ClientServer\\ppd-repo\\verificari.txt");

        try {
            FileWriter writer = new FileWriter(fname, true);

            String messageLocuri = checkLocuri();
            String messageSold = checkSold();

            writer.write(messageLocuri + " " + messageSold + "\n");

            writer.close();
        }
        catch(IOException exception){
            exception.printStackTrace();
        }
    }

    public boolean check() {
        return true;
    }
}
