package dao;

import bean.Tour;
import dao.exception.DAOException;

import java.util.List;

public interface TourDao {
    void addTour(Tour tour) throws DAOException;
    void editTour(Tour tour) throws DAOException;
    void deleteTour(Tour tour) throws DAOException;
    Tour getTourById(int id) throws DAOException;
    List<Tour> getTours() throws DAOException;
    List<Tour> searchTours(String request) throws DAOException;
}
