package objectProtocol;

import domain.Account;
import domain.Employee;
import domain.FestivalDTO;
import domain.TicketDTO;
import service.BadCredentialsException;
import service.IObserver;
import service.IServices;
import service.ServiceException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Date;


public class ClientObjectWorker implements Runnable, IObserver {
    private IServices server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    public ClientObjectWorker(IServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(connected){
            try {;
                Object request=input.readObject();
                System.out.println("Run of clientobjworker"+request);
                Object response=handleRequest((Request)request);
                System.out.println(response);
                if (response!=null){
                   sendResponse((Response) response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }

    private Response handleRequest(Request request){
        Response response=null;
        if (request instanceof LoginRequest){
            System.out.println("Login request ...");
            LoginRequest logReq=(LoginRequest)request;
            Account user= logReq.getUser();
            try {
                Account account=server.login(user,this);
                //TODO trimite numele
//                return new OkResponse();
                return new LoginResponse(account);
            } catch (ServiceException | BadCredentialsException e) {
                connected=false;
                return new ErrorResponse(e.getMessage());
            }
        }
        if (request instanceof FestivalRequest){
            System.out.println("Festival request");
            FestivalRequest festivalReq=(FestivalRequest) request;
            Date date = festivalReq.getDate();
            try {
                Iterable<FestivalDTO> festivalDTOS=server.searchByDate(date);
                return new FestivalResponse(festivalDTOS);

            } catch (ServiceException e) {
                connected=false;
                return new ErrorResponse(e.getMessage());
            }
        }
        if (request instanceof LogoutRequest){
            System.out.println("Logout request");
            LogoutRequest logReq=(LogoutRequest)request;
            Account user= logReq.getUser();
            try {
                server.logout(user, this);
                connected=false;
                return new OkResponse();

            } catch (ServiceException e) {
               return new ErrorResponse(e.getMessage());
            }
        }
        if(request instanceof SellTicketRequest){
            System.out.println("Sell ticket request");
            SellTicketRequest sellTicketRequest=(SellTicketRequest) request;
            TicketDTO ticketDTO= sellTicketRequest.getTicketDTO();
            try {
                server.sellTicket(ticketDTO.getFestivalID(), ticketDTO.getSeats(),ticketDTO.getClient());
                return new OkResponse();
            } catch (ServiceException e) {
                return new ErrorResponse(e.getMessage());
            }
        }

        return response;
    }

    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        output.writeObject(response);
        output.flush();
    }

    @Override
    public void ticketsSold(TicketDTO ticket) throws ServiceException {
        System.out.println("Ticket sold "+ticket);
        try {
            sendResponse(new TicketSoldResponse(ticket));
        } catch (IOException e) {
            throw new ServiceException("Sending error: "+e);
        }
    }
}
