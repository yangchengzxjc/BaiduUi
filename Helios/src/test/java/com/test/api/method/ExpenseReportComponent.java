package com.test.api.method;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.api.ComponentQueryApi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.utils.GsonUtil;
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

    private ComponentQueryApi componentQueryApi;

    public ExpenseReportComponent(){
        componentQueryApi =new ComponentQueryApi();
    }

    /**
     * 根据城市名称查询城市cityCode  城市控件等地方需要传入此参数
     * @param employee
     * @param city
     * @return
     * @throws HttpStatusException
     */
    public String getCityCode(Employee employee,String city) throws HttpStatusException {
        String cityCode="";
        if(city.equals("")){
            return cityCode;
        }else{
            JsonArray jsonArray= componentQueryApi.locationSearch(employee,city);
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
        JsonArray array = componentQueryApi.getSelectParticipant(employee,formOID,employee.getJobId());
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
        JsonArray jsonArray= componentQueryApi.queryExpenseLocation(employee,keyWords);
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

    /**
     * 根据部门编码查询部门相关信息
     * @param employee
     * @param deptCode   部门编码
     * @return
     * @throws HttpStatusException
     */
    public Map<String,String> queryDepartment(Employee employee, String deptCode) throws HttpStatusException {
        Map<String,String> deptMap = new HashMap<>();
        JsonArray deptList = componentQueryApi.getformDepartment(employee,deptCode);
        if(GsonUtil.isNotEmpt(deptList)){
            JsonObject departmentInfo = deptList.get(0).getAsJsonObject();
            deptMap.put("departmentOID",departmentInfo.get("departmentOid").getAsString());
            deptMap.put("name",departmentInfo.get("name").getAsString());
        }else{
            log.info("查询部门信息为空,请检查参数");
        }
        return deptMap;
    }


}
