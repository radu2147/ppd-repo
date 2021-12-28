package domain;

import java.io.Serializable;
import java.sql.Date;

public class VanzareDTO implements Serializable {
    private Integer festivalID;
    private Date date;

    public VanzareDTO(Integer festivalID, Date date) {
        this.festivalID = festivalID;
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
}
