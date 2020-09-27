package com.test.api.testcase.vendor;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.supplierObject.employeeInfoDto.EmployeeDTO;
import com.hand.basicconstant.CardType;
import com.hand.basicconstant.TmcChannel;
import com.hand.utils.GsonUtil;
import com.test.BaseTest;
import com.test.api.method.Infra.EmployeeMethod.EmployeeManagePage;
import com.test.api.method.InfraStructure;
import com.test.api.method.Vendor;
import com.test.api.method.VendorMethod.SyncService;
import org.testng.Assert;
import org.testng.annotations.*;
import java.util.ArrayList;


public class SyncEmployee extends BaseTest {
    private Employee employee;
    private EmployeeManagePage employeeManagePage;
    private InfraStructure infraStructure;
    private Vendor vendor;
    private SyncService syncService;


    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("14082978000") String phoneNumber, @Optional("hly123456") String pwd, @Optional("stage") String env){
        employee=getEmployee(phoneNumber,pwd,env);
        employeeManagePage =new EmployeeManagePage();
        infraStructure =new InfraStructure();
        vendor =new Vendor();
        syncService =new SyncService();
    }

    @Test(description = "新增员正常流程,")
    public void addEmployeeTest01() throws HttpStatusException, InterruptedException {
        String a = "82";
        String empID="M00"+a;
        JsonObject empObject = employeeManagePage.addEmployee(employee, "测试接口新建Q"+a,"",empID,"M00"+a+"@163.COM","人员类型01","","甄滙消费商测试公司1","测试部门A","0002","测试接口新建","职务01","级别A");
        String userOID=empObject.get("userOID").getAsString();
        JsonObject bookClass = vendor.queryBookClass(employee);
        JsonObject departCode = infraStructure.searchDepartmentDetail(employee,empObject.get("departmentOID").getAsString());
        Thread.sleep(10000);
        JsonObject userCardInfo=employeeManagePage.addUserCard(employee,userOID,CardType.CHINA_ID,"身份证名字",true);//新增身份证 启用 名字：身份证名字
        JsonArray userCardInfos = infraStructure.queryUserCard(employee,userOID);
        ArrayList cardList = syncService.addUserCardInfoDTO(userCardInfos);
        Thread.sleep(15000);
        EmployeeDTO b = syncService.addEmployeeDTO(empObject,userCardInfo,departCode,bookClass,employee,cardList);
        JsonObject c =infraStructure.queryUserSync(employee,TmcChannel.DT,"",empID);

        System.out.println(GsonUtil.objectToString(b));
        System.out.println(GsonUtil.objectToString(c));
    }

    @Test(description = "离职员工正常流程")
    public void deleteEmployeeTest02() throws HttpStatusException {
        //判断员工是否在职
        if(infraStructure.getUserDetail(employee,"zhang58062@hui.com").get("status").getAsInt() != 1001){
            //员工入职
            infraStructure.rehire(employee,"zhang58062@hui.com");
        }
        //员工离职
        int statusCode = infraStructure.leaveEmployee(employee,"zhang58062@hui.com");
        Assert.assertEquals(200,statusCode);
        //获取离职员工的status   1003 是离职状态
        int status = infraStructure.getUserDetail(employee,"zhang58062@hui.com").get("status").getAsInt();
        //对离职员工做一个断言
        Assert.assertEquals(1003,status);
    }

    @Test(description = "员工重新入职")
    public void rehireTest03() throws HttpStatusException {
        //先判断员工是否是离职的状态
        if(infraStructure.getUserDetail(employee,"zhang58062@hui.com").get("status").getAsInt() != 1003){
            infraStructure.leaveEmployee(employee,"zhang58062@hui.com");
        }
        //员工再次入职
        infraStructure.rehire(employee,"zhang58062@hui.com");
        int status = infraStructure.getUserDetail(employee,"zhang58062@hui.com").get("status").getAsInt();
        //对离职员工做一个断言
        Assert.assertEquals(1001,status);
    }

}
