package me.agronaut.springrest;


import me.agronaut.springrest.Model.User;
import me.agronaut.springrest.Service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = {UserService.class})
@AutoConfigurationPackage
@SpringBootTest
public class UserServiceTests {
    @Autowired
    UserService userSD;
    // private static final Logger log = LoggerFactory.getLogger(UserServiceTests.class);
    @Test
    public void testRegister() {
        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("testUser");
        testUser.setEmail("test@user.com");

        Long insertedId = userSD.save(testUser).getId();

        assertNotNull(insertedId);
    }

    @Test
    public void testLogin() {
        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("testUser");

        assertDoesNotThrow(() -> userSD.login(testUser));
        assertThrows(EntityNotFoundException.class,()-> userSD.login(null));
        assertThrows(EntityNotFoundException.class, ()->userSD.login(new User()));


    }
}
