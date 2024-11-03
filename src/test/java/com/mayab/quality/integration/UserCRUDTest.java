package com.mayab.quality.integration;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mayab.quality.loginunittest.dao.UserMysqlDAO;
import com.mayab.quality.loginunittest.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

class UserCRUDTest {

    private static Connection connection;
    private UserMysqlDAO daoMySql;

    @BeforeAll
    static void beforeAll() throws Exception {
        System.out.println("Before all CRUD test cases");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/calidad2024", "root", "123456");
    }

    @AfterAll
    static void afterAll() throws Exception {
        System.out.println("After all CRUD test cases");
        if (connection != null) {
            connection.close();
        }
    }

    @BeforeEach
    void setup() throws Exception {
        System.out.println("Before each CRUD test");
        daoMySql = new UserMysqlDAO(); // Using the no-argument constructor

        // Clean the table before each test
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM usuarios");
            stmt.execute("ALTER TABLE usuarios AUTO_INCREMENT = 1");
        }
    }

    @AfterEach
    void cleanup() {
        System.out.println("After each CRUD test");
    }

    @Test
    void testCreateUser() {
        User user = new User("testUser", "test@correo.com", "password123");
        int newId = daoMySql.save(user);

        assertThat(newId, is(not(0)));
        System.out.println("User created with ID: " + newId);
    }

    @Test
    void testReadUser() {
        User user = new User("username2", "correo2@correo.com", "password123");
        int newId = daoMySql.save(user);

        User retrievedUser = daoMySql.findById(newId);

        assertThat(retrievedUser, is(notNullValue()));
        assertThat(retrievedUser.getName(), is("username2"));
        assertThat(retrievedUser.getEmail(), is("correo2@correo.com"));
    }

    @Test
    void testUpdateUser() {
        User user = new User("username2", "correo2@correo.com", "password123456789");
        int newId = daoMySql.save(user);

        user.setId(newId);
        user.setName("updatedName");
        daoMySql.updateUser(user);

        User updatedUser = daoMySql.findById(newId);
        assertThat(updatedUser.getName(), is("updatedName"));
    }

    @Test
    void testDeleteUser() {
        User user = new User("username2", "correo2@correo.com", "password123");
        int newId = daoMySql.save(user);

        boolean isDeleted = daoMySql.deleteById(newId);

        assertThat(isDeleted, is(true));

        User deletedUser = daoMySql.findById(newId);
        assertThat(deletedUser, is(nullValue()));
    }
}
