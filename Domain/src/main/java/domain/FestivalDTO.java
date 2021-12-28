package domain;

import java.io.Serializable;
import java.sql.Date;

public class FestivalDTO implements Serializable {
    private Long festivalID;
    private String name;
    private Date date;
    private String location;
    private Long seats;
    private Long soldSeats;

    public FestivalDTO(Long festivalID,String name, Date date, String location, Long seats, Long soldSeats) {
        this.festivalID=festivalID;
        this.name = name;
        this.date = date;
        this.location = location;
        this.seats = seats;
        this.soldSeats = soldSeats;
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

    public String getLocation() {
        return location;
    }

    public Long getSeats() {
        return seats;
    }

    public Long getSoldSeats() {
        return soldSeats;
    }

    public void setSoldSeats(Integer seats){
        this.soldSeats+=seats;
    }
    @Override
    public String toString() {
        return "Artist: "+name+" date: "+date+" location: "+location+" total seats: "+seats;
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
