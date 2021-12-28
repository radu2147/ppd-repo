package domain;

import java.io.Serializable;

public class TicketDTO implements Serializable {
    private Integer festivalID;
    private Long seats;
    private String client;

    public TicketDTO(Integer festivalID, Long seats, String client) {
        this.festivalID = festivalID;
        this.seats = seats;
        this.client = client;
    }

    public Integer getFestivalID() {
        return festivalID;
    }

    public void setFestivalID(Integer festivalID) {
        this.festivalID = festivalID;
    }

    public Long getSeats() {
        return seats;
    }

    public void setSeats(Long seats) {
        this.seats = seats;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "domain.TicketDTO{" +
                "festivalID=" + festivalID +
                ", seats=" + seats +
                ", client='" + client + '\'' +
                '}';
    }
}
