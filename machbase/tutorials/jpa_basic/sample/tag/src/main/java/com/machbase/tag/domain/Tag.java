package com.machbase.tag.domain;

import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@ToString
@Entity
public class Tag {

    @EmbeddedId
    private TagPK pk;

    @Column
    private double value;

    public Tag() {}

    public Tag(TagPK pk, double value) {
        this.pk = pk;
        this.value = value;
    }

    public TagPK getPK() { return pk; }

    public double getValue() { return value; }
}
