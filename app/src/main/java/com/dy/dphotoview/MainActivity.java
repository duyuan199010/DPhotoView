package com.dy.dphotoview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.duyuan.photoview.PhotoActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayList<String> photos = new ArrayList<>();
        //photos.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1486649968368&di=f951a41839ef31be231fb839d2c7efbf&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fwallpaper%2F1306%2F17%2Fc0%2F22190407_1371452382110.jpg");
        photos.add(
                "http://pic109.nipic.com/file/20160914/4516668_215919019000_2.jpg");
        photos.add("http://pic108.nipic.com/file/20160901/4516668_210829551000_2.jpg");
        photos.add("http://pic109.nipic.com/file/20160909/4516668_153420589000_2.jpg");
        findViewById(R.id.photo).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {

                PhotoActivity.actionStart(MainActivity.this, photos);
            }
        });
    }
}
