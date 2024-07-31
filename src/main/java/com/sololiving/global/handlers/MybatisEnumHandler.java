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
        String value = rs.getString(columnName);
        if (value != null) {
            try {
                return Enum.valueOf(type, value);
            } catch (IllegalArgumentException e) {
                return null; // 존재하지 않는 enum 값인 경우 null 반환
            }
        }
        return null;
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        if (value != null) {
            try {
                return Enum.valueOf(type, value);
            } catch (IllegalArgumentException e) {
                return null; // 존재하지 않는 enum 값인 경우 null 반환
            }
        }
        return null;
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        if (value != null) {
            try {
                return Enum.valueOf(type, value);
            } catch (IllegalArgumentException e) {
                return null; // 존재하지 않는 enum 값인 경우 null 반환
            }
        }
        return null;
    }
}
