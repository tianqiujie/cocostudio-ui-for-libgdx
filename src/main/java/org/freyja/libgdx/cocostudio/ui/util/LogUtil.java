package org.freyja.libgdx.cocostudio.ui.util;

import com.badlogic.gdx.Gdx;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tian on 2016/3/19.
 */
public class LogUtil {
    public static boolean debug = true;
    public static final String LOG = "------------------->";

    public static void Log(Object msg){
        Date date=new Date();
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=format.format(date);

        if (debug)
            Gdx.app.log(time + LOG, msg.toString());
    }
}
