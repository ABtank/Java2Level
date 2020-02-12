package lesson6;

import java.io.*;
import java.net.Socket;

public class SimpleClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("127.0.0.1", 8189)) {
            //создаем сокет и слушаем его же
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("Hello server!");
            String b = in.readUTF();
            System.out.println("we have got an answer: " + b);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
