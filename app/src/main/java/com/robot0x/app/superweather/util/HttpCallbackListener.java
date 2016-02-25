package com.robot0x.app.superweather.util;

/**
 * Created by Jackie on 2016/2/25.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
