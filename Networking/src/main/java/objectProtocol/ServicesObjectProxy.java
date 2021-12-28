package objectProtocol;

import domain.Account;
import domain.Festival;
import domain.FestivalDTO;
import domain.TicketDTO;
import service.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class ServicesObjectProxy implements IServices {
    private final String host;
    private final int port;

    private IObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    //private List<Response> responses;
    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;
    public ServicesObjectProxy(String host, int port) {
        this.host = host;
        this.port = port;
        //responses=new ArrayList<Response>();
        qresponses=new LinkedBlockingQueue<Response>();
    }

    public Account login(Account user, IObserver client) throws ServiceException {
        initializeConnection();
        sendRequest(new LoginRequest(user));
        Response response=readResponse();
        if (response instanceof LoginResponse){
            this.client=client;
            return ((LoginResponse) response).getUser();
        }
        if (response instanceof OkResponse){
            this.client=client;
            return null;
        }
        if (response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            closeConnection();
            throw new ServiceException(err.getMessage());
        }
        return null;
    }

    @Override
    public Iterable<FestivalDTO> searchByDate(Date date) throws ServiceException {
        sendRequest(new FestivalRequest(date));
        Response response=readResponse();
        if (response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            throw new ServiceException(err.getMessage());
        }
        if(response instanceof FestivalResponse)
            return ((FestivalResponse)response).getFestivalDTOS();
        return null;
    }

    @Override
    public void sellTicket(Integer festivalID, Long seats, String client) throws ServiceException {
        sendRequest(new SellTicketRequest(new TicketDTO(festivalID,seats,client)));
        Response response=readResponse();
        if (response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            throw new ServiceException(err.getMessage());
        }
        System.out.println("Ticket sold - proxy");
    }

    public void logout(Account user, IObserver client) throws ServiceException {
        sendRequest(new LogoutRequest(user));
        Response response=readResponse();
        closeConnection();
        if (response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            throw new ServiceException(err.getMessage());
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
            throw new  ServiceException("Error sending object "+e);
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
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }


    private void handleUpdate(UpdateResponse update){

        if (update instanceof TicketSoldResponse){
            TicketSoldResponse ticketSoldResponse=(TicketSoldResponse) update;
            TicketDTO ticketDTO=ticketSoldResponse.getTicketDTO();
            System.out.println("Some tickets were sold");
            try{
                client.ticketsSold(ticketDTO);
            }catch (ServiceException e){
                e.printStackTrace();
            }
        }
    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    System.out.println("waiting for response");
                    Object response=input.readObject();
                    System.out.println("response received "+response);
                    if (response instanceof UpdateResponse){
                         handleUpdate((UpdateResponse)response);
                    }else{
                        try {
                            qresponses.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();  
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }
}
