package com.mayab.quality.loginunittest.dao;

import com.mayab.quality.loginunittest.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserMysqlDAO implements IDAOUser {

    public Connection getConnectionMySQL() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3307/calidad2024", "root", "123456");
        } catch (Exception e) {
            System.out.println(e);
        }
        return con;
    }

    @Override
    public User findByUserName(String name) {
        Connection connection = getConnectionMySQL();
        User result = null;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from usuarios WHERE name = ?");
            preparedStatement.setString(1, name);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                boolean isLogged = rs.getBoolean("isLogged");

                result = new User(username, email, password);
                result.setId(id);
                result.setLogged(isLogged);
            }

            connection.close();
            rs.close();
            preparedStatement.close();

        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    @Override
    public int save(User user) {
        Connection connection = getConnectionMySQL();
        int result = -1;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO usuarios(name, email, password, isLogged) VALUES(?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setBoolean(4, user.isLogged());

            if (preparedStatement.executeUpdate() >= 1) {
                try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                    if (rs.next()) {
                        result = rs.getInt(1);
                    }
                }
            }
            connection.close();
            preparedStatement.close();

        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    @Override
    public User findUserByEmail(String email) {
        Connection connection = getConnectionMySQL();
        User result = null;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from usuarios WHERE email = ?");
            preparedStatement.setString(1, email);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("name");
                String emailUser = rs.getString("email");
                String password = rs.getString("password");
                boolean isLogged = rs.getBoolean("isLogged");

                result = new User(username, emailUser, password);
                result.setId(id);
                result.setLogged(isLogged);
            }

            rs.close();
            preparedStatement.close();
            connection.close();

        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    @Override
    public List<User> findAll() {
        Connection connection = getConnectionMySQL();
        List<User> users = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from usuarios");
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                boolean log = rs.getBoolean("isLogged");

                User user = new User(name, email, password);
                user.setId(id);
                user.setLogged(log);
                users.add(user);
            }

            connection.close();
            rs.close();
            preparedStatement.close();

        } catch (Exception e) {
            System.out.println(e);
        }
        return users;
    }

    @Override
    public User findById(int id) {
        Connection connection = getConnectionMySQL();
        User result = null;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from usuarios WHERE id = ?");
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int idUser = rs.getInt("id");
                String username = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                boolean isLogged = rs.getBoolean("isLogged");

                result = new User(username, email, password);
                result.setId(idUser);
                result.setLogged(isLogged);
            }

            connection.close();
            rs.close();
            preparedStatement.close();

        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    @Override
    public boolean deleteById(int id) {
        Connection connection = getConnectionMySQL();
        boolean result = false;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM usuarios WHERE id = ?");
            preparedStatement.setInt(1, id);

            if (preparedStatement.executeUpdate() >= 1) {
                result = true;
            }

            connection.close();
            preparedStatement.close();

        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    @Override
    public User updateUser(User userNew) {
        Connection connection = getConnectionMySQL();
        User result = null;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE usuarios SET name = ?, password = ? WHERE id = ?");
            preparedStatement.setString(1, userNew.getName());
            preparedStatement.setString(2, userNew.getPassword());
            preparedStatement.setInt(3, userNew.getId());

            if (preparedStatement.executeUpdate() >= 1) {
                result = userNew;
            }

            connection.close();
            preparedStatement.close();

        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }
}
