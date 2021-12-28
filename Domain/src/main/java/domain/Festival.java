package domain;


import java.sql.Date;

public class Festival extends Entity<Long>{
    private Date date;
    private String location;
    private String name;
    private String genre;
    private Long seats;
    private Artist artist;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Long getSeats() {
        return seats;
    }

    public void setSeats(Long seats) {
        this.seats = seats;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Festival(Long id, Date date, String location, String name, String genre, Long seats, Artist artist) {
        super(id);
        this.date = date;
        this.location = location;
        this.name = name;
        this.genre = genre;
        this.seats=seats;
        this.artist = artist;
    }

    @Override
    public String toString() {
        return "Festival{" +
                "date=" + date +
                ", location='" + location + '\'' +
                ", name='" + name + '\'' +
                ", genre='" + genre + '\'' +
                ", seats=" + seats +
                '}';
    }
}
