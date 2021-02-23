package Client;

import java.io.InputStream;
import java.io.OutputStream;

public interface IClientStrategy {

    public void clientStrategy(InputStream inFromServer, OutputStream outToServer);
}
