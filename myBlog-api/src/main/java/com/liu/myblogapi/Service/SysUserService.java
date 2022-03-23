package com.liu.myblogapi.Service;

import com.liu.myblogapi.pojo.SysUser;
import com.liu.myblogapi.vo.Result;
import com.liu.myblogapi.vo.UserVo;


public interface SysUserService {

    SysUser findUserById(Long userId);

    SysUser findUser(String account, String password);

    Result getUserInfoByToken(String token);

    void save(SysUser sysUser);

    SysUser findUserByAccount(String account);

    UserVo findUserVoById(Long authorId);
}
