package service;

import java.sql.Date;
import java.util.List;

public class Services implements IServices {
    MainPageService mainPageService;

    public Services(MainPageService mainPageService) {
        this.mainPageService = mainPageService;
    }

    @Override
    public void addVanzare(Integer festivalID, Date date, List<Integer> seats) {

    }
}
