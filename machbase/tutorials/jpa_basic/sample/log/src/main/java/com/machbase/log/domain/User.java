package com.machbase.log.domain;

import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@ToString
@Entity
@Table(name = "t1")
public class User {
    @Id
    private Timestamp _arrival_time;
    private String name;
    private int age;

    public User() {
    }
    public User(Timestamp dt, String name, int age) {
        this._arrival_time = dt;
        this.name = name;
        this.age = age;
    }

    public static Timestamp get_arrival_time() throws ParseException, InterruptedException {
        Thread.sleep(0, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Calendar cal = Calendar.getInstance();
        String today = null;
        today = sdf.format(cal.getTime());
        Timestamp ts = Timestamp.valueOf(today);

        return ts;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return this.age;
    }
}
