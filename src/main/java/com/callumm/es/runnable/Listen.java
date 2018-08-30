package com.callumm.es.runnable;

import com.callumm.es.utility.StringUtils;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Listen implements Runnable {

    private static DatagramSocket socket = null;

    private static final int PORT_NUM = 2506;

    @Override
    public void run() {
        try {
            DatagramSocket serverSocket = new DatagramSocket(PORT_NUM);

            byte[] receiveData = new byte[1024];
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                // Received -- Terminal type and IP address
                String receivedInfo = new String(receivePacket.getData());
                if (!receivedInfo.startsWith(StringUtils.getNetworkPrefix())) {
                    // Reject connection
                    continue;
                }
                boolean skip = false;
                for (InetAddress address : listAllLocalAddresses()) {
                    if (address.getHostAddress().equalsIgnoreCase(receivePacket.getAddress().getHostAddress())) {
                        skip = true;
                        break;
                    }
                }
                if (skip)
                    continue;
                // Start conversation in new thread
                Thread communicationThread = new Thread(new Communicate(serverSocket, receivePacket));
                communicationThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Set<InetAddress> listAllLocalAddresses() throws SocketException {
        Set<InetAddress> localAddress = new HashSet<>();
        Enumeration<NetworkInterface> interfaces
                = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();

            if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                continue;
            }

            networkInterface.getInterfaceAddresses().stream()
                    .map(a -> a.getAddress())
                    .filter(Objects::nonNull)
                    .forEach(localAddress::add);
        }
        return localAddress;
    }
}
