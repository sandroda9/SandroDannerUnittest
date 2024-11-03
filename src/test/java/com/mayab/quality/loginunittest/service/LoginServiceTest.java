package com.mayab.quality.loginunittest.service;

// Import mockito
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.mayab.quality.loginunittest.dao.IDAOUser;
import com.mayab.quality.loginunittest.model.User;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;

// Import hamcrest
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoginServiceTest {

    private User mockUser;
    private IDAOUser mockDao;
    private LoginService loginservice;
    
    @BeforeEach
    void setUp() throws Exception {
        mockUser = mock(User.class);   //dao
        mockDao = mock(IDAOUser.class);  //user
        loginservice = new LoginService(mockDao); //service
    }

    @Test
    void testLoginFailure() {
        // Arrange
        String username = "john_doe";
        String password = "wrongPassword";
        
        // Mock the behavior of user and dao
        when(mockUser.getPassword()).thenReturn("password123");
        when(mockDao.findByUserName(username)).thenReturn(mockUser);
        
        // Act
        boolean loginResult = loginservice.loging(username, password);
        		// boolean res = service.login("name", "123");
        
        // Assert
        assertFalse(loginResult, "Login should fail with incorrect credentials.");
        		//assertThat(res,is(true));
    }
}
