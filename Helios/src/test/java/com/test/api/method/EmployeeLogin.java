package com.test.api.method;

import com.google.gson.JsonObject;
import com.hand.api.LoginApi;
import com.hand.basicObject.Employee;

/**
 * @Author peng.zhang
 * @Date 2020/6/5
 * @Version 1.0
 **/
public class EmployeeLogin {

    private LoginApi loginApi;

    public EmployeeLogin() {
        this.loginApi =new LoginApi();
    }

    public String getToken(Employee employee){
       JsonObject response = loginApi.login(employee);
       return response.get("access_token").getAsString();
    }
}
