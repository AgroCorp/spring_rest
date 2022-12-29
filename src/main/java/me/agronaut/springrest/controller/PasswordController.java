package me.agronaut.springrest.controller;

import me.agronaut.springrest.model.Password;
import me.agronaut.springrest.model.PasswordDto;
import me.agronaut.springrest.model.User;
import me.agronaut.springrest.service.PasswordService;
import me.agronaut.springrest.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/password")
@CrossOrigin
public class PasswordController {
    private final PasswordService passwordService;
    private final UserService userService;

    private final ModelMapper modelMapper;

    @Autowired
    public PasswordController(PasswordService passwordService, UserService userService, ModelMapper mapper) {
        this.passwordService = passwordService;
        this.userService = userService;
        this.modelMapper = mapper;
    }

    @PostMapping("/add")
    public ResponseEntity<Password> addPassword(@RequestBody PasswordDto passwordDto, HttpServletRequest request) {
        User currentUser = userService.getByUsername(request.getUserPrincipal().getName());

        Password password = modelMapper.map(passwordDto, Password.class);
        password.setUser(currentUser);

        return new ResponseEntity<>(passwordService.addPassword(password), HttpStatus.CREATED);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Password> getById(@PathVariable("id")String encodedId) {
        Long decodedId = Long.parseLong(URLDecoder.decode(encodedId, StandardCharsets.UTF_8));

        return new ResponseEntity<>(passwordService.getById(decodedId), HttpStatus.OK);
    }

    @GetMapping("/getAllByUser")
    public Page<Password> getAllByUser(HttpServletRequest request, Pageable pageable) {
        User currentUser = userService.getByUsername(request.getUserPrincipal().getName());
        return passwordService.getAllByUser(currentUser, pageable);
    }

    @PutMapping("/update")
    public ResponseEntity<Password> updatePassword(@RequestBody PasswordDto passwordDto) {
        return new ResponseEntity<>(passwordService.updatePassword(modelMapper.map(passwordDto, Password.class)), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deletePassword(@PathVariable("id")String encodedId) {
        Long decodedId = Long.parseLong(URLDecoder.decode(encodedId, StandardCharsets.UTF_8));
        passwordService.deletePassword(decodedId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
