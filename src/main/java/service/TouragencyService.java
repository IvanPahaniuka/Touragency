package service;

import bean.Tour;
import bean.Order;
import bean.User;
import service.exception.ServiceException;

import java.util.List;

public interface TouragencyService {
    boolean addNewTour(Tour tour) throws ServiceException;
    boolean addEditedTour(Tour tour) throws ServiceException;
    void deleteTour(Tour tour) throws ServiceException;
    List<Tour> getTourList() throws ServiceException;
    List<Tour> searchTours(String request) throws ServiceException;
    Tour getTourById(String id) throws ServiceException;
    void addNewOrder(Order order) throws ServiceException;
    List<Order> getOrders() throws ServiceException;
    List<Order> getUserOrders(User user) throws ServiceException;
    void deleteOrder(Order order) throws ServiceException;
    void updateOrder(Order order) throws ServiceException;
}
