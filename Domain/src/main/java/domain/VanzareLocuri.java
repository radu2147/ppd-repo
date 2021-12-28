package domain;

public class VanzareLocuri extends Entity<Long>{
    private Vanzare vanzare;
    private int loc;
    public VanzareLocuri(Long aLong, Vanzare name, int loc) {
        super(aLong);
        this.vanzare =name;

        this.loc = loc;
    }

    public Vanzare getVanzare() {
        return vanzare;
    }

    public int getLoc() {
        return loc;
    }
}

