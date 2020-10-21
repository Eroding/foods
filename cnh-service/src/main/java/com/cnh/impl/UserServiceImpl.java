package com.cnh.impl;

import com.cnh.UsersService;
import com.cnh.mapper.UsersMapper;
import com.cnh.pojo.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.spring.annotation.MapperScan;

@Service
@MapperScan(basePackages = {"com.cnh.mapper"})
public class UserServiceImpl implements UsersService {
    @Autowired
    public UsersMapper usersMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExit(String username) {

        Example userExample = new Example(Users.class);
        Example.Criteria userCirteria =userExample.createCriteria();
        userCirteria.andEqualTo("username",username);
        Users result = usersMapper.selectOneByExample(userExample);
        return result==null?false:true;
    }
}
