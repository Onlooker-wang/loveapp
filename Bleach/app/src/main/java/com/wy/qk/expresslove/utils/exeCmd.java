package com.wy.qk.expresslove.utils;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class exeCmd {
    private static final String TAG = "exeCmd";
    private static final String COMMAND_SH = "sh";
    private static final String COMMAND_EXIT = "exit\n";
    private static final String COMMAND_LINE_END = "\n";

    private static int execRootCmdSilent(String cmd, int sleepTime) {
        Log.i(TAG, "execRootCmdSilent: cmd = " + cmd);
        int result = -1;
        DataOutputStream dos = null;

        try {
            java.lang.Process p = Runtime.getRuntime().exec(COMMAND_SH);
            dos = new DataOutputStream(p.getOutputStream());

            Log.i(TAG, cmd);
            dos.writeBytes(cmd + COMMAND_LINE_END);
            dos.flush();
            result = p.waitFor(sleepTime, TimeUnit.MILLISECONDS) ? 0 : -1;

            dos.writeBytes(COMMAND_EXIT);
            dos.flush();
            // p.waitFor();
            // result = p.exitValue();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


}
