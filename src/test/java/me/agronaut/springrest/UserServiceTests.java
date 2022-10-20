package me.agronaut.springrest;


import me.agronaut.springrest.Model.User;
import me.agronaut.springrest.Service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringRestApplication.class)
public class UserServiceTests {
   @MockBean
    private UserService userSD;

    @Test
    public void testRegister() throws UserService.UserExistByEmailException {
        User testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");
        testUser.setPassword("testUser");
        testUser.setEmail("test@user.com");

        when(userSD.register(any(User.class))).thenReturn(testUser);

        Long insertedId = userSD.register(testUser).getId();

        assertNotNull(insertedId);
    }

    @Test
    public void testLogin() throws UserService.UserExistByEmailException, UserService.NotActiveUserException {
        User testUser = new User();
        testUser.setUsername("gaborka98");
        testUser.setPassword("asdasd123");

        when(userSD.login(testUser)).thenReturn(testUser).thenThrow(UserService.NotActiveUserException.class);
        when(userSD.login(null)).thenThrow(EntityNotFoundException.class);

        assertDoesNotThrow(() -> userSD.login(testUser));
        assertThrows(UserService.NotActiveUserException.class, () -> userSD.login(testUser));
        assertThrows(EntityNotFoundException.class,()-> userSD.login(null));
    }
}
