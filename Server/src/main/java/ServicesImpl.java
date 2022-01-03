import domain.validators.ValidationException;
import service.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


public class ServicesImpl implements IServices {

    private MainPageService mainPageService;

    public ServicesImpl(MainPageService mainPageService) {
        this.mainPageService=mainPageService;
    }

    @Override
    public synchronized void addVanzare(Integer festivalID, Date date, List<Integer> seats) throws VanzareException {
        try {
            mainPageService.sellVanzare(festivalID.longValue(), date, seats);
        } catch (ValidationException e) {
            throw new VanzareException(e.getMessage());
        }
    }

    public void verificari() {
        mainPageService.verificari();
    }
}
