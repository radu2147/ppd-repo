package utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;


public abstract class AbstractServer {
    private final int port;
    private ServerSocket server=null;
    private final int TIME_TO_RUN = 10;
    public AbstractServer( int port){
              this.port=port;
    }

    public void start() {

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Future<?> future = executor.submit(() -> {
            try{
                server=new ServerSocket(port);
                while(true){
                    System.out.println("Waiting for clients ...");
                    Socket client = server.accept();
                    System.out.println("Client connected ...");
                    processRequest(client);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                stop();
            }
        });
        try {
            future.get(TIME_TO_RUN, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            notifyConnectedClients();
            stop();
            System.out.println(future.cancel(true));
        }
        System.out.println("Gracefully shutting down the server...");
        executor.shutdown();
        System.out.println("Shut down");

    }

    protected abstract void processRequest(Socket client);

    protected abstract void notifyConnectedClients();

    public void stop() {
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
