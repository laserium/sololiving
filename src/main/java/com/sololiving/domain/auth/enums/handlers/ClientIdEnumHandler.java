package com.sololiving.domain.auth.enums.handlers;

import com.sololiving.domain.auth.enums.ClientId;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;

public class ClientIdEnumHandler extends BaseTypeHandler<ClientId> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ClientId parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.name());
    }

    @Override
    public ClientId getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String name = rs.getString(columnName);
        return name == null ? null : ClientId.valueOf(name);
    }

    @Override
    public ClientId getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String name = rs.getString(columnIndex);
        return name == null ? null : ClientId.valueOf(name);
    }

    @Override
    public ClientId getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String name = cs.getString(columnIndex);
        return name == null ? null : ClientId.valueOf(name);
    }
}
