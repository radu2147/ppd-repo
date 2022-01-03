package service;

import domain.Vanzare;
import domain.validators.ValidationException;

import java.sql.Date;
import java.util.List;

public class MainPageService {
    private final VanzareService VanzareService;

    public MainPageService(VanzareService VanzareService) {
        this.VanzareService = VanzareService;
    }

    public Vanzare sellVanzare(Long festivalID, Date seats, List<Integer> s) throws ValidationException {
        return VanzareService.addVanzare(festivalID, seats, s);
    }
}
