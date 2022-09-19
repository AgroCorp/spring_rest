package me.agronaut.springrest.Controller;

import lombok.extern.log4j.Log4j2;
import me.agronaut.springrest.Model.User;
import me.agronaut.springrest.Service.UserService;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@CrossOrigin
@Log4j2
public class LoginController {
    @Autowired
    private UserService service;


    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User newUser)
    {
        log.debug("START");
        User registeredUser = service.register(newUser);

        return new ResponseEntity<>(registeredUser, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User loginUser)
    {
        return new ResponseEntity<>(service.login(loginUser), HttpStatus.OK);
    }

    @PostMapping("/forgot_password")
    public String forgotPassword(@RequestBody String email) {
        service.reset_password(email);
        return "OK";
    }

    @PostMapping("/set_new_password/{token}")
    public ResponseEntity<User> setNewPassword(@PathVariable String token, @RequestBody String newPassword) {
        return new ResponseEntity<>(service.set_new_password(newPassword, token), HttpStatus.OK);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @Secured("ADMIN")
    @PostMapping("/list_all_user")
    public List<User> getAll(@RequestBody MultiValueMap<String, String> formData) {
        log.info("getUsers called");
        log.info("formdata: " + formData);

        return service.getAll(formData);
    }
}
