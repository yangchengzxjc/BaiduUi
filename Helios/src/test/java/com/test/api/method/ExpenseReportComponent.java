package com.test.api.method;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.api.ComponentQuery;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author peng.zhang
 * @Date 2020/6/11
 * @Version 1.0
 **/
@Slf4j
public class ExpenseReportComponent {

    private ComponentQuery componentQuery;

    public ExpenseReportComponent(){
        componentQuery=new ComponentQuery();
    }

    public String getCityCode(Employee employee,String city) throws HttpStatusException {
        String cityCode="";
        if(city.equals("")){
            return cityCode;
        }else{
            JsonArray jsonArray=componentQuery.locationSearch(employee,city);
            for (int i =0 ;i< jsonArray.size();i++){
                if(jsonArray.get(i).getAsJsonObject().get("vndLocationName").getAsString().equalsIgnoreCase(city)){
                    cityCode = jsonArray.get(i).getAsJsonObject().get("vndLocationCode").getAsString();
                }
            }
            return cityCode;
        }
    }

    /**
     * 从控件上查询参与人根据fullName 获取参与人 （是否是自己）
     * @param employee
     * @param formOID
     * @param fullName 员工的姓名
     * @throws HttpStatusException
     */
    public JsonObject getParticipant(Employee employee,String formOID,String fullName) throws HttpStatusException {
        JsonArray array = componentQuery.getSelectParticipant(employee,formOID,employee.getJobId());
        JsonObject participant = new JsonObject();
        for(int i=0; i<array.size();i++){
            if(array.get(i).getAsJsonObject().get("fullName").getAsString().equals(fullName)){
                participant.addProperty("userOID",array.get(i).getAsJsonObject().get("userOID").getAsString());
                participant.addProperty("fullName",array.get(i).getAsJsonObject().get("fullName").getAsString());
                participant.addProperty("participantOID",array.get(i).getAsJsonObject().get("userOID").getAsString());
                participant.addProperty("highOff", (String) null);
                participant.addProperty("avatar",(String) null);
            }
        }
        return participant;
    }

    /**
     * 费用上的出法地  目的地等控件的地址搜索栏
     * @param employee
     * @param keyWords
     * @throws HttpStatusException
     */
    public JsonObject getExpenseLocation(Employee employee, String keyWords) throws HttpStatusException {
        JsonArray jsonArray=componentQuery.queryExpenseLocation(employee,keyWords);
        JsonObject jsonObject =new JsonObject();
        for(int i= 0;i<jsonArray.size();i++){
            if(jsonArray.get(i).getAsJsonObject().get("name").getAsString().equals(keyWords)){
                jsonObject.addProperty("address",keyWords);
                String longitude =jsonArray.get(i).getAsJsonObject().get("location").getAsString().split(",")[0];
                String latitude = jsonArray.get(i).getAsJsonObject().get("location").getAsString().split(",")[1];
                jsonObject.addProperty("longitude",longitude);
                jsonObject.addProperty("latitude",latitude);
            }
        }
        return jsonObject;
    }

    public Map<String,String> queryDepartment(Employee employee, String deptCode) throws HttpStatusException {
        JsonObject departmentInfo = componentQuery.getformDepartment(employee,deptCode).getAsJsonArray().get(0).getAsJsonObject();
        Map<String,String> deptMap = new HashMap<>();
        deptMap.put("departmentOID",departmentInfo.get("departmentOid").getAsString());
        deptMap.put("name",departmentInfo.get("name").getAsString());
        return deptMap;
    }


}
