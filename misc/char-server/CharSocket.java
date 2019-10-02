import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/*
 * Run CharServer on a terminal and then CharSocket on another
 * 
 * Bytes read from socket input stream is encoded to UTF-16LE with
 * InputStreamReader. Chars are read from reader and new string is created using
 * count as limit otherwise string may contain extra chars.
 * 
 */
public class CharSocket {

    public static void main(String[] args) throws IOException {
        CharSocket charSocket = new CharSocket();
        charSocket.start();
    }

    public void start() throws IOException {

        try (Socket soc = new Socket("localhost", 9999);
                Reader in = new InputStreamReader(soc.getInputStream(),
                        StandardCharsets.UTF_16LE)) {
            char[] chars = new char[11];
            int count;
            while ((count = in.read(chars)) > 0) {
                System.out.print(new String(chars, 0, count));
            }
        }

    }
}
