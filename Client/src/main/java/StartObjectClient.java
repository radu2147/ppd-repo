import javafx.application.Application;

import javafx.stage.Stage;
import objectProtocol.ServicesObjectProxy;
import service.IServices;
import service.ServiceException;
import utils.RandomData;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class StartObjectClient extends Application {
    private static int defaultPort = 55555;
    private static String defaultServer = "localhost";
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
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

        try{
            serverPort = Integer.parseInt(clientProps.getProperty("server.port"));
        }catch(NumberFormatException ex){
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultPort);
        }

        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);
        IServices server = new ServicesObjectProxy(serverIP, serverPort);


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        var currentDate = formatter.format(date);
        Integer nbOfShows = 3;
        Integer totalSeats = 100;


        ScheduledExecutorService executor =
                Executors.newSingleThreadScheduledExecutor();

        Runnable periodicTask = () -> {
            // Invoke method(s) to do the work
            makeASell(nbOfShows, totalSeats, date, server, executor);
        };
        executor.scheduleAtFixedRate(periodicTask, 0, 5, TimeUnit.SECONDS);
    }

    public void makeASell(Integer nbOfShows, Integer totalSeats, Date date, IServices server, ScheduledExecutorService executor) {
        RandomData randomData = new RandomData();

        Integer showId = randomData.getShowId(nbOfShows);
        List<Integer> seats = randomData.getSeats(totalSeats);

        try {
            System.out.println("\nNOTIFICATION:");
            server.addVanzare(showId, new java.sql.Date(date.getTime()), seats);
            System.out.println("Vanzare reusita!");

        } catch (ServiceException e) {
            if (e.getMessage().contains("Vanzare nereusita"))
                System.out.println(e.getMessage());
            else
                executor.shutdown();
        }

    }
}
