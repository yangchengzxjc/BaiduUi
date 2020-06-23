package com.test.api.method;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.api.ComponentQuery;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import lombok.extern.slf4j.Slf4j;

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
     * 从控件上查询参与人
     * @param employee
     * @param formOID
     * @param number   表示你需要拿第几个参与人，0代表第一个人
     * @throws HttpStatusException
     */
    public JsonObject getParticipant(Employee employee,String formOID,int number) throws HttpStatusException {
        JsonObject Participant=componentQuery.getSelectParticipant(employee,formOID,employee.getJobId()).get(number).getAsJsonObject();
        JsonObject myobj = new JsonObject();
        myobj.addProperty("userOID",Participant.get("userOID").getAsString());
        myobj.addProperty("fullName",Participant.get("fullName").getAsString());
        myobj.addProperty("participantOID",Participant.get("userOID").getAsString());
        myobj.addProperty("highOff",Participant.get("highOff").getAsString());
        myobj.addProperty("avatar",Participant.get("avatar").getAsString());
        return myobj;
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

}
