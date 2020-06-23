package com.hand.baseMethod;

/**
 * @Author ： xinghong.chen
 * @Email: xinghong.chen@huilianyi.com
 * @DATE : 2019/3/20 2:43
 **/

public class ThreadLocalUtil<T> {
    // 设置当前线程变量
    public void setThreadValue(ThreadLocal<T> threadLocal, T value){
        if (threadLocal.get() == null ){
            threadLocal.set( value );
        }
    }

    // 获得当前线程变量的值
    public T getThreadValue(ThreadLocal<T> threadLocal){
        return threadLocal.get();
    }
}
