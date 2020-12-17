package com.cnh;

import com.cnh.bo.UsersBo;
import com.cnh.pojo.Users;
import org.springframework.stereotype.Service;


public interface UsersService {

    //判断用户名是否存在
    public boolean queryUsernameIsExit(String username);

    //注册新用户
    public Users create(UsersBo usersBo) throws Exception;

    //用户登录
    public Users queryUserForLogin(String username, String password) throws Exception;
}
