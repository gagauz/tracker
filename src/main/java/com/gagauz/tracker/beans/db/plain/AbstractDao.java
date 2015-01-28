package com.gagauz.tracker.beans.db.plain;

import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractDao {
    @Autowired
    private DataSource dataSource;

    private int query(String sql, Object... parameters) {
        int result = 0;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            if (parameters.length == 0) {
                resultSet = statement.executeQuery(sql);
            } else {
                statement = connection.prepareStatement(sql);
                for (int i = 0; i < parameters.length; i++)
                    setParameter(statement, i, parameters[i]);
                resultSet = statement.executeQuery();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    private void setParameter(PreparedStatement statement, int index, Object object) throws SQLException {
        if (null == object)
            statement.setObject(index, object);
        else if (object instanceof Byte)
            statement.setByte(index, (Byte) object);
        else if (object instanceof Integer)
            statement.setInt(index, (Integer) object);
        else if (object instanceof Long)
            statement.setLong(index, (Long) object);
        else if (object instanceof Float)
            statement.setFloat(index, (Float) object);
        else if (object instanceof Double)
            statement.setDouble(index, (Double) object);
        else if (object instanceof BigDecimal)
            statement.setBigDecimal(index, (BigDecimal) object);
        else if (object instanceof String)
            statement.setString(index, (String) object);
        else
            throw new IllegalArgumentException("Unknown type of object " + object.getClass());

    }
}
