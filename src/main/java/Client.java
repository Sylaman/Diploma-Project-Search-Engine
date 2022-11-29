import com.google.gson.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

@SuppressWarnings("deprecation")
public class Client {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try (Socket socket = new Socket("localhost", 8989);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String request = in.readLine();
            System.out.println(request);
            String answer = scanner.nextLine();
            out.println(answer);
            String initialJson = in.readLine();
            System.out.println(getJson(initialJson));
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }

    public static String getJson(String json) {
        GsonBuilder jsonBuilder = new GsonBuilder();
        Gson gson = jsonBuilder.setPrettyPrinting().create();
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(json);
        return gson.toJson(jsonElement);
    }
}