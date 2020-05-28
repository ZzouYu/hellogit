package com.zouyu.dao;

import com.zouyu.vo.User;

import java.util.List;

public interface UserDao {

    User queryPassword(String userName);

    List<String> queryRolesByUsername(String userName);
}
