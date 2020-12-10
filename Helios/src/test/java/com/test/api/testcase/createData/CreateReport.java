package com.test.api.testcase.createData;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.FormComponent;
import com.hand.basicObject.itinerary.FlightItinerary;
import com.hand.basicConstant.SupplierOID;
import com.hand.utils.DocumnetUtil;
import com.hand.utils.UTCTime;
import com.test.BaseTest;
import com.test.api.method.ApplicationMethod.TravelApplicationPage;
import com.test.api.method.ExpenseReport;
import com.test.api.method.TravelApplication;
import org.testng.annotations.Test;

import java.util.ArrayList;

/**
 * @Author peng.zhang
 * @Date 2020/8/17
 * @Version 1.0
 **/
public class CreateReport extends BaseTest {

    //日常报销单的新建-提交
    @Test
    public void createDailyReport() throws HttpStatusException {
        ExpenseReport expenseReport = new ExpenseReport();
        //读取文件中的账号信息
        ArrayList data = DocumnetUtil.fileReader("D:/t.txt",1);
        for(int i=0; i<10;i++){
            for (Object phoneNumber:data){
                //设置环境登录信息
                Employee employee = getEmployee((String) phoneNumber, "hly123", "mcd");
                expenseReport.createExpenseReport(employee,"formName");
            }
        }
    }

    //费用申请单新建-提交-费用报销单新建-提交
    @Test
    public void createExpenseReport() throws HttpStatusException {
        FormComponent component =null;
        TravelApplication travelApplication =new TravelApplication();
        ExpenseReport expenseReport =new ExpenseReport();
        Employee employee = getEmployee("14082978625", "hly12345", "stage");
        //申请单生成预算
        JsonObject expenseBudget = travelApplication.addBudgetExpense(employee,23.0,false,"大巴","费用申请单-测试");
        JsonArray expenseBudgets = new JsonArray();
        expenseBudgets.add(expenseBudget);
        String budget = travelApplication.addBudgetDetail(expenseBudgets,23.0);
        JsonObject application = travelApplication.createExpenseApplication(employee,"费用申请单-测试",component,budget);
        String applicationOID =application.get("applicationOID").getAsString();
        //申请单提交
        travelApplication.submitExpenseApplication(employee,applicationOID);
        //获取申请单默认的控件信息
        JsonArray dafaultCustomFormValue = expenseReport.getValueFromApplication(employee,applicationOID);
        expenseReport.createTravelExpenseReport(employee,"费用报销单-测试",applicationOID,dafaultCustomFormValue);
        //报销单新建费用
    }

    //差旅申请单-提交-差旅报销单-提交-差旅报销单-
    @Test
    public void createTravelReport() throws HttpStatusException {
        Employee employee = getEmployee("14082978625", "hly12345", "stage");
        //造数据可以将component 为空
        FormComponent component = null;
        //创建申请单
        TravelApplication travelApplication =new TravelApplication();

        String applicationOID = travelApplication.createTravelApplication(employee,"差旅申请单-节假日",component).get("applicationOID");
        //添加差旅行程(飞机 酒店 火车等行车均已经支持)
        ArrayList<FlightItinerary> flightItineraries =new ArrayList<>();
        TravelApplicationPage travelApplicationPage = new TravelApplicationPage();
        FlightItinerary flightItinerary=travelApplicationPage.addFlightItinerary(employee,1001, SupplierOID.CTRIPAIR.getSupplierOID(),"西安市","北京",null,UTCTime.getNowStartUtcDate());
        flightItineraries.add(flightItinerary);
        travelApplication.addItinerary(employee,applicationOID,flightItineraries);
        //行程会自动带出预算费用。
        travelApplication.submitApplication(employee,applicationOID,"");
        //差旅报销单新建
        ExpenseReport expenseReport =new ExpenseReport();
        //获取申请单默认的控件信息
        JsonArray dafaultCustomFormValue = expenseReport.getValueFromApplication(employee,applicationOID);
        expenseReport.createTravelExpenseReport(employee,"差旅报销单-节假日",applicationOID,dafaultCustomFormValue);
    }


}
