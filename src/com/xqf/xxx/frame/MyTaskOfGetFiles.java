package com.xqf.xxx.frame;

/**
 * Created by XQF on 2016/12/27.
 */
public class MyTaskOfGetFiles implements Runnable {
    @Override
    public void run() {
        new GetFilesOfPc();
    }
}
