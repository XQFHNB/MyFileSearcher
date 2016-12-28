package com.xqf.xxx.frame;


import com.xqf.xxx.db.DBHelper;

import java.util.TimerTask;

/**
 * 更新来个暴力的，先将其删除，。，。再全部插入一遍，，，厉害吧，没时间了，搞起再说
 * Created by XQF on 2016/12/27.
 */
public class MyTimerTask extends TimerTask {

    @Override
    public void run() {
        DBHelper.getDbHelper().updateDB();
    }
}
