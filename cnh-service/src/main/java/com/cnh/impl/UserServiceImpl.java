package com.cnh.impl;

import com.cnh.UsersService;
import com.cnh.bo.UsersBo;
import com.cnh.com.imooc.utils.MD5Utils;
import com.cnh.mapper.UsersMapper;
import com.cnh.org.n3r.idworker.IdWorker;
import com.cnh.org.n3r.idworker.Sid;
import com.cnh.pojo.Users;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExit(String username) {

        Example userExample = new Example(Users.class);
        Example.Criteria userCirteria = userExample.createCriteria();
        userCirteria.andEqualTo("username", username);
        Users result = usersMapper.selectOneByExample(userExample);
        return result == null ? false : true;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users create(UsersBo usersBo) throws Exception {
        Users users = new Users();
        //BeanUtils.copyProperties(usersBo,users);
        users.setUsername(usersBo.getUsername());
        users.setPassword(MD5Utils.getMD5Str(usersBo.getPassword()));
        users.setId(sid.nextShort());
        users.setBirthday("0000-00-00");
        users.setRealname(usersBo.getUsername());
        int i = usersMapper.insert(users);
        return (i == 1) ? users : null;
    }

    //双引号里面的字段是vo类的字段，并不是数据库驼峰的字段
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin(String username, String password) throws Exception {
        Example userExample = new Example(Users.class);
        Example.Criteria userCirteria = userExample.createCriteria();
        userCirteria.andEqualTo("username", username);
        userCirteria.andEqualTo("password", MD5Utils.getMD5Str(password));
        Users result = usersMapper.selectOneByExample(userExample);
        return result;
    }
}
