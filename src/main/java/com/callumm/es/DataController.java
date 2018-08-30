package com.callumm.es;

import com.callumm.es.runnable.Discover;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataController {

    @CrossOrigin
    @RequestMapping("/")
    public String index() {
        ObjectMapper mapper = new ObjectMapper();
        StringBuilder arrayString = new StringBuilder("[");
        for (Device device : DeviceManager.getConnectedDevices()) {
            try {
                arrayString.append(mapper.writeValueAsString(device));
                if (DeviceManager.getConnectedDevices().indexOf(device) != DeviceManager.getConnectedDevices().size() - 1)
                    arrayString.append(",");
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        arrayString.append("]");
        return arrayString.toString();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/")
    public String discover() {
        Thread discoverThread = new Thread(new Discover());
        discoverThread.start();

        if (discoverThread.isAlive())
            return "{'Discover':'OK'}";
        return "{'Discover':'Error'}";
    }
}
