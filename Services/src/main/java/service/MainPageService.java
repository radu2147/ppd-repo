package service;

import domain.*;
import domain.validators.ValidationException;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MainPageService {
    private final ArtistService artistService;
    private final FestivalService festivalService;
    private final TicketService ticketService;
    public EmployeeService employeeService;

    public MainPageService(ArtistService artistService, FestivalService festivalService, TicketService ticketService, EmployeeService employeeService) {
        this.artistService = artistService;
        this.festivalService = festivalService;
        this.ticketService = ticketService;
        this.employeeService=employeeService;
    }

    public Iterable<FestivalDTO> getFestivals(){
        Iterable<Festival> festivalList = festivalService.getAll();
        ArrayList<FestivalDTO> ret=new ArrayList<>();
        festivalList.forEach(x->{
            ret.add(new FestivalDTO(x.getId(),x.getArtist().getName(),x.getDate(),x.getLocation(),x.getSeats(),getSoldSeats(x.getId())));
        });
        return ret;
    }

    public Iterable<FestivalDTO> getFestivalsByDate(Date date){
        Iterable<Festival> festivalList = festivalService.getByDate(date);
        ArrayList<FestivalDTO> ret=new ArrayList<>();
        festivalList.forEach(x->{
            ret.add(new FestivalDTO(x.getId(),x.getArtist().getName(),x.getDate(),x.getLocation(),x.getSeats(),getSoldSeats(x.getId())));
        });
        return ret;
    }

    public Long getSoldSeats(Long festival_id){
        return ticketService.getSoldSeats(festival_id);
    }

    public Ticket sellTicket(Long festivalID, Long seats, String client) throws ValidationException {
        Random random=new Random();
        double x=random.nextDouble()%25;
        return ticketService.addTicket(x*seats,client, festivalID, seats.intValue());
    }

    public Festival addFestival(Date date,String location,String name,String genre,Long seats,Long aid) throws ValidationException {
        return festivalService.addFestival(date,location,name,genre,seats,aid);
    }

    public Artist addArtist(String name, String genre) throws ValidationException {
        return artistService.addArtist(name,genre);
    }
}
