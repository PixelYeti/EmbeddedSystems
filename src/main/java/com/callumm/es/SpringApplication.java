package com.callumm.es;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.time.LocalDateTime;

@SpringBootApplication
public class SpringApplication {

    public static void main(String[] args) throws Exception {
        org.springframework.boot.SpringApplication.run(SpringApplication.class, args);

        DeviceManager.addDiscoveredDevice(Inet4Address.getByName("192.168.1.45"));
        DeviceManager.connectDevice(new Device(Inet4Address.getByName("192.168.1.45"), LocalDateTime.now()));
        Server.startServer();
    }
}
