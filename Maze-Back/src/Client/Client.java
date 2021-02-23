package Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client implements IClientStrategy{

    private int port;
    private InetAddress host;
    private IClientStrategy client;

    public Client(InetAddress host, int port, IClientStrategy client) {
        this.port = port;
        this.host = host;
        this.client = client;
    }

    @Override
    public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {}

    public void communicateWithServer() {
        try {
            Socket s = new Socket(host,port);
            client.clientStrategy(s.getInputStream(),s.getOutputStream());
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
