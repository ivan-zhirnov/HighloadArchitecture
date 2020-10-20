package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static Socket clientSocket; //сокет для общения
    private static ServerSocket server; // серверсокет
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет

    public static void main(String[] args) {
        try {
            try  {
                String response = "";
                ServerSocket server = new ServerSocket(4004);
                System.out.println("Сервер запущен!");
                clientSocket = server.accept();
                try {
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                    response = "W";
                    int random = randomInt(0, 100);
                    System.out.println("Сгенерированное число:  " + random);
                    while (!response.equals("close")) {
                        response = in.readLine();
                        String[] responseWords = response.split(" ");
                        System.out.println(response);
                        int responseNumber = Integer.parseInt(responseWords[1]);
                        if (responseNumber > random) {
                            out.write("more\n");
                        } else if (responseNumber < random) {
                            out.write("less\n");
                        } else {
                            out.write("correct\n");
                        }
                        out.flush();
                    }

                } finally {
                    clientSocket.close();
                    in.close();
                    out.close();
                }
            } finally {
                System.out.println("Сервер закрыт!");
                server.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private static int randomInt(int min, int max) {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }
}
