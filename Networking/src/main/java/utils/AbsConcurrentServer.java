package utils;

import objectProtocol.Worker;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public abstract class AbsConcurrentServer extends AbstractServer {

    private ExecutorService executor = Executors.newFixedThreadPool(5);

    private List<Worker> tasks = new ArrayList<>();

    public AbsConcurrentServer(int port) {
        super(port);
        System.out.println("Concurrent AbstractServer");
    }

    protected void processRequest(Socket client) {
        var run = createWorker(client);
        executor.execute(run);
        tasks.add(run);
    }

    @Override
    protected void notifyConnectedClients() {
        for(var el: tasks){
            el.shutDown();
        }
        executor.shutdown();
    }

    protected abstract Worker createWorker(Socket client);


}
