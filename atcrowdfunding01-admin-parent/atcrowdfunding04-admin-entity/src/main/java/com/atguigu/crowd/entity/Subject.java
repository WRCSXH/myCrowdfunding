package com.atguigu.crowd.entity;

public class Subject {
    private String subjectName;
    private String subjectGrade;

    public Subject() {
    }

    public Subject(String subjectName, String subjectGrade) {
        this.subjectName = subjectName;
        this.subjectGrade = subjectGrade;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectGrade() {
        return subjectGrade;
    }

    public void setSubjectGrade(String subjectGrade) {
        this.subjectGrade = subjectGrade;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "subjectName='" + subjectName + '\'' +
                ", subjectGrade='" + subjectGrade + '\'' +
                '}';
    }
}
