package com.mayab.quality.integration;

import java.io.FileInputStream;
import java.sql.DriverManager;
import java.util.List;

import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.mayab.quality.loginunittest.dao.IDAOUser;
import com.mayab.quality.loginunittest.dao.UserMysqlDAO;
import com.mayab.quality.loginunittest.model.User;
import com.mayab.quality.loginunittest.service.UserService;

class UserServiceTest {

    private UserService service;

    @BeforeEach
    public void setup() throws Exception {
        IDAOUser dao = new UserMysqlDAO();
        service = new UserService(dao);

        // Configure DBUnit database connection
        IDatabaseConnection connection = new DatabaseConnection(
            DriverManager.getConnection("jdbc:mysql://localhost:3307/calidad2024", "root", "123456")
        );

        try {
            DatabaseOperation.CLEAN_INSERT.execute(connection, getDataSet());
        } catch (Exception e) {
            fail("Error in setup: " + e.getMessage());
        } finally {
            connection.close();
        }
    }

    private IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(new FileInputStream("src/resources/initDB.xml"));
    }

    @Test
    public void testCreateUser_HappyPath() {
        // Arrange
        String name = "hola";
        String email = "hola@gmail.com";
        String password = "pass1234";

        // Act
        User createdUser = service.createUser(name, email, password);

        // Assert
        assertNotNull(createdUser);
        assertEquals(name, createdUser.getName());
        assertEquals(email, createdUser.getEmail());
        assertNotNull(createdUser.getId());
    }

    @Test
    public void testCreateUser_UserAlreadyExists() {
        // Arrange
        String email = "existing@gmail.com";
        String password = "pass1234";

        // Simulate existing user via UserService
        service.createUser("existingUser", email, password);

        // Act
        User createdUser = service.createUser("newUser", email, password);

        // Assert
        assertNull(createdUser, "Expected null since the user already exists");
    }

    @Test
    public void testCreateUser_InvalidPassword() {
        // Arrange
        String name = "invalidUser";
        String email = "invalid@gmail.com";
        String password = "short"; // Invalid password length

        // Act
        User createdUser = service.createUser(name, email, password);

        // Assert
        assertNull(createdUser);
    }

    @Test
    public void testFindUserByEmail_HappyPath() {
        // Arrange
        String email = "findme@gmail.com";
        String name = "testUser";
        String password = "password1234";
        service.createUser(name, email, password);  // Use UserService

        // Act
        User foundUser = service.findUserByEmail(email);

        // Assert
        assertNotNull(foundUser);
        assertEquals(email, foundUser.getEmail());
        assertEquals(name, foundUser.getName());
        assertEquals(password, foundUser.getPassword());
    }

    @Test
    public void testFindAllUsers() {
        // Arrange
        User user1 = new User("User1", "user1@example.com", "pass1234");
        User user2 = new User("User2", "user2@example.com", "pass5678");

        service.createUser(user1.getName(), user1.getEmail(), user1.getPassword());
        service.createUser(user2.getName(), user2.getEmail(), user2.getPassword());

        // Act
        List<User> users = service.findAllUsers();

        // Assert
        assertNotNull(users);
        assertTrue(users.size() >= 2);

        // Check if the list contains the added users
        assertTrue(users.stream().anyMatch(user -> 
            user.getName().equals("User1") && 
            user.getEmail().equals("user1@example.com") &&
            user.getPassword().equals("pass1234")
        ));
        
        assertTrue(users.stream().anyMatch(user -> 
            user.getName().equals("User2") && 
            user.getEmail().equals("user2@example.com") &&
            user.getPassword().equals("pass5678")
        ));
    }

    @Test
    public void testFindUserById_HappyPath() {
        // Arrange
        User user = service.createUser("UserById", "userbyid@example.com", "pass1233");
        
        // Check if user creation succeeded
        assertNotNull(user);
        int userId = user.getId();
        assertNotNull(userId);

        // Act
        User foundUser = service.findUserById(userId);

        // Assert
        assertNotNull(foundUser);
        assertEquals("UserById", foundUser.getName());
        assertEquals("userbyid@example.com", foundUser.getEmail());
    }

    @Test
    public void testDeleteUser() {
        // Arrange
        User user = service.createUser("deleteUser", "delete@gmail.com", "password1234");
        int userId = user.getId();

        // Act
        boolean isDeleted = service.deleteUser(userId);

        // Assert
        assertTrue(isDeleted);
        assertNull(service.findUserById(userId));
    }
}
