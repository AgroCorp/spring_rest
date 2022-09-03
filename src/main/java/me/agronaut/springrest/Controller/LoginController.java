package me.agronaut.springrest.Controller;

import me.agronaut.springrest.Model.User;
import me.agronaut.springrest.Service.UserService;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@RestController
public class LoginController {
    @Autowired
    private UserService service;

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @CrossOrigin
    @PostMapping("/register")
    User register(@RequestBody User newUser)
    {
        System.out.println(newUser.toString());
        return service.save(newUser);
    }

    @CrossOrigin
    @PostMapping("/login")
    String login(@RequestBody User loginUser)
    {
        return service.login(loginUser);
    }

    @CrossOrigin
    @PostMapping("/users")
    List<User> getAll(@RequestBody MultiValueMap<String, String> formData) {
        log.info("getUsers called");
        log.info("formdata: " + formData);

        return service.getAll(formData);
    }
}
