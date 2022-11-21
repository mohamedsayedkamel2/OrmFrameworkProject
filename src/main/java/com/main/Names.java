package com.main;

import framework.model.annotations.Field;
import framework.model.annotations.PrimaryKey;
import framework.model.annotations.Table;
import framework.model.annotations.Transient;

@Table(name = "mohamedsayedkamelali")
public class Names {

    @PrimaryKey
    int id;
    @Field(name = "mohamed")
    public String name;

    @Field(name = "mohamed")
    @Transient
    public int age;

    @Field
    public float salary;

    public Names(String s) {}

}
