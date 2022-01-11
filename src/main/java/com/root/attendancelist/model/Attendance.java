package com.root.attendancelist.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;
    private Long StudentID;

    @NotNull
    private String attendanceFrom;

    @NotNull
    private String attendanceTo;
    @Pattern(regexp = "(absent|late|present)")
    private String attendanceStatus;
    @NotBlank
    private String nameOfSubject;
    public Attendance() {

    }

    public Attendance(Long ID, Long groupID, String attendanceFrom, String attendanceTo, String attendanceStatus, String nameOfSubject) {
        this.ID = ID;
        this.StudentID = groupID;
        this.attendanceFrom = attendanceFrom;
        this.attendanceTo = attendanceTo;
        this.attendanceStatus = attendanceStatus;
        this.nameOfSubject = nameOfSubject;
    }


    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getStudentID() {
        return StudentID;
    }

    public void setStudentID(Long studentID) {
        StudentID = studentID;
    }


    public String getAttendanceFrom() {
        return attendanceFrom;
    }

    public void setAttendanceFrom(String attendanceFrom) {
        this.attendanceFrom = attendanceFrom;
    }

    public String getAttendanceTo() {
        return attendanceTo;
    }

    public void setAttendanceTo(String attendanceTo) {
        this.attendanceTo = attendanceTo;
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "ID=" + ID +
                ", StudentID=" + StudentID +
                ", attendanceFrom='" + attendanceFrom + '\'' +
                ", attendanceTo='" + attendanceTo + '\'' +
                ", attendanceStatus='" + attendanceStatus + '\'' +
                '}';
    }

    public String getNameOfSubject() {
        return nameOfSubject;
    }

    public void setNameOfSubject(String nameOfSubject) {
        this.nameOfSubject = nameOfSubject;
    }
}
