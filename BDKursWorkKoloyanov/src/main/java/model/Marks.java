package model;

import java.sql.Date;

public class Marks {
    private Integer id;
    private String student;
    private String subject;
    private String teacher;
    private Integer value;

    public Marks(Integer id, String student, String subject, String teacher, Integer value) {
        this.id = id;
        this.student = student;
        this.subject = subject;
        this.teacher = teacher;
        this.value = value;
    }

    public Marks(String student, String subject, String teacher, Integer value) {
        this.student = student;
        this.subject = subject;
        this.teacher = teacher;
        this.value = value;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
