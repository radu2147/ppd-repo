package service;

import domain.Account;
import domain.SpetacolDTO;

import java.sql.Date;

public class Services implements IServices {
    MainPageService mainPageService;

    public Services(MainPageService mainPageService) {
        this.mainPageService = mainPageService;
    }

    @Override
    public Account login(Account account, IObserver client) {
        return null;
    }

    @Override
    public Iterable<SpetacolDTO> searchByDate(Date date) {
        return null;
    }

    @Override
    public void sellVanzare(Integer festivalID, Date date) {

    }

    @Override
    public void logout(Account user, IObserver client) {
    }
}
