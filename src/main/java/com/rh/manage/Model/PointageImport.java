package com.rh.manage.Model;

public class PointageImport {
    private String id;
    private String name;
    private String dept;
    private String attendance_time;
    private String attendance_type;
    private String machine_name;

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
    public String getDept() {
        return dept;
    }
    public void setDept(String dept) {
        this.dept = dept;
    }
    public String getAttendance_time() {
        return attendance_time;
    }
    public void setAttendance_time(String attendance_time) {
        this.attendance_time = attendance_time;
    }
    public String getAttendance_type() {
        return attendance_type;
    }
    public void setAttendance_type(String attendance_type) {
        this.attendance_type = attendance_type;
    }
    public String getMachine_name() {
        return machine_name;
    }
    public void setMachine_name(String machine_name) {
        this.machine_name = machine_name;
    }
}