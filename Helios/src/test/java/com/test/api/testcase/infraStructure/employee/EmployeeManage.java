package com.test.api.testcase.infraStructure.employee;

import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.infrastructure.employee.EmployeeExtendComponent;
import com.hand.basicObject.infrastructure.employee.InfraEmployee;
import com.hand.basicConstant.CardType;
import com.hand.utils.RandomNumber;
import com.hand.utils.UTCTime;
import com.test.BaseTest;
import com.test.api.method.Infra.EmployeeMethod.EmployeeManagePage;
import com.test.api.method.InfraStructure;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.*;

/**
 * @Author peng.zhang
 * @Date 2020/7/3
 * @Version 1.0
 **/
@Slf4j
public class EmployeeManage extends BaseTest {

    private Employee employee;
    private EmployeeManagePage employeeManagePage;
    private InfraEmployee infraEmployee;
    private InfraStructure infraStructure;
    //人员入参
    private String fullName = "员工"+ RandomNumber.getTimeNumber(8);
    private String employeeID = String.valueOf(RandomNumber.getRandomNumber());
    private String mobile = "283666"+ RandomNumber.getRandomNumber();
    private String alreadyMobile = "15773251211";
    private String email = String.format("%s@hui.com",RandomNumber.getRandomNumber()+RandomNumber.getUUID(4));
    private String alreadyEmail = "15773251211@sina.com";
    private String employeeTypeValueName = "技术";
    private String directManager;
    private String companyName;
    private String departmentName;
    private String departmentCode = "";
    private String position = "测试工程师";
    private String duty = "测试";
    private String rank = "技术经理";
    private String errorEmployeeID = RandomNumber.getTimeNumber(15);
    private String errorFullName = RandomNumber.getTimeNumber(15)+RandomNumber.getTimeNumber(15)+RandomNumber.getTimeNumber(15)+RandomNumber.getTimeNumber(15)+RandomNumber.getTimeNumber(15)+RandomNumber.getTimeNumber(15)+RandomNumber.getTimeNumber(15);
    private String editEmployeeID;
    private String editEmployeeUserOid;
    private String companyNameMultiplePosts ="stage测试08";
    private String departmentNameMultiplePosts="20200310072121测试";
    private String departmentCodeMultiplePosts="";//20200310072121top
    private String positionMultiplePosts="非主岗工程师";
    private String errorCompanyNameMultiplePosts ="1230";

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("hong.liang@xnhly.com") String phoneNumber, @Optional("lhfa123456") String pwd, @Optional("stage") String env){
        employee=getEmployee(phoneNumber,pwd,env);
        employeeManagePage =new EmployeeManagePage();
        infraEmployee = new InfraEmployee();
        infraStructure = new InfraStructure();
        this.directManager = employee.getFullName();
        this.companyName = employee.getCompanyName();
        this.departmentName = employee.getDepartmentName();
    }

    @Test(description = "新增员工正常流程")
    public void addEmployee01() throws HttpStatusException {
        //员工扩展字段
        EmployeeExtendComponent component =new EmployeeExtendComponent();
        component.setCustList("hong888");
        component.setText("1");
        component.setDefaultCostCenter("测试成本中心项987");
        JsonObject object = employeeManagePage.addEmployee(employee,fullName,mobile,employeeID,email,employeeTypeValueName,directManager,companyName,departmentName,departmentCode,position,duty,rank,component);
        String name = object.get("fullName").getAsString();
        log.info("获取到的人员姓名为：" + name);
        //获取新增人员后的工号，用于查询该人员数据，用于编辑
        this.editEmployeeID = object.get("employeeID").getAsString();
        this.editEmployeeUserOid = object.get("userOID").getAsString();
        Assert.assertEquals(name,fullName);
    }

    @Test(description = "新增员工异常流程--工号为空")
    public void addEmployee02() throws HttpStatusException {
        //员工扩展字段
        EmployeeExtendComponent component =new EmployeeExtendComponent();
        JsonObject object = employeeManagePage.addEmployee(employee,fullName,mobile,"",email,employeeTypeValueName,directManager,companyName,departmentName,departmentCode,position,duty,rank,component);
        String message = object.get("message").getAsString();
        String errorCode = object.get("errorCode").getAsString();
        Assert.assertEquals(message,"工号不能为空");
        Assert.assertEquals(errorCode,"6040008");
    }

    @Test(description = "新增员工异常流程--工号超20位")
    public void addEmployee03() throws HttpStatusException {
        //员工扩展字段
        EmployeeExtendComponent component =new EmployeeExtendComponent();
        JsonObject object = employeeManagePage.addEmployee(employee,fullName,mobile,"intere"+errorEmployeeID ,email,employeeTypeValueName,directManager,companyName,departmentName,departmentCode,position,duty,rank,component);
        String message = object.get("message").getAsString();
        String errorCode = object.get("errorCode").getAsString();
        log.info(message+errorCode);
        Assert.assertEquals(message,"工号长度不允许超过20位");
        Assert.assertEquals(errorCode,"6040021");
    }

    @Test(description = "手机号已存在校验")
    public void addEmployee04() throws HttpStatusException {
        //员工扩展字段
        EmployeeExtendComponent component =new EmployeeExtendComponent();
        component.setCustList("hong888");
        component.setText("1");
        component.setDefaultCostCenter("测试成本中心项987");
        JsonObject object = employeeManagePage.addEmployee(employee,fullName,alreadyMobile,employeeID,email,employeeTypeValueName,directManager,companyName,departmentName,departmentCode,position,duty,rank,component);
        String message = object.get("message").getAsString();
        String errorCode = object.get("errorCode").getAsString();
        Assert.assertEquals(message,"手机号被占用：该手机号已被绑定，请重新输入手机号");
        Assert.assertEquals(errorCode,"6040013");
    }

    @Test(description = "邮箱已存在校验")
    public void addEmployee05() throws HttpStatusException {
        //员工扩展字段
        EmployeeExtendComponent component =new EmployeeExtendComponent();
        component.setCustList("hong888");
        component.setText("1");
        component.setDefaultCostCenter("测试成本中心项987");
        JsonObject object = employeeManagePage.addEmployee(employee,fullName,"14009220010",employeeID+1,alreadyEmail,employeeTypeValueName,directManager,companyName,departmentName,departmentCode,position,duty,rank,component);
        String message = object.get("message").getAsString();
        String errorCode = object.get("errorCode").getAsString();
        Assert.assertEquals(message,"邮箱已被占用：该邮箱已被绑定，请重新输入邮箱地址");
        Assert.assertEquals(errorCode,"6040014");
    }

    @Test(description = "姓名超过100字符")
    public void addEmployee06() throws HttpStatusException {
        //员工扩展字段
        EmployeeExtendComponent component =new EmployeeExtendComponent();
        component.setCustList("hong888");
        component.setText("1");
        component.setDefaultCostCenter("测试成本中心项987");
        JsonObject object = employeeManagePage.addEmployee(employee,errorFullName,mobile,employeeID,email,employeeTypeValueName,directManager,companyName,departmentName,departmentCode,position,duty,rank,component);
        String message = object.get("message").getAsString();
        String errorCode = object.get("errorCode").getAsString();
        Assert.assertEquals(message,"姓名长度不允许超过100位");
        Assert.assertEquals(errorCode,"6040022");
    }

    @Test(description = "正常新增多岗")
    @Ignore
    public void addEmployee07() throws HttpStatusException {
        //员工扩展字段
        EmployeeExtendComponent component =new EmployeeExtendComponent();
        component.setCustList("hong888");
        component.setText("1");
        component.setDefaultCostCenter("测试成本中心项987");
        JsonObject object = employeeManagePage.addEmployeeMultiplePosts(employee,fullName,mobile,employeeID,email,employeeTypeValueName,directManager,companyName,companyNameMultiplePosts,departmentName,departmentNameMultiplePosts,departmentCode,departmentCodeMultiplePosts,position,positionMultiplePosts,duty,rank,true,false,component);
        String name = object.get("fullName").getAsString();
        log.info("获取到的人员姓名为：" + name);
        Assert.assertEquals(name,fullName);
    }

    @Test(description = "异常新增多岗-同一个公司下2个主岗")
    @Ignore
    public void addEmployee08() throws HttpStatusException {
        //员工扩展字段
        EmployeeExtendComponent component =new EmployeeExtendComponent();
        component.setCustList("hong888");
        component.setText("1");
        component.setDefaultCostCenter("测试成本中心项987");
        JsonObject object = employeeManagePage.addEmployeeMultiplePosts(employee,fullName,mobile,employeeID,email,employeeTypeValueName,directManager,companyName,companyName,departmentName,departmentName,departmentCode,departmentCode,position,position,duty,rank,true,true,component);
        String message = object.get("message").getAsString();
        String errorCode = object.get("errorCode").getAsString();
        Assert.assertEquals(message,"同一用户公司不能设置多条主岗位");
        Assert.assertEquals(errorCode,"6040031");
    }

    @Test(description = "异常新增多岗-部门-公司-职位相同")
    @Ignore
    public void addEmployee09() throws HttpStatusException {
        //员工扩展字段
        EmployeeExtendComponent component =new EmployeeExtendComponent();
        component.setCustList("hong888");
        component.setText("1");
        component.setDefaultCostCenter("测试成本中心项987");
        JsonObject object = employeeManagePage.addEmployeeMultiplePosts(employee,fullName,mobile,employeeID,email,employeeTypeValueName,directManager,companyName,companyName,departmentName,departmentName,departmentCode,departmentCode,position,position,duty,rank,true,false,component);
        String errorCode = object.get("errorCode").getAsString();
        Assert.assertEquals(errorCode,"6040029");
    }

    @Test(description = "异常新增多岗-岗位跨账套")
    @Ignore
    public void addEmployee010() throws HttpStatusException {
        //员工扩展字段
        EmployeeExtendComponent component =new EmployeeExtendComponent();
        component.setCustList("hong888");
        component.setText("1");
        component.setDefaultCostCenter("测试成本中心项987");
        JsonObject object = employeeManagePage.addEmployeeMultiplePosts(employee,fullName,mobile,employeeID,email,employeeTypeValueName,directManager,companyName,errorCompanyNameMultiplePosts,departmentName,departmentName,departmentCode,departmentCode,position,position,duty,rank,true,false,component);
        String message = object.get("message").getAsString();
        String errorCode = object.get("errorCode").getAsString();
        Assert.assertEquals(message,"用户岗位只允许同一账套下公司");
        Assert.assertEquals(errorCode,"6040034");
    }

    @Test(description = "编辑员工-正常编辑-修改了邮箱,手机号以及生日")
    public void editEmployee() throws HttpStatusException {
        InfraEmployee infraEmployee =new InfraEmployee();
        infraEmployee.setMobile("130000"+RandomNumber.getRandomNumber());
        infraEmployee.setEmail(RandomNumber.getTimeNumber()+"@123.com");
        infraEmployee.setBirthday(UTCTime.getBeijingDate(-2000));
        assert employeeManagePage.editEmployee(employee,editEmployeeID,infraEmployee).toString().contains("fullName");
    }

    @DataProvider(name = "CardError01") //姓名校验
    public static Object[][] addCardError01() {
        return new Object[][] {
                {CardType.PASSPORT,"护照","huzhao",true,"0","2020-07-29T10:25:11+08:00","CN",false,"431225189087654","2022-07-29T10:25:11+08:00"},
                {CardType.MAINLAND,"台胞证","taibaozheng",true,"0","2020-07-29T10:25:11+08:00","CN",false,"431225189087654","2022-07-29T10:25:11+08:00"},
                {CardType.HOME_RETURN_PERMIT,"回乡证","huixiangzheng",true,"0","2020-07-29T10:25:11+08:00","CN",false,"431225189087654","2022-07-29T10:25:11+08:00"},
                {CardType.HONG_KONG,"港澳通行证","gangaotongxingzheng",true,"0","2020-07-29T10:25:11+08:00","CN",false,"431225189087654","2022-07-29T10:25:11+08:00"},
                {CardType.PERMANENT_RESIDENCE,"外国人永久居留身份证","waiguorenyongjiujuliuzheng",true,"0","2020-07-29T10:25:11+08:00","CN",false,"431225189087654","2022-07-29T10:25:11+08:00"},
                {CardType.TRAVEL,"旅行证","lvxingzheng",true,"0","2020-07-29T10:25:11+08:00","CN",false,"431225189087654","2022-07-29T10:25:11+08:00"},
                {CardType.TAIWAN,"台湾通行证","taiwantongxingzheng",true,"0","2020-07-29T10:25:11+08:00","CN",false,"431225189087654","2022-07-29T10:25:11+08:00"},
                {CardType.PASSPORT,"","huzhao",true,"0","2020-07-29T10:25:11+08:00","CN",false,"431225189087654","2022-07-29T10:25:11+08:00"},
                {CardType.MAINLAND,"","taibaozheng",true,"0","2020-07-29T10:25:11+08:00","CN",false,"431225189087654","2022-07-29T10:25:11+08:00"},
                {CardType.HOME_RETURN_PERMIT,"","huixiangzheng",true,"0","2020-07-29T10:25:11+08:00","CN",false,"431225189087654","2022-07-29T10:25:11+08:00"},
                {CardType.HONG_KONG,"","gangaotongxingzheng",true,"0","2020-07-29T10:25:11+08:00","CN",false,"431225189087654","2022-07-29T10:25:11+08:00"},
                {CardType.PERMANENT_RESIDENCE,"","waiguorenyongjiujuliuzheng",true,"0","2020-07-29T10:25:11+08:00","CN",false,"431225189087654","2022-07-29T10:25:11+08:00"},
                {CardType.TRAVEL,"","lvxingzheng",true,"0","2020-07-29T10:25:11+08:00","CN",false,"431225189087654","2022-07-29T10:25:11+08:00"},
                {CardType.TAIWAN,"","taiwantongxingzheng",true,"0","2020-07-29T10:25:11+08:00","CN",false,"431225189087654","2022-07-29T10:25:11+08:00"}
        };
    }


    @DataProvider(name = "CardError02")  //国籍校验
    public static Object[][] addCardError02() {
        return new Object[][] {
                {CardType.CHINA_ID,"身份证","shenfenzheng",true,"0","2020-07-29T10:25:11+08:00","TW",false,"431225189080001","2022-07-29T10:25:11+08:00"},
                {CardType.HONG_KONG,"gangaotongxingzheng","gangao",true,"0","2020-07-29T10:25:11+08:00","HK",false,"431225189080005","2022-07-29T10:25:11+08:00"},
                {CardType.MILITARY,"军人证","junrenzheng",true,"0","2020-07-29T10:25:11+08:00","MO",false,"431225189080007","2022-07-29T10:25:11+08:00"},
                {CardType.TRAVEL,"lvxingzheng","lvxingzheng",true,"0","2020-07-29T10:25:11+08:00","MO",false,"431225189080008","2022-07-29T10:25:11+08:00"},
                {CardType.TAIWAN,"taiwantongxingzheng","taiwan",true,"0","2020-07-29T10:25:11+08:00","MO",false,"431225189080009","2022-07-29T10:25:11+08:00"}
        };
    }

    @DataProvider(name = "CardError03")  //姓名必填校验
    public static Object[][] addCardError03() {
        return new Object[][] {
                {CardType.PASSPORT,"huzhao","",true,"0","2020-07-29T10:25:11+08:00","CN",false,"431225189087654","2022-07-29T10:25:11+08:00"},
                {CardType.MAINLAND,"taibaozheng","",true,"0","2020-07-29T10:25:11+08:00","CN",false,"431225189087654","2022-07-29T10:25:11+08:00"},
                {CardType.HOME_RETURN_PERMIT,"huixiangzheng","",true,"0","2020-07-29T10:25:11+08:00","CN",false,"431225189087654","2022-07-29T10:25:11+08:00"},
                {CardType.HONG_KONG,"gangaotongxingzheng","",true,"0","2020-07-29T10:25:11+08:00","CN",false,"431225189087654","2022-07-29T10:25:11+08:00"},
                {CardType.PERMANENT_RESIDENCE,"waiguorenyongjiujuliuzheng","",true,"0","2020-07-29T10:25:11+08:00","CN",false,"431225189087654","2022-07-29T10:25:11+08:00"},
                {CardType.TRAVEL,"lvxingzheng","",true,"0","2020-07-29T10:25:11+08:00","CN",false,"431225189087654","2022-07-29T10:25:11+08:00"},
                {CardType.TAIWAN,"taiwantongxingzheng","",true,"0","2020-07-29T10:25:11+08:00","CN",false,"431225189087654","2022-07-29T10:25:11+08:00"},
                {CardType.CHINA_ID,"身份证","",true,"0","2020-07-29T10:25:11+08:00","CN",false,"431225189080001","2022-07-29T10:25:11+08:00"},
                {CardType.MILITARY,"军人证","",true,"0","2020-07-29T10:25:11+08:00","CN",false,"431225189080007","2022-07-29T10:25:11+08:00"}
        };
    }

    @DataProvider(name = "Card")
    public static Object[][] addCard() {
        return new Object[][] {
                {CardType.CHINA_ID,"身份证","shenfenzheng",true,"0","2020-07-29T10:25:11+08:00","CN",false,"431225189080001","2022-07-29T10:25:11+08:00"},
                {CardType.PASSPORT,"huzhao","huzhao",true,"0","2020-07-29T10:25:11+08:00","CN",false,"431225189080002","2022-07-29T10:25:11+08:00"},
                {CardType.MAINLAND,"taibaozheng","taibaozheng",true,"0","2020-07-29T10:25:11+08:00","TW",false,"431225189080003","2022-07-29T10:25:11+08:00"},
                //回乡证 MO HK
                {CardType.HOME_RETURN_PERMIT,"huixiangzheng","huixiangzheng",true,"0","2020-07-29T10:25:11+08:00","HK",false,"431225189080004","2022-07-29T10:25:11+08:00"},
                {CardType.HONG_KONG,"gangaotongxingzheng","gangao",true,"0","2020-07-29T10:25:11+08:00","CN",false,"431225189080005","2022-07-29T10:25:11+08:00"},
                {CardType.PERMANENT_RESIDENCE,"yongjiujuliuzheng","waiguoren",true,"0","2020-07-29T10:25:11+08:00","CN",false,"431225189080006","2022-07-29T10:25:11+08:00"},
                {CardType.MILITARY,"军人证","junrenzheng",true,"0","2020-07-29T10:25:11+08:00","CN",false,"431225189080007","2022-07-29T10:25:11+08:00"},
                {CardType.TRAVEL,"lvxingzheng","lvxingzheng",true,"0","2020-07-29T10:25:11+08:00","CN",false,"431225189080008","2022-07-29T10:25:11+08:00"},
                {CardType.TAIWAN,"taiwantongxingzheng","taiwan",true,"0","2020-07-29T10:25:11+08:00","CN",false,"431225189080009","2022-07-29T10:25:11+08:00"}
        };
    }

    @Test(description = "异常新增证件，包含中文或名为空",dataProvider = "CardError01")
    public void addEmployeeCard01(CardType cardType,String firstName,String lastName,boolean enabled,String gender,
                                  String birthday,String nationalityCode,Boolean defaultCard, String cardNo,
                                  String cardExpiredTime) throws HttpStatusException {
        //员工扩展字段
        EmployeeExtendComponent component =new EmployeeExtendComponent();
        component.setCustList("hong888");
        component.setText("1");
        JsonObject object = employeeManagePage.addUserCard(employee,editEmployeeUserOid,cardType,firstName,lastName,enabled,
                gender,birthday,nationalityCode,defaultCard,cardNo,cardExpiredTime);
        String message = object.get("message").getAsString();
        String errorCode = object.get("errorCode").getAsString();
        Assert.assertEquals(message,"护照、台胞证、回乡证、港澳通行证、台湾通行证、旅行证、外国人永久居留身份证的姓和名只能为英文字母（包括大小写）、“.”  、 “_”");
        Assert.assertEquals(errorCode,"13842@CONTACT_CARD_ERROR_005");
    }

    @Test(description = "异常新增证件，国籍匹配不正确",dataProvider = "CardError02")
    public void addEmployeeCard02(CardType cardType,String firstName,String lastName,boolean enabled,String gender,
                                  String birthday,String nationalityCode,Boolean defaultCard, String cardNo,
                                  String cardExpiredTime) throws HttpStatusException {
        //员工扩展字段
        EmployeeExtendComponent component =new EmployeeExtendComponent();
        component.setCustList("hong888");
        component.setText("1");
        JsonObject object = employeeManagePage.addUserCard(employee,editEmployeeUserOid,cardType,firstName,lastName,enabled,
                gender,birthday,nationalityCode,defaultCard,cardNo,cardExpiredTime);
        String message = object.get("message").getAsString();
        String errorCode = object.get("errorCode").getAsString();
        Assert.assertEquals(message,"身份证,军人证,台湾通行证,港澳通行证,旅行证国籍只能为中国大陆");
        Assert.assertEquals(errorCode,"CONTACT_CARD_ERROR_001");
    }

    @Test(description = "异常新增证件，国籍不存在")
    public void addEmployeeCard03() throws HttpStatusException {
        //员工扩展字段
        EmployeeExtendComponent component =new EmployeeExtendComponent();
        component.setCustList("hong888");
        component.setText("1");
        JsonObject object = employeeManagePage.addUserCard(employee,editEmployeeUserOid,CardType.PASSPORT,"huzhao",
                "huzhao",true,"0","2020-07-29T10:25:11+08:00",
                "Q1",false,"431225189080002","2022-07-29T10:25:11+08:00");
        String message = object.get("message").getAsString();
        String errorCode = object.get("errorCode").getAsString();
        Assert.assertEquals(message,"国籍编码不存在");
        Assert.assertEquals(errorCode,"250009");
    }

    @Test(description = "异常新增证件，台胞证国籍只能为中国台湾")
    public void addEmployeeCard04() throws HttpStatusException {
        //员工扩展字段
        EmployeeExtendComponent component =new EmployeeExtendComponent();
        component.setCustList("hong888");
        component.setText("1");
        JsonObject object = employeeManagePage.addUserCard(employee,editEmployeeUserOid,CardType.MAINLAND,"taibaozheng",
                "taibaozheng",true,"0","2020-07-29T10:25:11+08:00","CN",
                false,"431225189080003","2022-07-29T10:25:11+08:00");
        String message = object.get("message").getAsString();
        String errorCode = object.get("errorCode").getAsString();
        Assert.assertEquals(message,"台胞证国籍只能为中国台湾");
        Assert.assertEquals(errorCode,"CONTACT_CARD_ERROR_002");
    }

    @Test(description = "异常新增证件，回乡证国籍只能为中国香港和中国澳门")
    public void addEmployeeCard05() throws HttpStatusException {
        //员工扩展字段
        EmployeeExtendComponent component =new EmployeeExtendComponent();
        component.setCustList("hong888");
        component.setText("1");
        JsonObject object = employeeManagePage.addUserCard(employee,editEmployeeUserOid,CardType.HOME_RETURN_PERMIT,"huixiangzheng",
                "huixiangzheng",true,"0","2020-07-29T10:25:11+08:00","CN",
                false,"431225189080004","2022-07-29T10:25:11+08:00");
        String message = object.get("message").getAsString();
        String errorCode = object.get("errorCode").getAsString();
        Assert.assertEquals(message,"回乡证国籍只能为中国香港和中国澳门");
        Assert.assertEquals(errorCode,"CONTACT_CARD_ERROR_003");
    }

    @Test(description = "异常新增证件，国籍匹配不正确",dataProvider = "CardError03")
    public void addEmployeeCard06(CardType cardType,String firstName,String lastName,boolean enabled,String gender,
                                  String birthday,String nationalityCode,Boolean defaultCard, String cardNo,
                                  String cardExpiredTime) throws HttpStatusException {
        //员工扩展字段
        EmployeeExtendComponent component =new EmployeeExtendComponent();
        component.setCustList("hong888");
        component.setText("1");
        JsonObject object = employeeManagePage.addUserCard(employee,editEmployeeUserOid,cardType,firstName,lastName,enabled,
                gender,birthday,nationalityCode,defaultCard,cardNo,cardExpiredTime);
        String message = object.get("message").getAsString();
        String errorCode = object.get("errorCode").getAsString();
        Assert.assertEquals(message,"身份证,军人证姓名不能为空,其它证件姓不能为空");
        Assert.assertEquals(errorCode,"250004");
    }

    @Test(description = "正常新增证件",dataProvider = "Card")
    public void addEmployeeCard07(CardType cardType,String firstName,String lastName,boolean enabled,String gender,
                                  String birthday,String nationalityCode,Boolean defaultCard, String cardNo,
                                  String cardExpiredTime) throws HttpStatusException {
        //员工扩展字段
        EmployeeExtendComponent component =new EmployeeExtendComponent();
        component.setCustList("hong888");
        component.setText("1");
        assert employeeManagePage.addUserCard(employee,editEmployeeUserOid,cardType,firstName,lastName,enabled,
                gender,birthday,nationalityCode,defaultCard,cardNo,cardExpiredTime).toString().contains(editEmployeeUserOid);
    }
}
