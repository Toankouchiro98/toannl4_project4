package com.example.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

public class UserTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private final UserRepository users = mock(UserRepository.class);

    @Mock
    private final CartRepository cartRepository = mock(CartRepository.class);

    private final BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    public static User saveUser(){
        User user = new User();

        user.setId(1);
        user.setUsername("toannl45");
        user.setPassword("toannl4");

        return user;
    }
    
    @Test
    public void createUserHappyPath() throws Exception{
        // when(encoder.encode("Password")).thenReturn("HashedPassword");

        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("toannl45");
        userRequest.setPassword("toannl4");
        userRequest.setConfirmPassword("toannl4");

        ResponseEntity<User> response = userController.createUser(userRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("toannl45", user.getUsername());
        // assertEquals("HashedPassword", user.getPassword());
    }

    @Test
    public void findByIdFoundTest(){
        User user = saveUser();

        when(users.findById(1L)).thenReturn(Optional.of(user));
        ResponseEntity<User> response = userController.findById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void findByIdNotFoundTest(){
        ResponseEntity<User> response = userController.findById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void findByUsernameFoundTest(){
        User user = saveUser();

        when(users.findByUsername("toannl45")).thenReturn(user);
        ResponseEntity<User> response = userController.findByUserName("toannl45");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        assertEquals(1L, user.getId());
        assertEquals("toannl45", user.getUsername());
        assertEquals("toannl4", user.getPassword());
    }

    @Test
    public void findByUsernameNotFoundTest(){
        ResponseEntity<User> response = userController.findByUserName("toannl45");

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}