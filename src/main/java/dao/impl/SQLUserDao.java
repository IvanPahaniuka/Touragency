package dao.impl;

import bean.User;
import bean.enums.UserRole;
import dao.UserDao;
import dao.exception.DAOException;
import dao.pool.ConnectionPool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SQLUserDao implements UserDao {

    @Override
    public User signIn(User user) throws DAOException {
        ConnectionPool pool = null;
        Connection connection = null;
        try {
            pool = ConnectionPool.getInstance();
            connection = pool.getConnection();

            var sql = "SELECT * FROM users WHERE login=? AND password=?";
            var statement = connection.prepareStatement(sql);
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setRole(UserRole.values()[rs.getInt("role")]);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            if (pool != null)
                pool.returnConnection(connection);
        }
        return user;
    }

    @Override
    public User signUp(User user) throws DAOException {
        ConnectionPool pool = null;
        Connection connection = null;
        try {
            pool = ConnectionPool.getInstance();
            connection = pool.getConnection();

            var sql = "INSERT INTO users (first_name, last_name, login, password, role) VALUES (?, ?, ?, ?, ?)";
            var statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getLogin());
            statement.setString(4, user.getPassword());
            statement.setInt(5, user.getRole().ordinal());
            statement.executeUpdate();

            var generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            if (pool != null)
                pool.returnConnection(connection);
        }
        return user;
    }


    @Override
    public User getUserByLogin(String login) throws DAOException {
        User user = new User();
        ConnectionPool pool = null;
        Connection connection = null;
        try {
            pool = ConnectionPool.getInstance();
            connection = pool.getConnection();

            var sql = "SELECT * FROM users WHERE login=?";
            var statement = connection.prepareStatement(sql);
            statement.setString(1, login);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("password"));
                user.setRole(UserRole.values()[rs.getInt("role")]);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            if (pool != null)
                pool.returnConnection(connection);
        }
        return user;
    }


    @Override
    public List<User> getUsers() throws DAOException {
        List<User> users = new ArrayList<>();
        ConnectionPool pool = null;
        Connection connection = null;
        try {
            pool = ConnectionPool.getInstance();
            connection = pool.getConnection();

            var sql = "SELECT * FROM users";
            var statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                var user = new User();
                user.setId(rs.getInt("id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("password"));
                user.setRole(UserRole.values()[rs.getInt("role")]);
                users.add(user);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            if (pool != null)
                pool.returnConnection(connection);
        }
        return users;
    }
}
