package com.glandroid.weatherreport;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Xml;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWeatherInfo();
    }

    /**
     * 获取天气信息
     * 对XML文件的解析
     */
    private void getWeatherInfo() {
        try {
            XmlPullParser parser = Xml.newPullParser();
            InputStream is = getAssets().open("getWeatherbyCityName.xml");
            parser.setInput(is, "utf-8");
            ArrayList<String> infos = new ArrayList<>();
            int type = parser.getEventType();
            while (type != XmlPullParser.END_DOCUMENT) {
                if ("string".equals(parser.getName())) {
                    String info = parser.nextText();
                    infos.add(info);
                }
                type = parser.next();
            }
            is.close();
            String cityname = infos.get(0);
            String temp = infos.get(1);
            String weather = infos.get(2);
            String wind = infos.get(3);
            String wearinfo = infos.get(4);
            TextView tv = (TextView) findViewById(R.id.tv_info);
            tv.setText("城市名称：" + cityname + "\n温度：" + temp + "\n天气信息："
                    + weather + "\n风力：" + wind + "\n穿衣指数：" + wearinfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
