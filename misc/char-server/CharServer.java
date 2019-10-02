import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/*
 * Run CharServer on a terminal and then CharSocket on another
 * 
 * Bytes are encoded as UTF-16LE and pushed to socket output stream
 */
public class CharServer {

    public static void main(String[] args) throws IOException {
        
        String str = "a Z â Δ 你 好 ಡ ತ ع";

        try (ServerSocket serverSoc = new ServerSocket(9999)) {
            System.out.printf("server started at: %s%n",
                    serverSoc.getLocalSocketAddress());
            while (true) {
                System.out.println("Waiting for connection");
                try (Socket soc = serverSoc.accept();
                        OutputStream os = soc.getOutputStream()) {
                    byte[] bytes = str.getBytes(StandardCharsets.UTF_16LE);
                    os.write(bytes);
                    System.out.printf("connected and wrote %d bytes%n",
                            bytes.length);
                }
                System.out.println("connection closed");
            }
        }
    }
}
