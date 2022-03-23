package com.liu.myblogapi.Service;

import com.liu.myblogapi.pojo.SysUser;
import com.liu.myblogapi.vo.Result;
import com.liu.myblogapi.vo.params.LoginParam;

public interface LoginService {
    /**
     * 登录
     * @param loginParam
     * @return
     */
    Result login(LoginParam loginParam);

    SysUser checkToken(String token);

    Result logout(String token);

    Result register(LoginParam loginParam);
}