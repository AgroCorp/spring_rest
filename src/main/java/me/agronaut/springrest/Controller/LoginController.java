package me.agronaut.springrest.Controller;

import me.agronaut.springrest.Model.User;
import me.agronaut.springrest.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
public class LoginController {
    @Autowired
    private UserService service;

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
    @PostMapping("/logout")
    Boolean logout(@RequestAttribute String token)
    {
        return service.logout(token);
    }

    @CrossOrigin
    @GetMapping("/users")
    List<User> getall() {
        return service.getall();
    }
}
