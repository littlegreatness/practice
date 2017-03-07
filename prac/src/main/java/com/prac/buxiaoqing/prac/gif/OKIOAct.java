package com.prac.buxiaoqing.prac.gif;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.prac.buxiaoqing.prac.R;

import java.io.ByteArrayOutputStream;
import java.io.File;

import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;

public class OKIOAct extends AppCompatActivity {

    private String TAG = "buxq";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okio);
    }


    /**
     * 读写
     *
     * @param view
     * @throws Exception
     */
    public void readWriteFile(View view) {
        try {
            File file = new File(getApplicationContext().getFilesDir().getPath().toString() + "/readWriteFile");
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedSink sink = Okio.buffer(Okio.sink(file));
            sink.writeUtf8("hello ,OKIO file");
            sink.close();
            BufferedSource source = Okio.buffer(Okio.source(file));
            Log.e(TAG, "readWriteFile: " + source.readUtf8());
            source.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 追写文件
     *
     * @throws Exception
     */
    public void appendFile(View view) {
        try {
            File file = new File(getApplicationContext().getFilesDir().getPath().toString() + "/appendFile");
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedSink sink = Okio.buffer(Okio.appendingSink(file));
            sink.writeUtf8("Helllo,");
            sink.close();
            sink = Okio.buffer(Okio.appendingSink(file));
            sink.writeUtf8(" java.io file");
            sink.close();
            BufferedSource source = Okio.buffer(Okio.source(file));
            Log.e(TAG, "readWriteFile: " + source.readUtf8());
            source.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读流
     *
     * @throws Exception
     */
    public void sinkFromOutputStream(View view) throws Exception {
        try {
            Buffer data = new Buffer();
            data.writeUtf8("a");
            data.writeUtf8("bbbbbbbbb");
            data.writeUtf8("c");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Sink sink = Okio.sink(out);
            sink.write(data, 3);
            Log.e(TAG, "sinkFromOutputStream: " + data.readUtf8());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
