package dao.impl;

import bean.Book;
import bean.Order;
import bean.User;
import bean.enums.OrderStatus;
import dao.OrderDao;
import dao.exception.DAOException;
import dao.factory.DAOFactory;
import dao.pool.ConnectionPool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLOrderDao implements OrderDao {
    @Override
    public void addOrder(Order order) throws DAOException {
        ConnectionPool pool = null;
        Connection connection = null;
        try {
            pool = ConnectionPool.getInstance();
            connection = pool.getConnection();

            var sql = "INSERT INTO orders (user_login, book_id, order_status) VALUES (?, ?, ?)";
            var statement = connection.prepareStatement(sql);
            statement.setString(1, order.getUser().getLogin());
            statement.setInt(2, order.getBook().getId());
            statement.setInt(3, order.getStatus().ordinal());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            if (pool != null)
                pool.returnConnection(connection);
        }
    }

    @Override
    public void deleteOrder(Order order) throws DAOException {
        ConnectionPool pool = null;
        Connection connection = null;
        try {
            pool = ConnectionPool.getInstance();
            connection = pool.getConnection();

            var sql = "DELETE FROM orders WHERE user_login=? AND book_id=?";
            var statement = connection.prepareStatement(sql);
            statement.setString(1, order.getUser().getLogin());
            statement.setInt(2, order.getBook().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            if (pool != null)
                pool.returnConnection(connection);
        }
    }

    @Override
    public void deleteOrdersByBook(Book book) throws DAOException {
        ConnectionPool pool = null;
        Connection connection = null;
        try {
            pool = ConnectionPool.getInstance();
            connection = pool.getConnection();

            var sql = "DELETE FROM orders WHERE book_id=?";
            var statement = connection.prepareStatement(sql);
            statement.setInt(1, book.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            if (pool != null)
                pool.returnConnection(connection);
        }
    }

    @Override
    public void updateOrder(Order order) throws DAOException {
        ConnectionPool pool = null;
        Connection connection = null;
        try {
            pool = ConnectionPool.getInstance();
            connection = pool.getConnection();

            var sql = "UPDATE orders SET order_status=? WHERE user_login=? AND book_id=?";
            var statement = connection.prepareStatement(sql);
            statement.setInt(1, order.getStatus().ordinal());
            statement.setString(2, order.getUser().getLogin());
            statement.setInt(3, order.getBook().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            if (pool != null)
                pool.returnConnection(connection);
        }
    }

    @Override
    public List<Order> getOrders() throws DAOException {
        List<Order> orders = new ArrayList<>();
        ConnectionPool pool = null;
        Connection connection = null;
        try {
            pool = ConnectionPool.getInstance();
            connection = pool.getConnection();

            var sql = "SELECT * FROM orders";
            var statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                var order = new Order();
                var book = DAOFactory.getInstance().getBookDAO().getBookById(rs.getInt("book_id"));
                var user = DAOFactory.getInstance().getUserDAO().getUserByLogin(rs.getString("user_login"));
                order.setBook(book);
                order.setUser(user);
                order.setStatus(OrderStatus.values()[rs.getInt("order_status")]);
                orders.add(order);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            if (pool != null)
                pool.returnConnection(connection);
        }
        return orders;
    }

    @Override
    public List<Order> getUserOrders(User user) throws DAOException {
        List<Order> orders = new ArrayList<>();
        ConnectionPool pool = null;
        Connection connection = null;
        try {
            pool = ConnectionPool.getInstance();
            connection = pool.getConnection();

            var sql = "SELECT * FROM orders WHERE user_login=?";
            var statement = connection.prepareStatement(sql);
            statement.setString(1, user.getLogin());
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                var order = new Order();
                var book = DAOFactory.getInstance().getBookDAO().getBookById(rs.getInt("book_id"));
                user = DAOFactory.getInstance().getUserDAO().getUserByLogin(rs.getString("user_login"));
                order.setBook(book);
                order.setUser(user);
                order.setStatus(OrderStatus.values()[rs.getInt("order_status")]);
                orders.add(order);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            if (pool != null)
                pool.returnConnection(connection);
        }
        return orders;
    }
}
