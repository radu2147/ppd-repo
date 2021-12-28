package utils;

import objectProtocol.ClientObjectWorker;
import service.IServices;

import java.net.Socket;


public class ObjectConcurrentServer extends AbsConcurrentServer {
    private IServices services;
    public ObjectConcurrentServer(int port, IServices server) {
        super(port);
        this.services = server;
        System.out.println("ObjectConcurrentServer");

    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientObjectWorker worker=new ClientObjectWorker(services, client);
        Thread tw=new Thread(worker);
        return tw;
    }


}
