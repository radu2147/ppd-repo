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
    private final int TIME_TO_RUN = 20;
    public AbstractServer( int port){
              this.port=port;
    }
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public void start() {

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
        this.executor.shutdownNow();
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
