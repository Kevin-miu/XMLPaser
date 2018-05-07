package com.glandroid.xmlparser;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tv;
    private Button bt_dom, bt_sax, bt_pull;
    private XmlUtils xmlUtils;
    private List<Student> students;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xml);

        tv = (TextView) findViewById(R.id.tv);
        bt_dom = (Button) findViewById(R.id.bt_dom);
        bt_sax = (Button) findViewById(R.id.bt_sax);
        bt_pull = (Button) findViewById(R.id.bt_pull);

        bt_dom.setOnClickListener(this);
        bt_sax.setOnClickListener(this);
        bt_pull.setOnClickListener(this);

        xmlUtils = new XmlUtils();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_dom:
                try {
                    students = xmlUtils.dom2xml(getResources().getAssets().open("student.xml"));
                    tv.setText(students.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_sax:
                try {
                    students = xmlUtils.sax2xml(getResources().getAssets().open("student.xml"));
                    tv.setText(students.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_pull:
                try {
                    students = xmlUtils.pull2xml(getResources().getAssets().open("student.xml"));
                    tv.setText(students.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
