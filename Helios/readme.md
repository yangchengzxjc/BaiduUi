## git操作

### fork分支merge主项目
- 把被fork项目添加到远程列表
```
git remote add team https://code.huilianyi.com/auto-test/helios-cn.git
```
- 拉取被fork项目更新并合并
```$xslt
git fetch team
git merge team/master
// 或者直接pull
git pull team master
```
### 提交代码到被fork分支
```$xslt
git push team master:master
```

## testNG
### 常用注解、参数
- 注解

| 注解 | 描述 |
| --- | --- |
| @BeforeSuite | 在该套件的所有测试都运行在注释的方法之前，仅运行一次。 |
| @AfterSuite | 在该套件的所有测试都运行在注释方法之后，仅运行一次。|
| @BeforeClass | 在调用当前类的第一个测试方法之前运行，注释方法仅运行一次。|
| @AfterClass | 在调用当前类的第一个测试方法之后运行，注释方法仅运行一次 |
| @BeforeTest | 注释的方法将在属于<test>标签内的类的所有测试方法运行之前运行。|
| @AfterTest | 注释的方法将在属于<test>标签内的类的所有测试方法运行之后运行。|
| @BeforeGroups | 配置方法将在之前运行组列表。 此方法保证在调用属于这些组中的任何一个的第一个测试方法之前不久运行。|
| @AfterGroups | 此配置方法将在之后运行组列表。该方法保证在调用属于任何这些组的最后一个测试方法之后不久运行。|
| @BeforeMethod | 注释方法将在每个测试方法之前运行。|
| @AfterMethod | 注释方法将在每个测试方法之后运行。 |
| @DataProvider | 标记一种方法来提供测试方法的数据。 注释方法必须返回一个Object [] []，其中每个Object []可以被分配给测试方法的参数列表。 要从该DataProvider接收数据的@Test方法需要使用与此注释名称相等的dataProvider名称。||
| @Factory | 将一个方法标记为工厂，返回TestNG将被用作测试类的对象。 该方法必须返回Object []。 |
| @Listeners | 定义测试类上的侦听器。|
| @Parameters | 描述如何将参数传递给@Test方法。 |
| @Test | 将类或方法标记为测试的一部分。支持参数enable expectedExceptions timeOut groups dependsOnMethods dependsOnGroups |
|||

- 常用断言
```
assertEqual([String message], expected_value, actual_value)
assertTrue([String message], boolean condition) 
assertFalse([String message],boolean condition) 
assertNotNull([String message], java.lang.Object object) 
assertNull([String message], java.lang.Object object) 
assertSame([String message], java.lang.Object expected, java.lang.Object actual)
assertNotSame([String message], java.lang.Object unexpected, java.lang.Object actual)
assertArrayEquals([String message], expectedArray, resultArray)
```

