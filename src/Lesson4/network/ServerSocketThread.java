package Lesson4.network;

public class ServerSocketThread extends Thread{

    private int port;


    public ServerSocketThread(String name, int port) {
        super(name);
        this.port = port;
        start();
    }


    @Override
    public void run() {
        while (!isInterrupted()){
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                interrupt();
            }
            System.out.println("Server socket thread is working");
        }
        System.out.println("Server stopped");
    }
}
