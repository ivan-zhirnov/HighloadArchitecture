package client;

import java.io.*;
import java.net.Socket;

public class Client {
    private static Socket clientSocket;
    private static BufferedReader reader;
    private static BufferedReader in;
    private static BufferedWriter out;

    public static void main(String[] args) {
        try {
            try {
                clientSocket = new Socket("localhost", 4004);
                reader = new BufferedReader(new InputStreamReader(System.in));
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                String response = "";
                while (!response.equals("correct")) {
                    System.out.println("Type your command:");
                    String word = reader.readLine();
                    out.write(word + "\n");
                    out.flush();
                    response = in.readLine();
                    System.out.println(response);
                }
            } finally {
                System.out.println("Client closed");
                out.write("close\n");
                out.flush();
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }

    }
}