### 用例组织
测试套件：创建一个XML文件 `testng.xml` 
- xml文件标签说明
```
<suite>  套件，根标签，通常由几个<test组成>
  属性：
　　name            套件的名称，必须属性；
　　verbose         运行的级别或详细程度；
　　parallel        是否运行多线程来运行这个套件；
　　thread-count    如果启用多线程，用于指定开户的线程数；
　　annotations     在测试中使用的注释类型；
　　time-out        在本测试中的所有测试方法上使用的默认超时时间； 
<test> 　　 测试用例，name为必须属性；
<classes>  用例中包含的类，子标签为<class name=”className”>;
<class>    测试类，其中属性name为必须属性；;
<packages> 用例中包含的包，包中所有的方法都会执行，子标签为<package name=”packageName”>;
<package>  测试包，name为必须属性；
<methods>  指定测试类中包含或排除的方法，子类为<include>,<exclude>;
<include>  指定需要测试的方法，name为必须属性；
<exclude>  指定类中不需要测试的方法，name为必须属性；
<groups>   指定测试用例中要运行或排除运行的分组，子标签为<run>,<run>下包含<include>,<exclude>标签，<include>,<exclude>的name指定运行、不运行的分组；
```
- package 运行测试包下所有方法
```xml
<suite name="TestAll">

    <test name="order">
        <packages>
            <package name="com.helios.*" />
        </packages>
    </test>

</suite>
```
- class 运行指定测试类
```xml
<?xml version="1.0" encoding="UTF-8"?>
<suite name="TestAll">

    <test name="order">
        <classes>
            <class name="com.helios.TestConfig" />
            <class name="com.helios.TestOrder" />
        </classes>
    </test>

    <test name="database">
        <classes>
            <class name="com.helios.TestConfig" />
            <class name="com.helios.TestDatabase" />
        </classes>
    </test>

</suite>
```
- group 运行注解为某个group的case
```xml
<?xml version="1.0" encoding="UTF-8"?>
<suite name="TestAll">

  <test name="database">
    <groups>
        <run>
            <exclude name="brokenTest" />
            <include name="dbTest" />
        </run>
    </groups>

    <classes>
        <class name="com.helios.TestDatabase" />
    </classes>
  </test>

</suite>
```
- method 运行特定方法
```xml
<?xml version="1.0" encoding="UTF-8"?>
<suite name="TestAll">

  <test name="order">
    <classes>
        <class name="com.helios.TestConfig" />
        <class name="com.helios.TestOrder">
            <methods>
                <include name="testMakeOrder" />
                <exclude name="testUpdateOrder" />
            </methods>
        </class>
    </classes>
  </test>

</suite>
```
- 执行顺序
    - class - `testng.xml` 之 `preserve-order` 
    > \<test\>中默认的preserve-order为true，表示\<test\>下所有\<classes\>按照顺序执行。
    一个\<class\>类里面可能存在多个测试方法(被@Test注解的方法)，
    这些方法的执行顺序不受preserve-order控制。默认测试方法的执行顺序是按照方法名的首字母升序排序执行的。 
    - test
        - 默认执行顺序是按照方法名的首字母升序排序执行
        - 使用 priority 指定执行顺序(默认值为0)，数值越小，越靠前执行
        - 在xml里面使用 \<include\> 指定需要执行的方法和顺序
    
### 参数化
- XML传递参数
```xml
<?xml version="1.0" encoding="UTF-8"?>
<suite name="test-parameter">

    <test name="example1">

        <!--先创建xxx.properties文件-->
        <!--然后xml中定义为 parameter 的参数值 -->
        <parameter name="config" value="xxx.properties" />
        <parameter name="param" value="param的值" />

        <classes>
            <class name="com.helios.TestParameterXML" />
        </classes>

    </test>

</suite>
```
- @DataProvider 注解
> 被注解的方法可以接收 无参(直接返回数据)、对象、方法 Method 、上下文 ITestContext 
```java
package roger.testng;

import java.util.Random;

import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/*
 *  数据提供者在方法签名中声明了一个 ITestContext 类型的参数
 *  testng 会将当前的测试上下文设置给它
 *  
 */
public class TestDataProviderITestContext {
    @DataProvider
    public Object[][] randomIntegers(ITestContext context) {
        String[] groups= context.getIncludedGroups();
        int size = 2;
        for (String group : groups) {
            System.out.println("--------------" + group);
            if (group.equals("function-test")) {
                size = 10;
                break;
            }
        }

        Object[][] result = new Object[size][];
        Random r = new Random();
        for (int i = 0; i < size; i++) {
            result[i] = new Object[] {new Integer(r.nextInt())};
        }

        return result;
    }

    // 如果在 unite-test 组中执行, 将返回2个随机整数构成数组;
    // 如果在 function-test 组中执行, 将返回 10 个随机整数构成数组
    @Test(dataProvider = "randomIntegers", groups = {"unit-test", "function-test"})
    public void random(Integer n) {
        System.out.println(n);
    }

}
```

### 用例编写步骤
- 定义接口路径：Helios/src/main/java/com/hand/basicconstant/ApiPath.java
- 实现接口请求：Helios/src/main/java/com/hand/api/VendorApi.java
- 调用接口方法类：Helios/src/test/java/com/test/api/method/Vendor.java
- 编写测试case：Helios/src/test/java/com/test/api/testcase/vendor/VendorSSOTest.java
- 用例集组织case：Helios/vendor_testng.xml

## toDo
- 请求方法封装，log增加请求request header、请求参数、response header、code、返回结果 DONE
- 测试报告增加请求路径 参数 返回结果输出 → 新增监听类`ExtentTestNGIReporterListener` DONE
