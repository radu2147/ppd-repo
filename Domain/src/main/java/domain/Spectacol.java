package domain;


import java.sql.Date;

public class Spectacol extends Entity<Long>{
    private Date date;
    private String name;
    private Long priceVanzare;
    private Long sold;
    private Sala sala;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSold() {
        return sold;
    }

    public void setSold(Long sold) {
        this.sold = sold;
    }

    public Spectacol(Long id, Date date, String name, Long sold, Long priceVanzare, Sala sala) {
        super(id);
        this.date = date;
        this.name = name;
        this.sold = sold;
        this.priceVanzare = priceVanzare;
        this.sala = sala;
    }

    @Override
    public String toString() {
        return "Festival{" +
                "date=" + date +
                ", name='" + name + '\'' +
                ", seats=" + sold +
                '}';
    }

    public Long getPriceVanzare() {
        return priceVanzare;
    }

    public void setPriceVanzare(Long priceVanzare) {
        this.priceVanzare = priceVanzare;
    }

    public Sala getSala() {
        return sala;
    }
}
