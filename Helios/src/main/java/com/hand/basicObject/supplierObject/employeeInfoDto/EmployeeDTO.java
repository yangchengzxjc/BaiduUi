package com.hand.basicObject.supplierObject.employeeInfoDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class EmployeeDTO {
    //审批单状态
    private String status;
    //汇联易系统员工名 fullName
    private String fullName;
    //员工工号
    private String employeeID;
    //员工手机号
    private String mobile;
    //员工邮箱
    private String email;
    //员工身份证名字
    private String name;
    //护照英文名_F
    private String enFirstName;
    //护照中文名_L
    private String enLastName;
    //国籍
    private String nationality;
    //性别
    private String gender;
    //级别
    private String rankName;
    //国内机票两舱是否可预订 T:是，F：否，为空则不管控
    private String isBookClass;
    //国际屏蔽舱位控制 C公务 F头等 Y经济 SY超经
    private String intlBookClassBlock;
    //租户id
    private String tenantId;
    //公司ID
    private String companyId;
    //公司OID
    private String companyOID;
    //公司code
    private String companyCode;
    //部门code
    private String deptCode;
    //部门code编码 自定义编码
    private String deptCustomCode;
    //部门名称
    private String deptName;
    //部门全路径
    private String deptPath;
    //证件信息
    private List<UserCardInfoDTO> userCardInfos;
    //扩展字段
    private List<CustomFormValueDTO> customFormValues;


}
