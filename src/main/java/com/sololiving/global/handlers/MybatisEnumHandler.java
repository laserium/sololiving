package com.sololiving.global.handlers;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;

public class MybatisEnumHandler<E extends Enum<E>> extends BaseTypeHandler<E> {

    private final Class<E> type;

    public MybatisEnumHandler(Class<E> type) {
        if (type == null) throw new IllegalArgumentException("Type argument cannot be null");
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.name());
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String name = rs.getString(columnName);
        if (name == null) {
            return null;
        }
        try {
            return Enum.valueOf(type, name);
        } catch (IllegalArgumentException e) {
            throw new SQLException("Unknown enum type: " + name + ", for enum class: " + type.getSimpleName(), e);
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String name = rs.getString(columnIndex);
        if (name == null) {
            return null;
        }
        try {
            return Enum.valueOf(type, name);
        } catch (IllegalArgumentException e) {
            throw new SQLException("Unknown enum type: " + name + ", for enum class: " + type.getSimpleName(), e);
        }
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String name = cs.getString(columnIndex);
        if (name == null) {
            return null;
        }
        try {
            return Enum.valueOf(type, name);
        } catch (IllegalArgumentException e) {
            throw new SQLException("Unknown enum type: " + name + ", for enum class: " + type.getSimpleName(), e);
        }
    }
}
