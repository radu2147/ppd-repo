package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomData {
    public Integer getShowId(Integer nbOfShows) {
        Random rand = new Random();

        // Generate random integers in range 0 to nbOfShows
        int showId = rand.nextInt(nbOfShows) + 1;

        return showId;
    }

    public List<Integer> getSeats(Integer totalSeats) {
        Random rand = new Random();

        // Generate random integers in range 0 to totalSeats
        int nbOfSeats = rand.nextInt(totalSeats - 1) + 1;

        List<Integer> seats = new ArrayList<>();

        for (int i = 0; i < nbOfSeats; i++) {
            Integer seat = rand.nextInt(totalSeats);
            seats.add(seat);
        }

        return seats;
    }
}
