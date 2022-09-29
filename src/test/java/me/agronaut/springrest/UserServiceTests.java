package me.agronaut.springrest;


import me.agronaut.springrest.Model.Role;
import me.agronaut.springrest.Model.User;
import me.agronaut.springrest.Service.EmailService;
import me.agronaut.springrest.Service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import javax.persistence.EntityNotFoundException;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

//@DataJpaTest
//@ContextConfiguration(classes = {UserService.class, EmailService.class})
//@AutoConfigurationPackage
@SpringBootTest
public class UserServiceTests {
    @Autowired
    UserService userSD;
    // private static final Logger log = LoggerFactory.getLogger(UserServiceTests.class);

    @Test
    @Order(1)
    public void testRegister() throws UserService.UserExistByEmailException {
        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("testUser");
        testUser.setEmail("test@user.com");

        Long insertedId = userSD.register(testUser).getId();

        assertNotNull(insertedId);
    }

    @Test
    @Order(2)
    public void testLogin() throws UserService.UserExistByEmailException, UserService.NotActiveUserException {
        User testUser = new User();
        testUser.setUsername("gaborka98");
        testUser.setPassword("asdasd123");

        assertDoesNotThrow(() -> userSD.login(testUser));
        assertThrows(EntityNotFoundException.class,()-> userSD.login(null));
        assertThrows(EntityNotFoundException.class, ()->userSD.login(new User()));

        User loggedin = userSD.login(testUser);

        System.out.println(loggedin.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
    }
}
