package utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;


public abstract class AbstractServer {
    private final int port;
    private ServerSocket server=null;
    private final int TIME_TO_RUN = 30;
    ScheduledExecutorService executorVerificari;
    ScheduledExecutorService executor;
    public AbstractServer( int port){
              this.port=port;
    }

    public abstract void verificari();

    public void start() {

        executorVerificari = Executors.newScheduledThreadPool(1);

        executorVerificari.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                verificari();
            }
        }, 0, 5, TimeUnit.SECONDS);

        executor = Executors.newScheduledThreadPool(1);
        Future<?> future = executor.submit(() -> {
            try{
                server=new ServerSocket(port);
                while(true){
                    System.out.println("Waiting for clients ...");
                    Socket client = server.accept();
                    System.out.println("Client connected ...");
                    processRequest(client);
                }
            } catch (SocketException err){
                System.out.println("Error with accepting the clients or shutting down");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });
        try {
            future.get(TIME_TO_RUN, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            stop();
        }
        System.out.println("Gracefully shutting down the server...");
        System.out.println("Shut down");
    }

    protected abstract void processRequest(Socket client);

    public void stop() {
        try {
            executorVerificari.shutdownNow();
            executor.shutdownNow();
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
