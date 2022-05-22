package com.nannning.crm.settings.service;

import com.nannning.crm.settings.domain.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    //查询用户
    User queryUserByLoginActAndLoginPwd(Map<String,Object> map);

    List<User> queryAllUser();
}
