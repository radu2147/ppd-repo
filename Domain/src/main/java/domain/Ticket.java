package domain;

import java.io.Serializable;

public class Ticket extends Entity<Long> implements Serializable {
    //TODO pune doar id-uri in fieldurile din clase
    private Festival festival;
    private Double price;
    private String client;
    private Integer seats;

    public String getNumeClient() {
        return numeClient;
    }

    public void setNumeClient(String numeClient) {
        this.numeClient = numeClient;
    }

    private String numeClient;

    public Ticket(Long id, Festival festival, Double price, String client, Integer seats) {
        super(id);
        this.festival = festival;
        this.price = price;
        this.client = client;
        this.seats = seats;
    }

    public Festival getFestival() {
        return festival;
    }

    public void setFestival(Festival festival) {
        this.festival = festival;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "festival=" + festival.getName() +
                ", price=" + price +
                ", client=" + client +
                ", seats=" + seats +
                '}';
    }
}
