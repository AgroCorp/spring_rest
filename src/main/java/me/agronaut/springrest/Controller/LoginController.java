package me.agronaut.springrest.Controller;

import lombok.extern.log4j.Log4j2;
import me.agronaut.springrest.Model.User;
import me.agronaut.springrest.Model.UserDao;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@CrossOrigin
@Log4j2
public class LoginController {
    @Autowired
    private UserService service;


    @PostMapping("/register")
    public UserDao register(@RequestBody User newUser) throws UserService.UserExistByEmailException
    {
        log.debug("START");
        User registeredUser = service.register(newUser);

        return new UserDao(registeredUser);
    }

    @PostMapping("/login")
    public UserDao login(@RequestBody User loginUser) throws UserService.NotActiveUserException
    {
        log.info("requestben kapott user: \n\t{}", loginUser);
        return new UserDao(service.login(loginUser));
    }

    @PostMapping("/forgot_password")
    public String forgotPassword(@RequestBody String email) {
        service.reset_password(email);
        return "OK";
    }

    @PostMapping("/set_new_password/{token}")
    public UserDao setNewPassword(@PathVariable String token, @RequestBody String newPassword) {
        return new UserDao(service.set_new_password(newPassword, token));
    }

    @Secured("ADMIN")
    @PostMapping(value = "/list_all_user", consumes = "application/json", produces = "application/json")
    public List<UserDao> getAll(@RequestBody User user) {
        log.info("user to search: " + user);

        return service.getAll(user).stream().map(UserDao::new).collect(Collectors.toList());
    }
}
