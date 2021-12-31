package utils;

import objectProtocol.Worker;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public abstract class AbsConcurrentServer extends AbstractServer {

    private ExecutorService executor = Executors.newFixedThreadPool(5);
    private List<Worker> lista = new ArrayList<>();

    public AbsConcurrentServer(int port) {
        super(port);
        System.out.println("Concurrent AbstractServer");
    }

    protected void processRequest(Socket client) {
        var run = createWorker(client);
        lista.add(run);
        executor.submit(run);
    }

    @Override
    public void stop() {
        super.stop();
        for (var future : lista) {
            future.shutDown();
        }
        this.executor.shutdownNow();
    }

    protected abstract Worker createWorker(Socket client);


}
