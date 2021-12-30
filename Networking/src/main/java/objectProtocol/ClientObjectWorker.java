package objectProtocol;

import domain.Account;
import domain.SpetacolDTO;
import domain.VanzareDTO;
import service.BadCredentialsException;
import service.IObserver;
import service.IServices;
import service.ServiceException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Date;


public class ClientObjectWorker implements Worker {
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
            try {
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
            if(!connected){
                try {
                    sendResponse(new ShutdownResponse());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Shutting down worker...");
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
        if(request instanceof VanzareRequest){
            System.out.println("Sell Vanzare request");
            VanzareRequest sellVanzareRequest=(VanzareRequest) request;
            VanzareDTO VanzareDTO= sellVanzareRequest.getVanzareDTO();
            try {
                server.addVanzare(VanzareDTO.getFestivalID(), VanzareDTO.getDate(), VanzareDTO.getSeats());
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
    public void shutDown() {
        this.connected = false;
    }
}
