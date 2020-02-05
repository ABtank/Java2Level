package lesson6;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(8189);
             Socket currentClient = serverSocket.accept()) { //блокируется поток в ожидании клиента
            //Socket currentClient = serverSocket.accept()
            System.out.println("К нам пдключился клиент");
            //запрос у клиента потоков ввода/вывода
            DataInputStream in = new DataInputStream(currentClient.getInputStream());
            DataOutputStream out = new DataOutputStream(currentClient.getOutputStream());
            String b = in.readUTF();//блокирует поток в ожидании ответа
            out.writeUTF("Echo: " + b);
            //currentClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
