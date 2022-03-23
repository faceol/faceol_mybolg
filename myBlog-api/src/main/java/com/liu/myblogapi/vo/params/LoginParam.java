package com.liu.myblogapi.vo.params;

public class LoginParam {

    private String account;

    private String password;

    private String nickname;

    public LoginParam() {
    }

    public LoginParam(String account, String password,String nickname) {
        this.account = account;
        this.password = password;
        this.nickname=nickname;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}