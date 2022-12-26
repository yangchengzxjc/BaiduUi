package com.test.api.testcase.UiAndintreF;

import org.junit.Test;

/**
 * @Author yc
 * @Date 2022/12/9
 * @Version 1.0
 **/
public class BollDrop01 {
    private double a;
    private double b;
    private double an;
    private int n;

    public void geometricProgression(int a,double b,int n){
        an = a;    //初始化an=a1=300
        for (int i = 1; i < n; i++) {
            if(an > 0.00001){
                an *= b;  //an=a(n-1）*q ，其中q表示等比数列的比，an表示第n项，a(n-1）表示an前一项
                System.out.println("i" + i);
                System.out.println(an);
            }
            else{
                System.out.println("game over");
            }
        }
    }

    @Test
    public void voBollDropTest01(){
        BollDrop01 boodrop = new BollDrop01();
        boodrop.geometricProgression(300,0.25,20);
    }
}
