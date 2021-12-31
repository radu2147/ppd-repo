import repository.FestivalRepo;
import repository.VanzareLocuriRepo;
import repository.VanzareRepo;
import service.IServices;
import service.MainPageService;
import service.VanzareService;
import utils.AbstractServer;
import utils.ObjectConcurrentServer;

import java.io.IOException;
import java.util.Properties;


public class StartObjectServer {
    private static int defaultPort=55555;
    public static void main(String[] args) {
       Properties serverProps=new Properties();
        try {
            serverProps.load(StartObjectServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.properties "+e);
            return;
        }
        FestivalRepo festivalRepo=new FestivalRepo(serverProps);
        VanzareRepo VanzareRepo=new VanzareRepo(serverProps);
        var vanzareLocuriRepo = new VanzareLocuriRepo(serverProps);
        VanzareService VanzareService=new VanzareService(VanzareRepo,festivalRepo, vanzareLocuriRepo);
        MainPageService mainPageService=new MainPageService(VanzareService);

        IServices services=new ServicesImpl(mainPageService);
        int serverPort=defaultPort;
        try {
            serverPort = Integer.parseInt(serverProps.getProperty("server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+serverPort);
        AbstractServer server = new ObjectConcurrentServer(serverPort, services);
        server.start();
    }
}
