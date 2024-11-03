package com.mayab.quality.unittest;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.mayab.quality.loginunittest.dao.UserMysqlDAO;
import com.mayab.quality.loginunittest.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

class UserMysqlDAOTest {

    private UserMysqlDAO dao;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    void setup() throws Exception {
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        dao = spy(new UserMysqlDAO() {
            @Override
            public Connection getConnectionMySQL() {
                return mockConnection;
            }
        });

        when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @Test
    void testFindByUserName() throws Throwable {
        try {
            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt(1)).thenReturn(1);
            when(mockResultSet.getString(2)).thenReturn("username2");
            when(mockResultSet.getString(3)).thenReturn("correo2@correo.com");
            when(mockResultSet.getString(4)).thenReturn("password123");
            when(mockResultSet.getBoolean(5)).thenReturn(false);

            User user = dao.findByUserName("username2");

            assertThat(user, is(notNullValue()));
            assertThat(user.getName(), is("username2"));
            assertThat(user.getEmail(), is("correo2@correo.com"));
            assertThat(user.getPassword(), is("password123"));

            verify(mockPreparedStatement).setString(1, "username2");
            verify(mockPreparedStatement).executeQuery();

            System.out.println("testFindByUserName passed.");
        } catch (AssertionError | Exception e) {
            System.out.println("testFindByUserName failed: " + e.getMessage());
            throw e;
        }
    }

    @Test
    void testSaveUser() throws Throwable {
        try {
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);
            when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt(1)).thenReturn(42);

            User user = new User("testUser", "test@correo.com", "password123");
            int newId = dao.save(user);

            assertThat(newId, is(42));

            verify(mockPreparedStatement).setString(1, "testUser");
            verify(mockPreparedStatement).setString(2, "test@correo.com");
            verify(mockPreparedStatement).setString(3, "password123");
            verify(mockPreparedStatement).setBoolean(4, false);
            verify(mockPreparedStatement).executeUpdate();

            System.out.println("testSaveUser passed.");
        } catch (AssertionError | Exception e) {
            System.out.println("testSaveUser failed: " + e.getMessage());
            throw e;
        }
    }

    @Test
    void testUpdateUser() throws Throwable {
        try {
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            User user = new User("testUser", "test@correo.com", "password123");
            user.setId(1);
            user.setName("updatedUser");

            boolean isUpdated = dao.updateUser(user) != null;

            assertThat(isUpdated, is(true));

            verify(mockPreparedStatement).setString(1, "updatedUser");
            verify(mockPreparedStatement).setString(2, "password123");
            verify(mockPreparedStatement).setInt(3, 1);
            verify(mockPreparedStatement).executeUpdate();

            System.out.println("testUpdateUser passed.");
        } catch (AssertionError | Exception e) {
            System.out.println("testUpdateUser failed: " + e.getMessage());
            throw e;
        }
    }

    @Test
    void testDeleteUser() throws Throwable {
        try {
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            boolean isDeleted = dao.deleteById(1);

            assertThat(isDeleted, is(true));

            verify(mockPreparedStatement).setInt(1, 1);
            verify(mockPreparedStatement).executeUpdate();

            System.out.println("testDeleteUser passed.");
        } catch (AssertionError | Exception e) {
            System.out.println("testDeleteUser failed: " + e.getMessage());
            throw e;
        }
    }
}
