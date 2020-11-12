package com.cnh.controller;

import com.cnh.UsersService;

import com.cnh.bo.UsersBo;
import com.cnh.com.imooc.utils.CookieUtils;
import com.cnh.com.imooc.utils.IMOOCJSONResult;
import com.cnh.com.imooc.utils.JsonUtils;
import com.cnh.pojo.Users;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Api(value = "第一次使用手动的swagger", tags = {"第一次使用手动的swagger"})
//这个方法用于不展示这个controller @ApiIgnore
@RequestMapping("/passport")
@CrossOrigin
public class PassportController {
    @Autowired
    private UsersService usersService;

    @ApiOperation(value = "判断是否用户注册了", notes = "第一次使用手动的swagger", httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public IMOOCJSONResult usernameIsExit(@RequestParam String username, HttpServletRequest request,
                                          HttpServletResponse response) {
        if (StringUtils.isBlank(username)) {
            return IMOOCJSONResult.errorMsg("用户名不能为空");
        }
        boolean isExit = usersService.queryUsernameIsExit(username);
        if (isExit) {
            return IMOOCJSONResult.errorMsg("用户名已经存在");
        }
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户注册", notes = "用户注册", httpMethod = "POST")
    @PostMapping("/regist")
    public IMOOCJSONResult register(@RequestBody UsersBo usersBo, HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
        Users users = usersService.create(usersBo);
        if (null != users) {
            return IMOOCJSONResult.errorMsg("用户名已经存在");
        }
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    public IMOOCJSONResult  login(@RequestBody UsersBo userBO,
                          HttpServletRequest request,
                          HttpServletResponse response) throws Exception {
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        Users users = usersService.queryUserForLogin(username, password);

        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(users),true);

        return IMOOCJSONResult.ok();
    }
}
