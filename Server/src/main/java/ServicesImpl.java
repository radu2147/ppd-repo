import domain.Account;
import domain.SpetacolDTO;
import domain.VanzareDTO;
import domain.validators.ValidationException;
import service.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;


public class ServicesImpl implements IServices {

    private LoginService loginService;
    private MainPageService mainPageService;
    private Map<String,IObserver> loggedClients;
    static final int SECONDS = 5;
    private final ScheduledExecutorService checkExecutor = Executors.newScheduledThreadPool(1);

    public ServicesImpl(LoginService loginService, MainPageService mainPageService) {
        this.loginService=loginService;
        this.mainPageService=mainPageService;
        loggedClients=new ConcurrentHashMap<>();;
    }

    @Override
    public synchronized void addVanzare(Integer festivalID, Date date, List<Integer> seats) throws ServiceException {
        try {
            mainPageService.sellVanzare(festivalID.longValue(), date, seats);
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

    public void verificari() {
        mainPageService.verificari();
    }

    public synchronized void check(){
//        checkExecutor.schedule(null, SECONDS, TimeUnit.SECONDS);
//        try {
//            checkExecutor.awaitTermination(1, TimeUnit.DAYS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        checkExecutor.shutdown();
    }
}
