package me.agronaut.springrest.Controller;

import lombok.extern.log4j.Log4j2;
import me.agronaut.springrest.Model.User;
import me.agronaut.springrest.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@CrossOrigin
@Log4j2
public class LoginController {
    private final UserService service;
    @Autowired
    public LoginController(UserService service) {
        this.service = service;
    }


    @PostMapping("/register")
    public User register(@RequestBody User newUser) throws UserService.UserExistByEmailException
    {
        log.debug("START");

        return service.register(newUser);
    }

    @PostMapping("/login")
    public User login(@RequestBody User loginUser) throws UserService.NotActiveUserException
    {
        log.info("requestben kapott user: \n\t{}", loginUser);
        return service.login(loginUser);
    }

    @PostMapping("/forgot_password")
    public String forgotPassword(@RequestBody String email) {
        service.reset_password(email);
        return "OK";
    }

    @PostMapping("/set_new_password/{token}")
    public User setNewPassword(@PathVariable String token, @RequestBody String newPassword) {
        return service.set_new_password(newPassword, token);
    }

    @Secured("ADMIN")
    @PostMapping(value = "/list_all_user", consumes = "application/json", produces = "application/json")
    public List<User> getAll(@RequestBody User user) {
        log.info("user to search: " + user);

        return service.getAll(user);
    }
}
