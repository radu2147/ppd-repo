package objectProtocol;

import domain.VanzareDTO;
import service.IObserver;
import service.IServices;
import service.ServiceException;
import service.VanzareException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Date;
import java.util.List;
import java.util.concurrent.*;


public class ServicesObjectProxy implements IServices, IObserver {
    private final String host;
    private final int port;

    private IObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Socket connection;

    //private List<Response> responses;
    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;
    public ServicesObjectProxy(String host, int port) {
        this.host = host;
        this.port = port;
        initializeConnection();
        qresponses= new LinkedBlockingQueue<>();
    }

    @Override
    public void addVanzare(Integer festivalID, Date date, List<Integer> seats) throws ServiceException, VanzareException {
        sendRequest(new VanzareRequest(new VanzareDTO(festivalID,date, seats)));
        Response response=readResponse();
        if (response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            throw new VanzareException(err.getMessage());
        }
    }

    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(Request request)throws ServiceException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new ServiceException("Error sending object "+e);
        }

    }

    private Response readResponse() throws ServiceException {
        Response response=null;
        try{
            response=qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
    private void initializeConnection() {
         try {
            connection=new Socket(host,port);
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            finished=false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void startReader(){
        executor.submit(new ReaderThread());
    }

    @Override
    public void shutDown() {
        executor.shutdownNow();
        closeConnection();
    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    System.out.println("waiting for response");
                    Object response=input.readObject();
                    System.out.println("response received "+response);
                    if (response instanceof ShutdownResponse){
                         break;
                    }else{
                        try {
                            qresponses.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Reading error "+e);
                }
            }
            System.out.println("Closing...");
            shutDown();
        }
    }
}
