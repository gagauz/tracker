package com.gagauz.tracker.db.utils;

import com.gagauz.tracker.db.model.Project;
import org.hibernate.Query;

public class Param {

    private static final String modelPackage = Project.class.getPackage().getName();

    private final String name;
    private final Object value;

    public Param(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public static Param param(String name, Object value) {
        return new Param(name, value);

    }

    public void update(Query query) {
        /*
        if (value instanceof BigDecimal) {
            query.setBigDecimal(name, (BigDecimal) value);
        } else if (value instanceof BigInteger) {
            query.setBigInteger(name, (BigInteger) value);
        } else if (value instanceof byte[]) {
            query.setBinary(name, (byte[]) value);
        } else if (value instanceof Boolean) {
            query.setBoolean(name, (Boolean) value);
        } else if (value instanceof Byte) {
            query.setByte(name, (Byte) value);
        } else if (value instanceof Calendar) {
            query.setCalendar(name, (Calendar) value);
        } else if (value instanceof Date) {
            query.setDate(name, (Date) value);
        } else if (value instanceof Character) {
            query.setCharacter(name, (Character) value);
        } else if (value instanceof Double) {
            query.setDouble(name, (Double) value);
        } else if (value instanceof Float) {
            query.setFloat(name, (Float) value);
        } else if (value instanceof Integer) {
            query.setInteger(name, (Integer) value);
        } else if (value instanceof Long) {
            query.setLong(name, (Long) value);
        } else if (value instanceof String) {
            query.setString(name, (String) value);
        } else */if (null != value && value.getClass().getName().startsWith(modelPackage)) {
            query.setEntity(name, value);
        } else {
            query.setParameter(name, value);
        }
    }

}
