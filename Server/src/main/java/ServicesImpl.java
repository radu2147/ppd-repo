import domain.Account;
import domain.SpetacolDTO;
import domain.VanzareDTO;
import domain.validators.ValidationException;
import service.*;

import java.sql.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ServicesImpl implements IServices {

    private LoginService loginService;
    private MainPageService mainPageService;
    private Map<String,IObserver> loggedClients;

    public ServicesImpl(LoginService loginService, MainPageService mainPageService) {
        this.loginService=loginService;
        this.mainPageService=mainPageService;
        loggedClients=new ConcurrentHashMap<>();;
    }


    private final int defaultThreadsNo=5;

    public synchronized Account login(Account user, IObserver client) throws ServiceException, BadCredentialsException {
        System.out.println("entered login servicesImpl");
        Account userR=loginService.getAccount(user.getUsername(),user.getPassword());
        if (userR!=null){
            if(loggedClients.get(user.getId())!=null)
                throw new ServiceException("User already logged in.");

            loggedClients.put(user.getId(), client);
            return userR;
        }else
            throw new ServiceException("Authentication failed.");
    }

    @Override
    public synchronized Iterable<SpetacolDTO> searchByDate(Date date) throws ServiceException {
        if(date==null)
            return mainPageService.getFestivals();
        else
            return mainPageService.getFestivalsByDate(date);
    }

    @Override
    public synchronized void sellVanzare(Integer festivalID, Date date) throws ServiceException {
        try {
            mainPageService.sellVanzare(festivalID.longValue(), date);
//            loggedClients.forEach((x,y)->{
//                try {
//                    y.ticketsSold(new VanzareDTO(festivalID,date));
//                } catch (ServiceException e) {
//                    e.printStackTrace();
//                }
//            });
        } catch (ValidationException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public synchronized void logout(Account user, IObserver client) throws ServiceException {
        System.out.println("entered logout ServicesImpl");
        IObserver localClient=loggedClients.remove(user.getId());
        if (localClient==null)
            throw new ServiceException("User "+user.getId()+" is not logged in.");
    }





}
