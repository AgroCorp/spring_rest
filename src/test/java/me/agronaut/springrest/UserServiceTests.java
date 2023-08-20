package me.agronaut.springrest;


import me.agronaut.springrest.model.User;
import me.agronaut.springrest.model.UserDto;
import me.agronaut.springrest.repository.UserRepository;
import me.agronaut.springrest.service.EmailService;
import me.agronaut.springrest.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringRestApplication.class)
class UserServiceTests {
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private EmailService emailSD;

    private final User testUser = new User();

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private UserService userSD;

    @Test
    void testRegister() throws UserService.UserExistByEmailException {
        // new user does not exist
        testUser.setPassword("testPassword");
        testUser.setEmail("test@user.com");
        testUser.setId(1L);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userRepository.existsUserByEmail("test@user.com")).thenReturn(false);
        doNothing().when(emailSD).sendEmail(anyString(), anyString(), anyString(), anyString());

        Long insertedId = userSD.register(testUser).getId();
        assertNotNull(insertedId);

        // new user exist
        when(userRepository.existsUserByEmail("test@user.com")).thenReturn(true);
        assertThrows(UserService.UserExistByEmailException.class, () -> userSD.register(testUser));
    }

    @Test
    void testActivate() {
        testUser.setId(1L);
        doReturn(testUser).when(userRepository).save(any(User.class));
        doReturn(testUser).when(userRepository).getUserById(anyLong());

        userSD.activate(testUser.getId());
        assertThrows(EntityNotFoundException.class, () -> userSD.activate(null));


        verify(userRepository, times(1)).save(nullable(User.class));
        verify(userRepository, times(2)).getUserById(nullable(Long.class));
    }

    @Test
    void testLogin() {
        testUser.setActive(true);
        testUser.setUsername("testUser");
        testUser.setPassword("testPassword");

        User mockUser = new User();
        mockUser.setPassword(encoder.encode("testPassword"));
        mockUser.setActive(true);
        mockUser.setUsername("testUser");
        when(userRepository.getUserByUsername(anyString())).
                thenReturn(Optional.of(mockUser));

        // everything ok
        assertDoesNotThrow(() -> userSD.login(testUser));

        // not active user
        mockUser.setActive(false);
        assertThrows(UserService.NotActiveUserException.class, () -> userSD.login(testUser));
        mockUser.setActive(true);

        // null user
        assertThrows(EntityNotFoundException.class, () -> userSD.login(null));

        // bad password
        mockUser.setPassword(encoder.encode("testPasswordBad"));
        assertThrows(EntityNotFoundException.class, () -> userSD.login(testUser));

        // user not presented
        doReturn(Optional.empty()).when(userRepository).getUserByUsername(anyString());
        assertThrows(EntityNotFoundException.class, () -> userSD.login(new User()));
    }

    @Test
    void testGetById() {
        // null parameter
        assertNull(userSD.getById(null));
        // presented user
        doReturn(testUser).when(userRepository).getUserById(anyLong());
        assertNotNull(userSD.getById(2L));

        // not presented user
        doReturn(null).when(userRepository).getUserById(anyLong());
        assertNull(userSD.getById(1L));
    }

    @Test
    void testGetByUsername() {
        // null parameter
        assertThrows(EntityNotFoundException.class, () -> userSD.getByUsername(null));
        assertThrows(EntityNotFoundException.class, () -> userSD.getByUsername(""));

        // presented user
        doReturn(Optional.of(testUser)).when(userRepository).getUserByUsername(anyString());
        assertNotNull(userSD.getByUsername("laci"));

        // not presented user
        doReturn(Optional.empty()).when(userRepository).getUserByUsername(anyString());
        assertThrows(EntityNotFoundException.class, () -> userSD.getByUsername("jani"));
    }

    @Test
    void testGetAllByRole() {
        assertTrue(userSD.getAllByRole(null).isEmpty());
        assertTrue(userSD.getAllByRole("").isEmpty());

        List<User> mockList = new LinkedList<>();
        mockList.add(new User());
        mockList.add(new User());
        mockList.add(new User());
        doReturn(mockList).when(userRepository).findAllUserByRoleName(anyString());
        List<UserDto> res = userSD.getAllByRole("ADMIN");

        assertNotNull(res);
        assertFalse(res.isEmpty());
        assertEquals(3, res.size());
    }

    @Test
    void testSetNewPassword() {
        User mockUser = new User();
        mockUser.setId(12L);
        doReturn(mockUser).when(userRepository).getUserById(anyLong());

        // good
        userSD.setNewPassword("newPassword", 12L);


        assertThrows(NoSuchElementException.class, () -> {
            // check null
            userSD.setNewPassword(null, 12L);
            userSD.setNewPassword("", 12L);
            userSD.setNewPassword("newPassword", null);

            doReturn(null).when(userRepository).getUserById(anyLong());
            // user not found by id
            userSD.setNewPassword("newPassword", 12L);
        });
    }
}
