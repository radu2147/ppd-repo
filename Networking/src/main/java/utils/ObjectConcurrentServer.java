package utils;

import objectProtocol.ClientObjectWorker;
import objectProtocol.Worker;
import service.IServices;

import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class ObjectConcurrentServer extends AbsConcurrentServer {
    private IServices services;
    public ObjectConcurrentServer(int port, IServices server) {
        super(port);
        this.services = server;
        System.out.println("ObjectConcurrentServer");

    }

    @Override
    public void verificari() {
        services.verificari();
    }

    @Override
    protected Worker createWorker(Socket client) {
        return new ClientObjectWorker(services, client);
    }


}
