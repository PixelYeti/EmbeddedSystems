package com.callumm.es.runnable;

import java.io.IOException;
import java.net.*;
import java.util.*;

public class Discover implements Runnable {

    private static DatagramSocket socket = null;

    @Override
    public void run() {
        try {
            sendBroadcast();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendBroadcast() throws IOException {
        for (InetAddress address : listAllBroadcastAddresses()) {
            if (broadcast("[IOT]::HUB", address))
                break;
        }
    }

    private static boolean broadcast(String broadcastMessage, InetAddress address) throws IOException {
        socket = new DatagramSocket();
        socket.setBroadcast(true);

        byte[] buffer = broadcastMessage.getBytes();

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 2506);
        socket.send(packet);

        return true;
    }

    private static Set<InetAddress> listAllBroadcastAddresses() throws SocketException {
        Set<InetAddress> broadcastList = new HashSet<>();
        Enumeration<NetworkInterface> interfaces
                = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();

            if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                continue;
            }

            networkInterface.getInterfaceAddresses().stream()
                    .map(a -> a.getBroadcast())
                    .filter(Objects::nonNull)
                    .forEach(broadcastList::add);
        }
        return broadcastList;
    }
}
