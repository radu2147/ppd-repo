import objectProtocol.ServicesObjectProxy;
import service.IServices;
import service.ServiceException;
import service.VanzareException;
import utils.RandomData;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;

abstract class FutureRunnable implements Runnable {
    private Future<?> task;

    public Future<?> getTask() {
        return task;
    }

    public void setTask(Future<?> task) {
        this.task = task;
    }
}


public class StartObjectClient {
    private static int defaultPort = 55555;
    private static String defaultServer = "localhost";
    public static void main(String[] args) {
        start();
    }

    public static void start() {
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartObjectClient.class.getResourceAsStream("/client.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find client.properties " + e);
            return;
        }

        String serverIP = clientProps.getProperty("server.host", defaultServer);
        int serverPort = defaultPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultPort);
        }

        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);
        IServices server = new ServicesObjectProxy(serverIP, serverPort);

        Date date = new Date();
        Integer nbOfShows = 3;
        Integer totalSeats = 100;

        ExecutorService executor = Executors.newSingleThreadExecutor();
        FutureRunnable periodicTask = new FutureRunnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        break;
                    }
                    try {
                        makeASell(nbOfShows, totalSeats, date, server);
                    } catch (VanzareException e) {
                        System.out.println(e.getMessage());
                    } catch (ServiceException e) {
                        executor.shutdownNow();
                        break;
                    }
                }
            }
        };
        executor.execute(periodicTask);
    }

    public static void makeASell(Integer nbOfShows, Integer totalSeats, Date date, IServices server) throws ServiceException, VanzareException {
        RandomData randomData = new RandomData();

        Integer showId = randomData.getShowId(nbOfShows);
        List<Integer> seats = randomData.getSeats(totalSeats);

        System.out.println("\nNOTIFICATION:");
        server.addVanzare(showId, new java.sql.Date(date.getTime()), seats);
        System.out.println("Vanzare reusita!");

    }
}