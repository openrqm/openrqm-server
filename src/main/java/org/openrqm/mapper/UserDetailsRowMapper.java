/*
 * openrqm-server
 * SPDX-License-Identifier: GPL-2.0-only
 * Copyright (C) 2019 Marcel Jaehn
 */

package org.openrqm.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.openrqm.model.RQMUserDetail;
import org.springframework.jdbc.core.RowMapper;

public class UserDetailsRowMapper implements RowMapper<RQMUserDetail> {

    @Override
    public RQMUserDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
        RQMUserDetail user = new RQMUserDetail();
        user.setId(rs.getLong("id"));
        user.setEmail(rs.getString("email"));
        user.setName(rs.getString("name"));
        user.setSurname(rs.getString("surname"));
        user.setDepartment(rs.getString("department"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setToken(rs.getString("token"));
        return user;
    }
}
