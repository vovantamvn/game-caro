package com.company.server;

import com.company.utils.TranData;
import com.company.utils.ClientLabel;

import java.awt.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server implements SendPoint {

    public static void main(String[] args) {
        new Server();
    }

    private Socket socket1;
    private Socket socket2;
    private Set<Point> points;

    public Server() {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);

            this.socket1 = serverSocket.accept();
            this.socket2 = serverSocket.accept();

            this.points = new HashSet<>();

            new ListeningFormClient(ClientLabel.LABEL_ONE, socket1, this).start();
            new ListeningFormClient(ClientLabel.LABEL_TWO, socket2, this).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void send(Point point, ClientLabel label) {
        String message = TranData.convertToString(point.x, point.y);

        if (points.contains(point)){
            return;
        }

        try {
            DataOutputStream dos1 = new DataOutputStream(socket1.getOutputStream());
            DataOutputStream dos2 = new DataOutputStream(socket2.getOutputStream());

            dos1.writeUTF(message);
            dos1.writeBoolean(label != ClientLabel.LABEL_ONE);

            dos2.writeUTF(message);
            dos2.writeBoolean(label != ClientLabel.LABEL_TWO);

            points.add(point);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
