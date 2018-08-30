package com.callumm.es;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DeviceManager {

    private static List<Device> discoveredDevices = new ArrayList<>();
    private static List<Device> connectedDevices = new ArrayList<>();

    public static List<Device> getDiscoveredDevices() {
        return discoveredDevices;
    }

    public static List<Device> getConnectedDevices() {
        return connectedDevices;
    }

    public static void addDiscoveredDevice(InetAddress address) throws Exception {
        for (Device device : discoveredDevices)
            if (device.getAddress() == address)
                throw new Exception("Device already in list");
        discoveredDevices.add(new Device(address, LocalDateTime.now()));
    }

    public static void connectDevice(Device device) {
        connectedDevices.add(device);
    }

    public static Device getConnectedDevice(InetAddress address) {
        for (Device device : connectedDevices) {
            if (device.getAddress().getHostAddress().equalsIgnoreCase(address.getHostAddress()))
                return device;
        }
        return null;
    }

    public static void updateConnectedDevice(Device newDevice) {
        for (Device device : connectedDevices) {
            if (device.getAddress() != newDevice.getAddress())
                continue;
            device.setLastSeen(LocalDateTime.now());
            device.setBatteryOk(newDevice.isBatteryOk());
            device.setLastOpened(newDevice.getLastOpened());
            break;
        }
    }

    public static void removeConnectedDevice(InetAddress address) {
        for (Device device : connectedDevices) {
            if (device.getAddress() == address) {
                connectedDevices.remove(device);
                // Send forget message to device
                break;
            }
        }
    }
}
