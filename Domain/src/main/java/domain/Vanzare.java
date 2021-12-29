package domain;

import java.io.Serializable;
import java.sql.Date;

public class Vanzare extends Entity<Long> implements Serializable {
    //TODO pune doar id-uri in fieldurile din clase
    private Spectacol spectacol;
    private Date date;
    private Integer suma;

    public String getNumeClient() {
        return numeClient;
    }

    public void setNumeClient(String numeClient) {
        this.numeClient = numeClient;
    }

    private String numeClient;

    public Vanzare(Long id, Spectacol spectacol, Date date) {
        super(id);
        this.date = date;
        this.spectacol = spectacol;
        suma = 0;
    }

    public Spectacol getFestival() {
        return spectacol;
    }

    public Date getDate() {
        return date;
    }

    public Integer getSuma() {
        return suma;
    }

    @Override
    public String toString() {
        return "Vanzare{" +
                "festival=" + spectacol.getName() +
                ", seats=" + suma +
                '}';
    }
}
