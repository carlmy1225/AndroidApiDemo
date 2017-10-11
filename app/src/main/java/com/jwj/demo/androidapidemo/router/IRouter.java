package com.jwj.demo.androidapidemo.router;

import android.content.Context;
import android.os.Bundle;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/9/29
 * Copyright: Ctrip
 */

public interface IRouter {
    String CONFIG_FILE = "router_config";

    void goTo(Context context, String page, Bundle bundle);
}
