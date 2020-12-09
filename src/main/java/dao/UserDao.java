package dao;

import bean.User;
import dao.exception.DAOException;

import java.util.List;

public interface UserDao {
    User signIn(User user) throws DAOException;
    User signUp(User user) throws DAOException;
    User getUserByLogin(String login) throws DAOException;
    List<User> getUsers() throws DAOException;
}
