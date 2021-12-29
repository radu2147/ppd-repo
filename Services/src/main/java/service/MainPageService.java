package service;

import domain.Spectacol;
import domain.SpetacolDTO;
import domain.Vanzare;
import domain.validators.ValidationException;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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

    public Vanzare sellVanzare(Long festivalID, Date seats, List<Integer> s) throws ValidationException {
        return VanzareService.addVanzare(festivalID, seats, s);
    }
}
