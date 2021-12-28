package domain;

public class Sala extends Entity<Long>{

    private int seats;

    public Sala(Long id, int seats) {
        super(id);
        this.seats = seats;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }
}
