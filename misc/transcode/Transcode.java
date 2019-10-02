import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Transcode {

    private static long time;

    public static void main(String[] args) throws IOException {

        String str = new String("aZâΔ你好ಡತع");

        // create a big file
        Path path = Paths.get("x-utf16.txt");
        long fileSize =
                createBigFile(path, str, StandardCharsets.UTF_16, 1000 * 1000);

        // create a big array
        byte[] bigArray =
                createBigArray(str, StandardCharsets.UTF_16, 1000 * 1000);

        // convert file from UTF-16 to UTF-8
        markTime("start transcode");
        Path toPath = Paths.get("x-utf8.txt");
        transcodeFile(path, toPath, StandardCharsets.UTF_16,
                StandardCharsets.UTF_8);
        markTime(String.format("Java.IO, converted %s to %s size %d bytes",
                path, toPath, fileSize));

        byte[] convertedBytes = transcodeBytesAsString(bigArray,
                StandardCharsets.UTF_16, StandardCharsets.UTF_8);
        markTime(String.format("String encode and decode, converted %d bytes",
                convertedBytes.length));

        convertedBytes = transcodeBytesAsStreams(bigArray,
                StandardCharsets.UTF_16, StandardCharsets.UTF_8);
        markTime(String.format("Java.IO encode and decode, converted %d bytes",
                convertedBytes.length));
    }

    private static void transcodeFile(Path srcFile, Path dstFile,
            Charset fromCs, Charset toCs) throws IOException {
        try (BufferedReader in = Files.newBufferedReader(srcFile, fromCs);
                BufferedWriter out = Files.newBufferedWriter(dstFile, toCs);) {
            char[] buf = new char[10];
            int c;
            while ((c = in.read(buf)) > 0) {
                out.write(buf, 0, c);
            }
        }
    }

    private static byte[] transcodeBytesAsString(byte[] bytes, Charset fromCs,
            Charset toCs) {
        String str = new String(bytes, fromCs);
        byte[] toBytes = str.getBytes(toCs);
        return toBytes;
    }

    private static byte[] transcodeBytesAsStreams(byte[] bytes, Charset fromCs,
            Charset toCs) throws IOException {
        ByteArrayOutputStream boa = new ByteArrayOutputStream();
        try (Reader in = new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(bytes), fromCs));
                Writer out =
                        new BufferedWriter(new OutputStreamWriter(boa, toCs))) {
            char[] buf = new char[20];
            int c;
            while ((c = in.read(buf)) > 0) {
                out.write(buf, 0, c);
            }
        }
        return boa.toByteArray();
    }

    private static long createBigFile(Path path, String str, Charset charset,
            int repeat) throws IOException {
        try (Writer out = Files.newBufferedWriter(path, charset)) {
            for (int i = 0; i < repeat; i++) {
                out.write(str);
            }
        }
        return Files.size(path);
    }

    public static byte[] createBigArray(String str, Charset charset, int repeat)
            throws IOException {
        byte[] bytes = str.getBytes(charset);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (int c = 0; c < repeat; c++) {
            out.write(bytes);
        }
        return out.toByteArray();
    }

    private static void markTime(String msg) {
        if (time == 0) {
            System.out.println(msg);
            time = System.currentTimeMillis();
        } else {
            long eTime = System.currentTimeMillis();
            System.out.printf("%s: %d ms %n", msg, (eTime - time));
            time = eTime;
        }
    }
}
