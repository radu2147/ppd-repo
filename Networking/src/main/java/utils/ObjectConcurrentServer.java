package utils;

import objectProtocol.ClientObjectWorker;
import service.IServices;

import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ObjectConcurrentServer extends AbsConcurrentServer {
    private IServices services;
    private ExecutorService executor = Executors.newFixedThreadPool(5);
    public ObjectConcurrentServer(int port, IServices server) {
        super(port);
        this.services = server;
        System.out.println("ObjectConcurrentServer");

    }

    @Override
    protected Runnable createWorker(Socket client) {
        return new ClientObjectWorker(services, client);
    }


}
