package com.math.yang.mathyang.model;

import java.io.Serializable;

/**
 * Created by zhangtx on 2018/1/2.
 */

public class BookTerm implements Serializable {
    private String id;
    private String name;
    private int grade;
    private int classhour;
    private String term;
    private float money;
    private int videocount;
    private String coverurl;
    private String describe;
    private int boughtcount;
    private int commentcount;
    private String subjectid;
    private int showtype;
    private String datetime;
    private String bannercoverurl;
    public static final int TYPE_BANNER = 1;
    public static final int TYPE_BOOK = 0;
    private String indexUrl;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getBannercoverurl() {
        return bannercoverurl;
    }

    public void setBannercoverurl(String bannercoverurl) {
        this.bannercoverurl = bannercoverurl;
    }

    public int getClasshour() {
        return classhour;
    }

    public void setClasshour(int classhour) {
        this.classhour = classhour;
    }

    public BookTerm() {
    }

    public BookTerm(String id, String name, int grade, String term, float money, int videocount, String coverurl, String describe, int boughtcount, int commentcount, String subjectid) {
        this.id = id;
        this.name = name;
        this.grade = grade;
        this.term = term;
        this.money = money;
        this.videocount = videocount;
        this.coverurl = coverurl;
        this.describe = describe;
        this.boughtcount = boughtcount;
        this.commentcount = commentcount;
        this.subjectid = subjectid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public int getVideocount() {
        return videocount;
    }

    public void setVideocount(int videocount) {
        this.videocount = videocount;
    }

    public int getShowtype() {
        return showtype;
    }

    public void setShowtype(int showtype) {
        this.showtype = showtype;
    }

    public String getCoverurl() {
        return coverurl;
    }

    public void setCoverurl(String coverurl) {
        this.coverurl = coverurl;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getBoughtcount() {
        return boughtcount;
    }

    public void setBoughtcount(int boughtcount) {
        this.boughtcount = boughtcount;
    }

    public int getCommentcount() {
        return commentcount;
    }

    public void setCommentcount(int commentcount) {
        this.commentcount = commentcount;
    }

    public String getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(String subjectid) {
        this.subjectid = subjectid;
    }

    public String getIndexUrl() {
        return indexUrl;
    }
}
