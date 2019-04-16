package com.lsy.italker;

public class UserService implements IUserService {
    public String search(int hashCode) {
        return "User:" + hashCode;
    }
}
