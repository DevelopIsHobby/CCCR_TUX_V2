package com.machbase.lookup.domain;

import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@ToString
@Entity
// If you use volatile table, Please replace below line's table name.
@Table(name = "lookup")
public class Lookup {

    @Id
    private Long id;
    private String name;
    private Timestamp ts;

    public Lookup() {}

    public Lookup(Long id, String name, Timestamp ts) {
        this.id = id;
        this.name = name;
        this.ts = ts;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setTs(Timestamp ts) {
        this.ts = ts;
    }

    public static Timestamp get_arrival_time() throws InterruptedException {
        Thread.sleep(0, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Calendar cal = Calendar.getInstance();
        String today = null;
        today = sdf.format(cal.getTime());
        Timestamp ts = Timestamp.valueOf(today);

        return ts;
    }
}
