package com.liu.myblogapi.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.liu.myblogapi.dao.mapper.SysUserMapper;
import com.liu.myblogapi.pojo.SysUser;
import com.liu.myblogapi.utils.JWTUtils;
import com.liu.myblogapi.vo.ErrorCode;
import com.liu.myblogapi.vo.Result;
import com.liu.myblogapi.vo.params.LoginParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private RedisTemplate redisTemplate;
    //加密盐
    private static final String slat = "mszlu!@###";
    @Override
    //登录功能
   public Result login(LoginParam loginParam){
//        检查参数是否合法
//        根据用户名和密码去user表查询是否存在，如果不存在则登陆失败
//        如果存在则使用jwt返回token给前端
//        同时将token放入redis中，redis token：user信息 设置过期时间
//        登录的时候先验证token字符串是否合法，再去redis查看是否存在
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        if (account.isEmpty()||password.isEmpty()){
            return Result.fail(ErrorCode.NO_LOGIN.getCode(),ErrorCode.NO_LOGIN.getMsg());
        }
        password = DigestUtils.md5Hex(password+slat);
        SysUser sysUser=sysUserService.findUser(account,password);
        if (sysUser==null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        String token = JWTUtils.createToken(Long.valueOf(account));
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return Result.success(token);
    }

    //token检测
    @Override
    public SysUser checkToken(String token) {
        if (token.isEmpty()){
            return null;
        }
        Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
        if (stringObjectMap==null){
            return null;
        }
        String userJson =(String)redisTemplate.opsForValue().get("TOKEN_" + token);
        if (userJson.isEmpty()){
            return null;
        }
        SysUser sysUser = JSON.parseObject(userJson,SysUser.class);
        return sysUser;
    }
    //登出功能
    @Override
    public Result logout(String token) {
        redisTemplate.delete("TOKEN_"+token);
        return Result.success(null);
    }

    //注册功能
    @Override
    public Result register(LoginParam loginParam) {
        /*
        * 1.判断注册信息是否合法
        * 2.查看数据库是否存在用户，存在则返回已注册
        * 3.如果不存在则注册
        * 4.生产token，并存入redis
        *5加上事务，一旦中途出错，注册的用户需要回滚
        * */
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        String nickname = loginParam.getNickname();
        if (StringUtils.isBlank(account)
                || StringUtils.isBlank(password)
                || StringUtils.isBlank(nickname)
        ){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        SysUser sysUser=sysUserService.findUserByAccount(account);
        if (sysUser!=null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        sysUser = new SysUser();
        sysUser.setNickname(nickname);
        sysUser.setAccount(account);
        sysUser.setPassword(DigestUtils.md5Hex(password+slat));
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar("/static/img/logo.b3a48c0.png");
        sysUser.setAdmin(1); //1 为true
        sysUser.setDeleted(0); // 0 为false
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");
        this.sysUserService.save(sysUser);
        String token = JWTUtils.createToken(Long.valueOf(account));
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);


        return null;
    }
}