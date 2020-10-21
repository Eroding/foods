package com.cnh.controller;

import com.cnh.UsersService;
import com.cnh.utils.ResultVoUtil;
import com.cnh.utils.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Api(value = "第一次使用手动的swagger",tags = {"第一次使用手动的swagger"})
//这个方法用于不展示这个controller @ApiIgnore
@RequestMapping("/passport")
public class PassportController {
    @Autowired
    private UsersService usersService;

    @ApiOperation(value = "判断是否用户注册了",notes = "第一次使用手动的swagger",httpMethod = "GET")
    @GetMapping("/usernameIsExit")
    public ResultVo usernameIsExit(@RequestParam String username) {
        if (StringUtils.isBlank(username)) {
            return ResultVoUtil.error(500, "用户名为空");
        }
    boolean isExit = usersService.queryUsernameIsExit(username);
        if(isExit){
            return ResultVoUtil.error(501, "用户名已存在");
        }
        return ResultVoUtil.success();
    }
    }
