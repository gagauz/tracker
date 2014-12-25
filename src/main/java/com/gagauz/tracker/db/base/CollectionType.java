package com.gagauz.tracker.db.base;

import com.gagauz.tracker.utils.TextTransformer;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

public class CollectionType implements UserType, ParameterizedType {

    public static final String ELEMENT_CLASS = "class";

    private static final int SQL_TYPE = Types.VARCHAR;
    private static final int[] SQL_TYPES = new int[] {SQL_TYPE};
    private static final String SEPARATOR_STRING = ",";
    private Class<?> entityClass;
    private TextTransformer transformer;

    @Override
    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    @Override
    public void setParameterValues(Properties parameters) {
        final String clazzName = parameters.getProperty(ELEMENT_CLASS);
        try {
            entityClass = Class.forName(clazzName);
            transformer = TextTransformers.get(entityClass);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Class " + clazzName + " not found", e);
        }

    }

    @Override
    public Class<?> returnedClass() {
        return entityClass;
    }

    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        final String string = resultSet.getString(names[0]);
        if (resultSet.wasNull()) {
            return null;
        }
        String[] strings = string.split(SEPARATOR_STRING);
        List result = new ArrayList(strings.length);
        for (String str : strings) {
            result.add(transformer.applyR(str));
        }

        return result;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == y) {
            return true;
        }
        if (null == x || null == y) {
            return false;
        }
        return x.equals(y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return (null == x || !(x instanceof Collection)) ? 0 : ((Collection) x).hashCode();
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {

        if (null == value) {
            st.setNull(index, sqlTypes()[0]);
        } else if (!(value instanceof Collection)) {
            throw new HibernateException("Type of value [" + value.getClass() + "] is not Collection");
        } else {
            Iterator i = ((Collection) value).iterator();
            if (i.hasNext()) {
                StringBuilder sb = new StringBuilder();
                sb.append(transformer.applyP(i.next()));
                while (i.hasNext()) {
                    sb.append(SEPARATOR_STRING).append(transformer.applyP(i.next()));
                }
                st.setString(index, sb.toString());
            } else {
                st.setString(index, "");
            }
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return null == value ? null : new ArrayList((Collection) value);
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (ArrayList) deepCopy(value);
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return deepCopy(cached);
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return deepCopy(original);
    }
}
