package com.zouyu.dao.impl;

import com.zouyu.dao.UserDao;
import com.zouyu.vo.User;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zouyu
 * @description
 * @date 2019/4/23
 */
@Repository
public class UserDaoImpl implements UserDao {
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Override
    public User queryPassword(String userName) {
        String sql = "select username,password from users where username =?";

        List<User> list = jdbcTemplate.query(sql, new String[]{userName}, (resultSet, i) -> {
            User user = new User();
            user.setUserName(resultSet.getString("username"));
            user.setPassWord(resultSet.getString("password"));
            return user;
        });

        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        return list.get(0);
    }
    @Override
    public List<String> queryRolesByUsername(String userName) {

        String sql = "select role_name from USER_ROLES where username = ?";

        return jdbcTemplate.query(sql, new String[]{userName},
                (resultSet, i) -> resultSet.getString("role_name"));
    }

}
