package com.gagauz.tracker.beans.setup;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.JDBC4Connection;
import com.mysql.jdbc.ResultSetInternalMethods;
import com.mysql.jdbc.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.Properties;

public class StatementInterceptor implements com.mysql.jdbc.StatementInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatementInterceptor.class);

    private JDBC4Connection connection;

    private Properties properties;

    private String conn_id;

    private static boolean initialized = false;

    @Override
    public void init(Connection connection, Properties properties) throws SQLException {
        this.connection = (JDBC4Connection) connection;
        this.properties = properties;
        long connectionId = this.connection.getId();

        if (connectionId != 0) {
            initialized = true;
            conn_id = "Connection [" + connectionId + "] ";
            StringBuilder sb = new StringBuilder(conn_id + "Properties:\n");
            for (String prop : properties.stringPropertyNames()) {
                sb.append("\t").append(prop).append(" = ").append(properties.getProperty(prop)).append("\n");
            }
            LOGGER.debug(sb.toString());
        }
    }

    @Override
    public ResultSetInternalMethods preProcess(String s, Statement statement, Connection connection) throws SQLException {
        if (!initialized) {
            init(connection, properties);
        }
        if (s != null) {
            LOGGER.debug(conn_id + s);
        } else {
            String content = statement.toString();
            LOGGER.debug(conn_id + content.substring(content.indexOf(':')));
        }
        try {
            //throw new RuntimeException();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResultSetInternalMethods postProcess(String s, Statement statement, ResultSetInternalMethods resultSetInternalMethods, Connection connection)
            throws SQLException {
        SQLWarning warning;
        if (resultSetInternalMethods != null && (warning = resultSetInternalMethods.getWarnings()) != null) {
            log(warning);
        } else if ((warning = connection.getWarnings()) != null) {
            log(warning);
        }
        return resultSetInternalMethods;
    }

    @Override
    public boolean executeTopLevelOnly() {
        return false;
    }

    @Override
    public void destroy() {
        LOGGER.debug("Connection: {} destroyed", connection.toString());
    }

    private void log(SQLWarning warning) {
        LOGGER.warn("ATTENSION!!!");
        while (warning != null) {
            LOGGER.warn(warning.getMessage());
            warning = warning.getNextWarning();
        }
    }
}
