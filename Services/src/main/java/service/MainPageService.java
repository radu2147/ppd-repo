package service;

import domain.Spectacol;
import domain.SpetacolDTO;
import domain.Vanzare;
import domain.validators.ValidationException;

import java.sql.Date;
import java.util.ArrayList;

public class MainPageService {
    private final ArtistService artistService;
    private final FestivalService festivalService;
    private final VanzareService VanzareService;
    public EmployeeService employeeService;

    public MainPageService(ArtistService artistService, FestivalService festivalService, VanzareService VanzareService, EmployeeService employeeService) {
        this.artistService = artistService;
        this.festivalService = festivalService;
        this.VanzareService = VanzareService;
        this.employeeService = employeeService;
    }

    public Iterable<SpetacolDTO> getFestivals() {
        Iterable<Spectacol> festivalList = festivalService.getAll();
        ArrayList<SpetacolDTO> ret = new ArrayList<>();
        festivalList.forEach(x -> {
            ret.add(new SpetacolDTO(x.getId(), x.getName(), x.getDate(), x.getSold(), x.getPriceVanzare()));
        });
        return ret;
    }

    public Iterable<SpetacolDTO> getFestivalsByDate(Date date) {
        Iterable<Spectacol> festivalList = festivalService.getByDate(date);
        ArrayList<SpetacolDTO> ret = new ArrayList<>();
        festivalList.forEach(x -> {
            ret.add(new SpetacolDTO(x.getId(), x.getName(), x.getDate(), x.getSold(), x.getPriceVanzare()));
        });
        return ret;
    }

    public Vanzare sellVanzare(Long festivalID, Date seats) throws ValidationException {
        return VanzareService.addVanzare(festivalID, seats);
    }
}
