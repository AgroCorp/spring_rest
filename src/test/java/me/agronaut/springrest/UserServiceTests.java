package me.agronaut.springrest;

import lombok.extern.log4j.Log4j2;
import me.agronaut.springrest.Model.User;
import me.agronaut.springrest.Service.UserService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
@Log4j2
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

        log.info("\n\t[insertedId]\t{}", insertedId);
        Assert.notNull(insertedId, "Id is not null after insert");
    }
}
