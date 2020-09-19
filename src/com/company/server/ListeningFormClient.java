package com.company.server;

import com.company.utils.TranData;
import com.company.utils.ClientLabel;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ListeningFormClient extends Thread {

    private final Socket socket;
    private final SendPoint sendPoint;
    private final ClientLabel label;

    public ListeningFormClient(ClientLabel label, Socket socket, SendPoint sendPoint) {
        this.label = label;
        this.socket = socket;
        this.sendPoint = sendPoint;
    }

    @Override
    public void run() {
        while (true) {
            try {
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                String message = dataInputStream.readUTF();
                Point point = TranData.convertToPoint(message);

                if (point != null){
                    sendPoint.send(point, this.label);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
