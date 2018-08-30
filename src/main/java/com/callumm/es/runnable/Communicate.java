package com.callumm.es.runnable;

import com.callumm.es.Device;
import com.callumm.es.DeviceManager;
import com.callumm.es.utility.StringUtils;

import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Communicate implements Runnable {

    private DatagramSocket receiveSocket;
    private InetAddress address;
    private int port;
    private String dataReceived;

    public Communicate(DatagramSocket receiveSocket, DatagramPacket receivedPacket) {
        this.receiveSocket = receiveSocket;
        this.address = receivedPacket.getAddress();
        this.port = receivedPacket.getPort();
        this.dataReceived = new String(receivedPacket.getData());
    }

    @Override
    public void run() {
        try {
            String processData = dataReceived.substring(StringUtils.getNetworkPrefix().length());

            String[] splitInfo = processData.split(";");

            for (String kvPair : splitInfo) {
                if (kvPair.toCharArray()[0] == '\u0000')
                    continue;
                if (kvPair.trim().length() == 0)
                    continue;
                String key = kvPair.split(":")[0].trim();
                String value = kvPair.split(":")[1].trim();

                switch (key.toLowerCase()) {
                    case "discover":
                        DeviceManager.addDiscoveredDevice(address);
                        break;
                    case "battery": {
                        Device device = DeviceManager.getConnectedDevice(address);
                        if (device == null) {
                            DeviceManager.connectDevice(new Device(address, LocalDateTime.now()));
                            device = DeviceManager.getConnectedDevice(address);
                        }
                        device.setBatteryOk(value.equalsIgnoreCase("ok"));
                        DeviceManager.updateConnectedDevice(device);
                        break;
                    }
                    case "lastopened": {
                        Device device = DeviceManager.getConnectedDevice(address);
                        if (device == null) {
                            DeviceManager.connectDevice(new Device(address, LocalDateTime.now()));
                            device = DeviceManager.getConnectedDevice(address);
                        }
                        if (value.equalsIgnoreCase("1"))
                            device.setLastOpened(LocalDateTime.now());
                        DeviceManager.updateConnectedDevice(device);
                        break;
                    }
                    case "tamper": {
                        Device device = DeviceManager.getConnectedDevice(address);
                        if (device == null) {
                            DeviceManager.connectDevice(new Device(address, LocalDateTime.now()));
                            device = DeviceManager.getConnectedDevice(address);
                        }
                        if (value.equalsIgnoreCase("1"))
                            device.setLastTamper(LocalDateTime.now());
                        DeviceManager.updateConnectedDevice(device);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
