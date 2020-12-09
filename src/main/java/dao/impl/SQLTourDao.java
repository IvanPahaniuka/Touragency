package dao.impl;

import bean.Tour;
import dao.TourDao;
import dao.exception.DAOException;
import dao.pool.ConnectionPool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLTourDao implements TourDao {
    @Override
    public void addTour(Tour tour) throws DAOException {
        ConnectionPool pool = null;
        Connection connection = null;
        try {
            pool = ConnectionPool.getInstance();
            connection = pool.getConnection();

            var sql = "INSERT INTO tours (author, title, count) VALUES (?, ?, ?)";
            var statement = connection.prepareStatement(sql);
            statement.setString(1, tour.getDescription());
            statement.setString(2, tour.getTitle());
            statement.setInt(3, tour.getCount());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            if (pool != null)
                pool.returnConnection(connection);
        }
    }

    @Override
    public void editTour(Tour tour) throws DAOException {
        ConnectionPool pool = null;
        Connection connection = null;
        try {
            pool = ConnectionPool.getInstance();
            connection = pool.getConnection();

            var sql = "UPDATE tours SET description=?, title=?, count=? WHERE id=?";
            var statement = connection.prepareStatement(sql);
            statement.setString(1, tour.getDescription());
            statement.setString(2, tour.getTitle());
            statement.setInt(3, tour.getCount());
            statement.setInt(4, tour.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            if (pool != null)
                pool.returnConnection(connection);
        }
    }

    @Override
    public void deleteTour(Tour tour) throws DAOException {
        ConnectionPool pool = null;
        Connection connection = null;
        try {
            pool = ConnectionPool.getInstance();
            connection = pool.getConnection();

            var sql = "DELETE FROM tours WHERE id=?";
            var statement = connection.prepareStatement(sql);
            statement.setInt(1, tour.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            if (pool != null)
                pool.returnConnection(connection);
        }
    }

    @Override
    public Tour getTourById(int id) throws DAOException {
        var tour = new Tour();
        ConnectionPool pool = null;
        Connection connection = null;
        try {
            pool = ConnectionPool.getInstance();
            connection = pool.getConnection();

            var sql = "SELECT * FROM tours WHERE id=?";
            var statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                tour.setId(rs.getInt("id"));
                tour.setTitle(rs.getString("title"));
                tour.setDescription(rs.getString("description"));
                tour.setCount(rs.getInt("count"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            if (pool != null)
                pool.returnConnection(connection);
        }
        return tour;
    }

    @Override
    public List<Tour> getTours() throws DAOException {
        List<Tour> tours = new ArrayList<>();
        ConnectionPool pool = null;
        Connection connection = null;
        try {
            pool = ConnectionPool.getInstance();
            connection = pool.getConnection();

            var sql = "SELECT * FROM tours";
            var statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                var tour = new Tour();
                tour.setId(rs.getInt("id"));
                tour.setDescription(rs.getString("description"));
                tour.setTitle(rs.getString("title"));
                tour.setCount(rs.getInt("count"));
                tours.add(tour);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            if (pool != null)
                pool.returnConnection(connection);
        }
        return tours;
    }

    @Override
    public List<Tour> searchTours(String request) throws DAOException {
        List<Tour> tours = new ArrayList<>();
        ConnectionPool pool = null;
        Connection connection = null;
        try {
            pool = ConnectionPool.getInstance();
            connection = pool.getConnection();

            var sql = "SELECT * FROM tours WHERE title LIKE %'" + request + "%'";
            var statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                var tour = new Tour();
                tour.setId(rs.getInt("id"));
                tour.setDescription(rs.getString("description"));
                tour.setTitle(rs.getString("title"));
                tour.setCount(rs.getInt("count"));
                tours.add(tour);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            if (pool != null)
                pool.returnConnection(connection);
        }
        return tours;
    }
}
