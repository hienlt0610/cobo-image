package dev.testcode.room;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;

/**
 * Created by hienl on 1/10/2018.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        FFmpeg.getInstance(this);
    }
}
