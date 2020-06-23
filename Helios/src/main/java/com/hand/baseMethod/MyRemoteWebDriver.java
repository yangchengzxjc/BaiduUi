package com.hand.baseMethod;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.SessionStorage;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.interactions.HasTouchScreen;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.remote.RemoteTouchScreen;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.html5.RemoteLocalStorage;
import org.openqa.selenium.remote.html5.RemoteSessionStorage;

import java.net.URL;

/**
 * Created by 陈星宏 on 2019/7/2.
 */
public class MyRemoteWebDriver extends RemoteWebDriver implements HasTouchScreen, WebStorage {
    public TouchScreen touchScreen;

    public MyRemoteWebDriver(URL remoteAddress, Capabilities capabilities){
        super(remoteAddress, capabilities);
        this.touchScreen = new RemoteTouchScreen(this.getExecuteMethod());
    }

    @Override
    public TouchScreen getTouch() {
        return this.touchScreen;
    }

    @Override
    public LocalStorage getLocalStorage() {
        return new RemoteLocalStorage(this.getExecuteMethod());
    }

    @Override
    public SessionStorage getSessionStorage() {
        return new RemoteSessionStorage(this.getExecuteMethod());
    }
}
