package domain;

public class Employee extends Entity<Long>{
    private String name;
    public Employee(Long aLong,String name) {
        super(aLong);
        this.name=name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

