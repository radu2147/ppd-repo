package domain;

import java.io.Serializable;
import java.sql.Date;

public class SpetacolDTO implements Serializable {
    private Long festivalID;
    private String name;
    private Date date;
    private Long sold;
    private Long priceVanzare;

    public SpetacolDTO(Long festivalID, String name, Date date, Long seats, Long soldSeats) {
        this.festivalID=festivalID;
        this.name = name;
        this.date = date;

        this.sold = seats;
        this.priceVanzare = soldSeats;
    }

    public Long getFestivalID() {
        return festivalID;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }



    public Long getSold() {
        return sold;
    }

    public Long getPriceVanzare() {
        return priceVanzare;
    }

    public void setPriceVanzare(Long seats){
        this.priceVanzare = seats;
    }
    @Override
    public String toString() {
        return "Artist: "+name+" date: "+date+ "total seats: "+ sold;
//        return "FestivalDTO{" +
//                "festivalID=" + festivalID +
//                ", name='" + name + '\'' +
//                ", date=" + date +
//                ", location='" + location + '\'' +
//                ", seats=" + seats +
//                ", soldSeats=" + soldSeats +
//                '}';
    }
}
