package com.hand.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

/**
 * @Author peng.zhang
 * @Date 2020/12/23
 * @Version 1.0
 **/
@Slf4j
public class DButil {

    private final static String mysqlDriver = "com.mysql.jdbc.Driver";
    private final static String devMysqlUrl = "jdbc:mysql://106.15.120.125:21906/atl_mid_push";
    private final static String userName = "artemis";
    private final static String password = "123456Ms3";

    public static Business dbConnection(String businessCode) throws ClassNotFoundException {
        Class.forName(mysqlDriver);
        String sql = "SELECT * FROM `mid_business_document` d  WHERE d.business_code = ?";
        Connection connection=null;
        PreparedStatement preparedStatement= null;
        ResultSet resultSet =null;
        try {
            log.info("dev数据库连接");
            connection = DriverManager.getConnection(devMysqlUrl, userName, password);
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,businessCode);
            resultSet = preparedStatement.executeQuery();
            Business business = new Business();
            if(resultSet != null){
                while(resultSet.next()){
                    business.setBusinessCode(resultSet.getString("business_code"));
                    business.setApiCode(resultSet.getString("api_code"));
                }
            }
            return business;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }finally {
            //关闭资源
            try {
                if(connection!=null){
                    connection.close();
                }
                if(preparedStatement != null){
                    preparedStatement.close();
                }
                if(resultSet!= null){
                    resultSet.close();
                }
                log.info("关闭连接");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Data
    public static class Business{
        private String businessCode;
        private String apiCode;
    }
}
