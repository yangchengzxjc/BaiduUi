package com.hand.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.InfraEmployee;
import com.hand.basicconstant.ApiPath;
import com.hand.utils.RandomNumber;

import java.util.Random;

/**
 * @Author peng.zhang
 * @Date 2020/6/30
 * @Version 1.0
 **/
public class InfraStructure extends BaseRequest{


    public JsonObject addEmployee(Employee employee,InfraEmployee infraEmployee,JsonArray userJobsDTOs,JsonArray customFormValues) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ ApiPath.ADD_EMPLOYEE;
        Random random =new Random();
        random.nextInt();
        JsonObject body =new JsonObject();
        JsonArray array =new JsonArray();
        JsonObject contactI18n1 =new JsonObject();
        JsonObject contactI18n2 =new JsonObject();
        body.add("userJobsDTOs",userJobsDTOs);
        body.addProperty("departmentPath","");
        body.addProperty("directManager","");
        body.addProperty("directManagerId",infraEmployee.getDirectManagerId());
        body.addProperty("directManagerName",infraEmployee.getDirectManagerName());
        body.addProperty("employeeType",(String) null);
        if(infraEmployee.getEmail()==null){
            body.addProperty("email", RandomNumber.getUUID()+"@hly.com");
        }else{
            body.addProperty("email", infraEmployee.getEmail());
        }
        if(infraEmployee.getMobile()==null){
            body.addProperty("mobile","");
        }else{
            body.addProperty("mobile",infraEmployee.getMobile());
        }
        body.addProperty("mobileCode","86");
        body.addProperty("countryCode","CN");
        body.addProperty("employeeTypeCode",infraEmployee.getEmployeeTypeCode());
        body.addProperty("userOID",(String) null);
        body.addProperty("employeeID",RandomNumber.getRandomNumber()*new Random().nextInt(20));
        body.addProperty("fullName",infraEmployee.getFullName());
        body.addProperty("status",1001);
        body.addProperty("manager",false);
        body.addProperty("leavingDate","3018-01-31T16:00:00.000Z");
        body.addProperty("gender","");
        body.addProperty("genderCode",0);
        body.addProperty("birthday","2020-07-01");
        body.addProperty("entryTime","2020-07-01");
        body.addProperty("gender","");
        contactI18n1.addProperty("language","zh_cn");
        contactI18n1.addProperty("value",infraEmployee.getFullName());
        contactI18n2.addProperty("language","en");
        contactI18n2.addProperty("value",infraEmployee.getFullName());
        array.add(contactI18n1);
        array.add(contactI18n2);
        body.add("customFormValues",customFormValues);
        String res= doPost(url,getHeader(employee.getAccessToken()),null,body.toString(),null, employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }
}
