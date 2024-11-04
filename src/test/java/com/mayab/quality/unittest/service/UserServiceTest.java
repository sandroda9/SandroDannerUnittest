package com.mayab.quality.unittest.service;

import com.mayab.quality.loginunittest.dao.IDAOUser;
import com.mayab.quality.loginunittest.model.User;
import com.mayab.quality.loginunittest.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserService userService;
    private IDAOUser daoMock;
    private User mockUser;

    @Before
    public void setUp() {
        // Mocking the DAO interface
        daoMock = mock(IDAOUser.class);
        userService = new UserService(daoMock);

        // Creating a mock user
        mockUser = new User("Test User", "test@example.com", "password123");
    }

    @Test
    public void testCreateUserHappyPath() {
        when(daoMock.findUserByEmail(mockUser.getEmail())).thenReturn(null);
        when(daoMock.save(any(User.class))).thenReturn(1);

        User result = userService.createUser(mockUser.getName(), mockUser.getEmail(), mockUser.getPassword());

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(daoMock, times(1)).save(any(User.class));
    }

    @Test
    public void testCreateUserDuplicatedEmail() {
        when(daoMock.findUserByEmail(mockUser.getEmail())).thenReturn(mockUser);

        User result = userService.createUser(mockUser.getName(), mockUser.getEmail(), mockUser.getPassword());

        assertNotNull(result);
        assertEquals(mockUser.getEmail(), result.getEmail());
        verify(daoMock, never()).save(any(User.class));
    }

    @Test
    public void testUpdateUserPassword() {
        when(daoMock.findById(anyInt())).thenReturn(mockUser);
        mockUser.setPassword("newPassword123");
        when(daoMock.updateUser(any(User.class))).thenReturn(mockUser);

        User result = userService.updateUser(mockUser);

        assertNotNull(result);
        assertEquals("newPassword123", result.getPassword());
    }

    @Test
    public void testDeleteUser() {
        when(daoMock.deleteById(anyInt())).thenReturn(true);

        boolean result = userService.deleteUser(1);

        assertTrue(result);
        verify(daoMock, times(1)).deleteById(1);
    }

    @Test
    public void testFindUserByEmailHappyPath() {
        when(daoMock.findUserByEmail(mockUser.getEmail())).thenReturn(mockUser);

        User result = userService.findUserByEmail(mockUser.getEmail());

        assertNotNull(result);
        assertEquals(mockUser.getEmail(), result.getEmail());
    }

    @Test
    public void testFindUserByEmailNotFound() {
        when(daoMock.findUserByEmail("nonexistent@example.com")).thenReturn(null);

        User result = userService.findUserByEmail("nonexistent@example.com");

        assertNull(result);
    }

    @Test
    public void testFindAllUsers() {
        List<User> mockUsers = Arrays.asList(mockUser, new User("Another User", "another@example.com", "password456"));
        when(daoMock.findAll()).thenReturn(mockUsers);

        List<User> result = userService.findAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(daoMock, times(1)).findAll();
    }
}
