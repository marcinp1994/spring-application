package pl.marcin.it.springapplication.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.marcin.it.springapplication.exception.UserAlreadyExistException;
import pl.marcin.it.springapplication.model.user.User;
import pl.marcin.it.springapplication.repository.TokenRepository;
import pl.marcin.it.springapplication.repository.UserRepository;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MailService mailService;

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void addUserShouldReturnProperUser() throws MessagingException {
        //given
        User user = new User();
        user.setUsername("test");
        when(userRepository.save(any(User.class))).thenReturn(new User());

        //when
        User created = userService.addUser(user);

        //then
        assertThat(created.getUsername()).isSameAs(user.getUsername());
    }

    @Test
    public void getUserByNameShouldReturnProperUser(){
        //given
        String username = "test";
        User user = new User();
        user.setUsername(username);
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user));

        //when
        Optional<User> created = userService.getUserByName(username);

        //then
        assertTrue(created.isPresent());
        assertThat(created.get().getUsername()).isSameAs(user.getUsername());
    }

    @Test
    public void addUserShouldThrowUserAlreadyExistException(){
        //given
        User user = new User();
        user.setUsername("test");
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(new User()));
        when(userRepository.save(any(User.class))).thenReturn(new User());

        //when
        UserAlreadyExistException exception = assertThrows(
                UserAlreadyExistException.class,
                () -> userService.addUser(user));

        //then
        assertTrue(exception.getMessage().contains("exist"));
    }

    @Test
    public void getAllUsersShouldReturnProperSizeOfUsers() {
        //given
        List<User> userList = new ArrayList<>();
        User user1 = new User();
        user1.setUsername("test1");
        userList.add(user1);
        User user2 = new User();
        user2.setUsername("test2");
        userList.add(user2);
        when(userRepository.findAll()).thenReturn(userList);

        //when
        List<User> allUsers = userService.getAllUsers();

        //then
        assertNotNull(allUsers);
        assertThat(allUsers).hasSize(2);
    }

    @Test
    public void getAllUsersShouldReturnProperProperUsers() {
        //given
        List<User> userList = new ArrayList<>();
        User user1 = new User();
        user1.setUsername("test1");
        userList.add(user1);
        when(userRepository.findAll()).thenReturn(userList);

        //when
        List<User> allUsers = userService.getAllUsers();

        //then
        assertNotNull(allUsers);
        assertThat(allUsers).hasSize(1);
        assertEquals(user1.getUsername(), allUsers.get(0).getUsername());
    }

    @Test
    public void getAllUsersShouldReturnEmptyList() {
        //given
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        //when
        List<User> allUsers = userService.getAllUsers();

        //then
        assertNotNull(allUsers);
        assertThat(allUsers).hasSize(0);
    }

}