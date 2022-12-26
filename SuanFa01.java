package com.test.api.testcase.UiAndintreF;

/**
 * @Author yc
 * @Date 2022/6/29
 * @Version 1.0
 **/

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.hand.basicObject.infrastructure.employee.InfraJob;
import com.hand.utils.GsonUtil;

import java.util.ArrayList;

public class SuanFa01 {
    private String comanyOid = "asdfas";
    private String companyid = "fdsfase";
    private Integer employee = 231212;


    public String getComanyOid() {
        return comanyOid;
    }

    public void setComanyOid(String comanyOid) {
        this.comanyOid = comanyOid;
    }

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    public int getEmployee() {
        return employee;
    }

    public void setEmployee(int employee) {
        this.employee = employee;
    }

    public JsonArray userJobsDTOs(ArrayList<InfraJob> infraJob){
        return new JsonParser().parse(GsonUtil.objectToString(infraJob)).getAsJsonArray();
    }
    public static void main(String[] args) {
        SuanFa01 sf = new SuanFa01();
        ArrayList al = new ArrayList();
        al.add(sf.comanyOid);
        System.out.println(al+"第一次");
        al.add(sf.employee);
        System.out.println(al+"第二次");
        al.add(sf.companyid);
        System.out.println(al+"第三次");
        System.out.println(sf.userJobsDTOs(al));
    }
}

