package com.callumm.es;

import java.net.InetAddress;
import java.time.LocalDateTime;

public class Device {

    private InetAddress address;
    private LocalDateTime lastSeen;
    private LocalDateTime lastOpened;
    private LocalDateTime lastTamper;
    private boolean batteryOk;

    public Device(InetAddress address, LocalDateTime lastSeen) {
        this.address = address;
        this.lastSeen = lastSeen;
    }

    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public LocalDateTime getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(LocalDateTime lastSeen) {
        this.lastSeen = lastSeen;
    }

    public LocalDateTime getLastOpened() {
        return lastOpened;
    }

    public void setLastOpened(LocalDateTime lastOpened) {
        this.lastOpened = lastOpened;
    }

    public boolean isBatteryOk() {
        return batteryOk;
    }

    public void setBatteryOk(boolean batteryOk) {
        this.batteryOk = batteryOk;
    }

    public LocalDateTime getLastTamper() {
        return lastTamper;
    }

    public void setLastTamper(LocalDateTime lastTamper) {
        this.lastTamper = lastTamper;
    }
}
