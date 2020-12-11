package service.impl;

import bean.Tour;
import bean.Order;
import bean.User;
import dao.TourDao;
import dao.OrderDao;
import dao.exception.DAOException;
import dao.factory.DAOFactory;
import service.TouragencyService;
import service.exception.ServiceException;

import java.util.List;

public class TouragencyServiceImpl implements TouragencyService { //service should check input params
    private final TourDao tourDao = DAOFactory.getInstance().getTourDAO();
    private final OrderDao orderDao = DAOFactory.getInstance().getOrderDAO();

    @Override
    public boolean addNewTour(Tour tour) throws ServiceException {
        if (tour.getTitle().equals("") || tour.getDescription().equals("") || tour.getCount() < 0) {
            return false;
        } else {
            try {
                tourDao.addTour(tour);
                return true;
            } catch (DAOException e) {
                throw new ServiceException(e);
            }
        }
    }

    @Override
    public boolean addEditedTour(Tour tour) throws ServiceException {
        if (tour.getTitle().equals("") || tour.getDescription().equals("") || tour.getCount() < 0 || tour.getId() < 0) {
            return false;
        } else {
            try {
                tourDao.editTour(tour);
                return true;
            } catch (DAOException e) {
                throw new ServiceException(e);
            }
        }
    }

    @Override
    public void deleteTour(Tour tour) throws ServiceException {
        try {
            tourDao.deleteTour(tour);
            orderDao.deleteOrdersByTour(tour);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Tour> getTourList() throws ServiceException {
        try {
            return tourDao.getTours();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Tour> searchTours(String request) throws ServiceException {
        try {
            return tourDao.searchTours(request);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Tour getTourById(String id) throws ServiceException {
        try {
            Tour tour = tourDao.getTourById(Integer.parseInt(id));
            if (tour.getId() == -1) {
                throw new ServiceException("No such tour with specified id");
            } else {
                return tour;
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void addNewOrder(Order order) throws ServiceException {
        try {
            orderDao.addOrder(order);
            Tour tour = order.getTour();
            tour.setCount(tour.getCount() - 1);
            tourDao.editTour(tour);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteOrder(Order order) throws ServiceException {
        try {
            orderDao.deleteOrder(order);
            Tour tour = order.getTour();
            tour.setCount(tour.getCount() + 1);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Order> getOrders() throws ServiceException {
        try {
            return orderDao.getOrders();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Order> getUserOrders(User user) throws ServiceException {
        try {
            return orderDao.getUserOrders(user);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void updateOrder(Order order) throws ServiceException {
        try {
            orderDao.updateOrder(order);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}
