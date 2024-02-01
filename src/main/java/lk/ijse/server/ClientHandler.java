package lk.ijse.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread{
    private Socket socket;

    private ArrayList<ClientHandler> clientHandlers;

    private BufferedReader bufferedReader;

    private PrintWriter printWriter;

    public ClientHandler(Socket socket, ArrayList<ClientHandler> clientHandlers){
        try {
            this.socket = socket;
            this.clientHandlers = clientHandlers;
            this.bufferedReader = new BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));
            this.printWriter = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run(){
        try {
            String message;
            while ((message = bufferedReader.readLine()) != null){
                for (ClientHandler client : clientHandlers) {
                    client.printWriter.println(message);
                }
            }
            printWriter.close();
            bufferedReader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
