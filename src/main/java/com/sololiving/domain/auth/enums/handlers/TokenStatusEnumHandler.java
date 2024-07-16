package com.sololiving.domain.auth.enums.handlers;

import com.sololiving.domain.auth.enums.TokenStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;

public class TokenStatusEnumHandler extends BaseTypeHandler<TokenStatus> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, TokenStatus parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.name());
    }

    @Override
    public TokenStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String name = rs.getString(columnName);
        return name == null ? null : TokenStatus.valueOf(name);
    }

    @Override
    public TokenStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String name = rs.getString(columnIndex);
        return name == null ? null : TokenStatus.valueOf(name);
    }

    @Override
    public TokenStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String name = cs.getString(columnIndex);
        return name == null ? null : TokenStatus.valueOf(name);
    }
}