package domain;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class VanzareDTO implements Serializable {
    private Integer festivalID;
    private Date date;
    private List<Integer> seats;

    public VanzareDTO(Integer festivalID, Date date) {
        this.festivalID = festivalID;
        this.date = date;
        this.seats = new ArrayList<>();
    }

    public VanzareDTO(Integer festivalID, Date date, List<Integer> seats) {
        this.festivalID = festivalID;
        this.seats = seats;
        this.date = date;
    }

    public Integer getFestivalID() {
        return festivalID;
    }

    public void setFestivalID(Integer festivalID) {
        this.festivalID = festivalID;
    }

    @Override
    public String toString() {
        return "domain.VanzareDTO{" +
                "festivalID=" + festivalID +
                '}';
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Integer> getSeats() {
        return seats;
    }
}
