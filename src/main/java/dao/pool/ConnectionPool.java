package dao.pool;

import dao.exception.DAOException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ConnectionPool {
    private static final List<Connection> connections = new LinkedList<>();
    private static ConnectionPool pool;
    private static final int POOL_SIZE = 4;

    private ConnectionPool() {}

    public static ConnectionPool getInstance() throws DAOException {
        if (pool == null) {
            try {
                var initContext = new InitialContext();
                var envContext = (Context) initContext.lookup("java:/comp/env");
                var ds = (DataSource) envContext.lookup("jdbc/touragency");

                for (int i = 0; i < POOL_SIZE; i++) {
                    connections.add(ds.getConnection());
                }

                pool = new ConnectionPool();
            } catch (SQLException | NamingException e) {
                throw new DAOException(e);
            }
        }
        return pool;
    }

    public Connection getConnection() {
        while (connections.size() == 0) {
            Thread.yield();
        }

        var connection = connections.stream().findFirst().orElseThrow();
        connections.remove(connection);
        return connection;
    }

    public void returnConnection(Connection connection) {
        if (!connections.contains(connection)) {
            connections.add(connection);
        }
    }
}