package Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable{
    private int port;
    private IServerStrategy serverStrategy;
    private int listeningInterval;
    private volatile boolean stop;
    private ExecutorService threadPool;
    private int nThreds;

    public Server(int port, int  listeningInterval, IServerStrategy clientHandler){
        this.port = port;
        this.listeningInterval = listeningInterval;
        this.serverStrategy = clientHandler;
        this.stop = false;
       /* if (getT.equals("Runtime")){
            nThreds =Runtime.getRuntime().availableProcessors()*2;
        }
        else {
            nThreds= Integer.parseInt(getT);
        }
        this.threadPool = Executors.newFixedThreadPool(nThreds);*/
       this.threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);
    }

    public void start(){
        new Thread(this).start();
    }
    public void run()
    {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(listeningInterval);
            while (!stop)
            {
                try {
                    Socket clientSocket = serverSocket.accept();
                    threadPool.execute(() ->{
                        handle(clientSocket);
                    });

                }
                catch (IOException e) {
                }
            }

            serverSocket.close();
            threadPool.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handle(Socket clientSocket) {
        try {
            InputStream inFromClient = clientSocket.getInputStream();
            OutputStream outToClient = clientSocket.getOutputStream();
            this.serverStrategy.handleClient(inFromClient, outToClient);
            inFromClient.close();
            outToClient.close();
            clientSocket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void stop()
    {
        this.stop = true;
    }
}
