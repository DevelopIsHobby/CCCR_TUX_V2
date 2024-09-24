package com.machbase.tag.domain;

import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@ToString
@Embeddable
public class TagPK implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "time", nullable = false)
    private Timestamp time;

    public TagPK() {}

    public TagPK(String name, Timestamp time) {
        super();
        this.name = name;
        this.time = time;
    }

    public String getName() { return this.name; }

    public Timestamp getTime() { return this.time; }

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
