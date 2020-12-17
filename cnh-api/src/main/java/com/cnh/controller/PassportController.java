package com.cnh.controller;

import com.cnh.UsersService;

import com.cnh.bo.ShopcartBO;
import com.cnh.bo.UsersBo;
import com.cnh.com.imooc.utils.CookieUtils;
import com.cnh.com.imooc.utils.IMOOCJSONResult;
import com.cnh.com.imooc.utils.JsonUtils;
import com.cnh.com.imooc.utils.RedisOperator;
import com.cnh.pojo.Users;

import com.cnh.vo.UsersVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@Api(value = "第一次使用手动的swagger", tags = {"第一次使用手动的swagger"})
//这个方法用于不展示这个controller @ApiIgnore
@RequestMapping("/passport")
@CrossOrigin
public class PassportController extends BaseController {
    @Autowired
    private UsersService usersService;
    @Autowired
    private RedisOperator redisOperator;

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
        // 实现用户的redis会话
        UsersVO usersVO = conventUsersVO(users);
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(usersVO), true);
        //同步数据可数据
        synchShopcartData(users.getId(), request, response);
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    public IMOOCJSONResult login(@RequestBody UsersBo userBO,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        Users users = usersService.queryUserForLogin(username, password);
//todo:生成token，存入redis

        // 实现用户的redis会话
        UsersVO usersVO = conventUsersVO(users);
        //同步购物车
        synchShopcartData(users.getId(), request, response);

        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(usersVO), true);

        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户退出", notes = "用户退出", httpMethod = "POST")
    @PostMapping("/logout")
    public IMOOCJSONResult logout(@RequestParam String userId,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws Exception {
       /* String username = userBO.getUsername();
        String password = userBO.getPassword();
        Users users = usersService.queryUserForLogin(username, password);

        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(users),true);*/
        //清除用户相关的cookie
        CookieUtils.deleteCookie(request, response, "user");
        //todo:用户退出登录，清空购物车
        redisOperator.del(REDIS_USER_TOKEN + ":" + userId);
        //TODO:分布式绘画中需要清除用户的数据
        CookieUtils.deleteCookie(request, response, FOODIE_SHOPCART);
        return IMOOCJSONResult.ok();
    }

    /**
     * 注册登录成功后，同步cookie和redis中的购物车数据
     */
    private void synchShopcartData(String userId, HttpServletRequest request,
                                   HttpServletResponse response) {

        /**
         * 1. redis中无数据，如果cookie中的购物车为空，那么这个时候不做任何处理
         *                 如果cookie中的购物车不为空，此时直接放入redis中
         * 2. redis中有数据，如果cookie中的购物车为空，那么直接把redis的购物车覆盖本地cookie
         *                 如果cookie中的购物车不为空，
         *                      如果cookie中的某个商品在redis中存在，
         *                      则以cookie为主，删除redis中的，
         *                      把cookie中的商品直接覆盖redis中（参考京东）
         * 3. 同步到redis中去了以后，覆盖本地cookie购物车的数据，保证本地购物车的数据是同步最新的
         */

        // 从redis中获取购物车
        String shopcartJsonRedis = redisOperator.get(FOODIE_SHOPCART + ":" + userId);

        // 从cookie中获取购物车
        String shopcartStrCookie = CookieUtils.getCookieValue(request, FOODIE_SHOPCART, true);

        if (StringUtils.isBlank(shopcartJsonRedis)) {
            // redis为空，cookie不为空，直接把cookie中的数据放入redis
            if (StringUtils.isNotBlank(shopcartStrCookie)) {
                redisOperator.set(FOODIE_SHOPCART + ":" + userId, shopcartStrCookie);
            }
        } else {
            // redis不为空，cookie不为空，合并cookie和redis中购物车的商品数据（同一商品则覆盖redis）
            if (StringUtils.isNotBlank(shopcartStrCookie)) {

                /**
                 * 1. 已经存在的，把cookie中对应的数量，覆盖redis（参考京东）
                 * 2. 该项商品标记为待删除，统一放入一个待删除的list
                 * 3. 从cookie中清理所有的待删除list
                 * 4. 合并redis和cookie中的数据
                 * 5. 更新到redis和cookie中
                 */

                List<ShopcartBO> shopcartListRedis = JsonUtils.jsonToList(shopcartJsonRedis, ShopcartBO.class);
                List<ShopcartBO> shopcartListCookie = JsonUtils.jsonToList(shopcartStrCookie, ShopcartBO.class);

                // 定义一个待删除list
                List<ShopcartBO> pendingDeleteList = new ArrayList<>();

                for (ShopcartBO redisShopcart : shopcartListRedis) {
                    String redisSpecId = redisShopcart.getSpecId();

                    for (ShopcartBO cookieShopcart : shopcartListCookie) {
                        String cookieSpecId = cookieShopcart.getSpecId();

                        if (redisSpecId.equals(cookieSpecId)) {
                            // 覆盖购买数量，不累加，参考京东
                            redisShopcart.setBuyCounts(cookieShopcart.getBuyCounts());
                            // 把cookieShopcart放入待删除列表，用于最后的删除与合并
                            pendingDeleteList.add(cookieShopcart);
                        }

                    }
                }

                // 从现有cookie中删除对应的覆盖过的商品数据
                shopcartListCookie.removeAll(pendingDeleteList);

                // 合并两个list
                shopcartListRedis.addAll(shopcartListCookie);
                // 更新到redis和cookie
                CookieUtils.setCookie(request, response, FOODIE_SHOPCART, JsonUtils.objectToJson(shopcartListRedis), true);
                redisOperator.set(FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson(shopcartListRedis));
            } else {
                // redis不为空，cookie为空，直接把redis覆盖cookie
                CookieUtils.setCookie(request, response, FOODIE_SHOPCART, shopcartJsonRedis, true);
            }

        }
    }
}
