package com.company.server;

import com.company.utils.ClientLabel;

import java.awt.*;

public interface SendPoint {
    void send(Point point, ClientLabel label);
}
