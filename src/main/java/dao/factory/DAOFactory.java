package dao.factory;

import dao.TourDao;
import dao.OrderDao;
import dao.UserDao;
import dao.impl.SQLTourDao;
import dao.impl.SQLOrderDao;
import dao.impl.SQLUserDao;

public class DAOFactory {
    private static final DAOFactory instance = new DAOFactory();

    private final TourDao sqlTourImpl = new SQLBookDao();
    private final UserDao sqlUserImpl = new SQLUserDao();
    private final OrderDao sqlOrderImpl = new SQLOrderDao();

    private DAOFactory() {}

    public static DAOFactory getInstance() {
        return instance;
    }

    public TourDao getTourDAO() {
        return sqlTourImpl;
    }

    public UserDao getUserDAO() {
        return sqlUserImpl;
    }

    public OrderDao getOrderDAO() {
        return sqlOrderImpl;
    }
}
