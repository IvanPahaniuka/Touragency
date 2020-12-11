package service.impl;

import bean.User;
import dao.UserDao;
import dao.exception.DAOException;
import dao.factory.DAOFactory;
import service.ClientService;
import service.exception.ServiceException;

import java.util.List;

public class ClientServiceImpl implements ClientService {
    private final UserDao userDao = DAOFactory.getInstance().getUserDAO();

    private boolean isPasswordCorrect(User user) throws ServiceException {
        try {
            List<User> users = userDao.getUsers();
            User comparedUser = users.stream()
                    .filter(u -> u.getLogin().equals(user.getLogin()))
                    .findFirst().orElse(null);
            return (comparedUser == null) ? false : comparedUser.getPassword().equals(user.getPassword());
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    private boolean isLoginUnique(User user) throws ServiceException {
        try {
            List<User> users = userDao.getUsers();
            return users.stream().noneMatch(u -> u.getLogin().equals(user.getLogin()));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public User signIn(User user) throws ServiceException {
        if (user.getLogin().equals("")) {
            throw new ServiceException("Login can't be empty");
        } else {
            try {
                if (isPasswordCorrect(user)) {
                    return userDao.signIn(user);
                } else {
                    return null;
                }
            } catch (DAOException e) {
                throw new ServiceException(e);
            }
        }
    }

    @Override
    public User signUp(User user)
            throws ServiceException {
        if (user.getLogin().equals("") || user.getPassword().equals("") || user.getFirstName().equals("") ||
                user.getLastName().equals("")) {
            throw new ServiceException("Fields can't be empty");
        } else {
            try {
                if (isLoginUnique(user)) {
                    return userDao.signUp(user);
                } else {
                    return null;
                }
            } catch (DAOException e) {
                throw new ServiceException(e);
            }
        }
    }

    @Override
    public User getUserByLogin(String login) throws ServiceException {
        try {
            User user = userDao.getUserByLogin(login);
            if (user.getId() == -1) {
                throw new ServiceException("No such user with specified login");
            } else {
                return user;
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}
