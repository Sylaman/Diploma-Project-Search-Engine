import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            BooleanSearchEngine booleanSearchEngine = new BooleanSearchEngine(new File("pdfs"));
            Gson gson = new GsonBuilder().create();
            try (ServerSocket serverSocket = new ServerSocket(8989)) {
                while (true) {
                    try (Socket socket = serverSocket.accept(); PrintWriter out = new PrintWriter(socket.getOutputStream(), true); BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                        System.out.println("Введите ваш запрос в одно слово");
                        String request = in.readLine();
                        List<PageEntry> page = booleanSearchEngine.search(request);
                        var json = gson.toJson(page);
                        out.println(json);
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
}

