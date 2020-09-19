package com.company.client;

import com.company.utils.TranData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client extends JFrame implements MouseListener, Runnable {

    public static void main(String[] args) {
        try {
            new Client();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final int rows;
    private final int margin;
    private final int size;
    private final List<Point> points;
    private final Socket socket;
    private boolean isPlay = true;

    public Client() throws IOException {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(600, 600);
        this.addMouseListener(this);
        this.setVisible(true);

        this.rows = 15;
        this.margin = 50;
        this.size = 30;
        this.points = new ArrayList<>();
        this.socket = new Socket("localhost", 8080);

        new Thread(this::run).start();
    }

    @Override
    public void paint(Graphics g) {
        // draw columns
        for (int i = 0; i <= rows; i++) {
            g.drawLine(margin + size * i, margin, margin + size * i, margin + size * rows);
        }

        // draw rows
        for (int i = 0; i <= rows; i++) {
            g.drawLine(margin, margin + size * i, margin + size * rows, margin + size * i);
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("arial", Font.BOLD, this.size));

        for (int i=0; i<points.size(); i++){
            Point point = points.get(i);
            int x = this.margin + this.size * point.x + this.size/4 - this.size/8;
            int y = this.margin + this.size * point.y + this.size/2 + this.size/4 + this.size/8;

            System.out.println("Draw: " + x + ":" + y);

            if (i%2 == 0){
                g.drawString("O", x, y);
            } else {
                g.drawString("X", x, y);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (!this.isPlay){
            return;
        }

        int x = mouseEvent.getX();
        int y = mouseEvent.getY();

        if (x < margin || x >= margin + this.size * this.rows) {
            return;
        }

        if (y < margin || y >= margin + this.size * this.rows) {
            return;
        }

        int indexOfX = (x - this.margin) / this.size;
        int indexOfY = (y - this.margin) / this.size;

        String message = TranData.convertToString(indexOfX, indexOfY);

        try {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(message);

            this.isPlay = false;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void run() {
        while (true) {
            try {
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                String message = dataInputStream.readUTF();
                this.isPlay = dataInputStream.readBoolean();

                Point point = TranData.convertToPoint(message);

                if (point != null) {
                    this.points.add(point);
                    this.repaint();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
