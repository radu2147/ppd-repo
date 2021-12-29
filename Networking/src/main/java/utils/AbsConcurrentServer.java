package utils;

import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public abstract class AbsConcurrentServer extends AbstractServer {

    private ExecutorService executor = Executors.newFixedThreadPool(5);

    public AbsConcurrentServer(int port) {
        super(port);
         System.out.println("Concurrent AbstractServer");
    }

    protected void processRequest(Socket client) {
        executor.execute(createWorker(client));
    }

    protected abstract Runnable createWorker(Socket client) ;


}
