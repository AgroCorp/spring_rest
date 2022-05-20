package me.agronaut.springrest.Controller;

import me.agronaut.springrest.Model.User;
import me.agronaut.springrest.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
