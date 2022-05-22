package com.nannning.crm.settings.service.impl;

import com.nannning.crm.settings.domain.User;
import com.nannning.crm.settings.mapper.UserMapper;
import com.nannning.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User queryUserByLoginActAndLoginPwd(Map<String, Object> map) {
        return userMapper.selectUserByLoginActAndLoginPwd(map);
    }

    @Override
    public List<User> queryAllUser() {
        return userMapper.selectAllUser();
    }
}
