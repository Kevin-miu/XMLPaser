package com.glandroid.xmlparser;

/**
 * @author Kevin
 * @time 2018-5-7
 * @description student类，对应待解析的xml文件的数据结构
 * @updateTime 2018-5-7
 */

public class Student {
    private String name;
    private String sex;
    private String nickName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", nickName='" + nickName + '\'' +
                '}';
    }
}
