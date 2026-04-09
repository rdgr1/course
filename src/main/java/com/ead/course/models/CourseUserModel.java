package com.ead.course.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TB_COURSES_USERS")
public class CourseUserModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;
    @Column(nullable = false)
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private CourseModel course;

    public CourseUserModel(UUID uuid, UUID userId, CourseModel course) {
        this.uuid = uuid;
        this.userId = userId;
        this.course = course;
    }

    public CourseUserModel() {
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public CourseModel getCourseModel() {
        return course;
    }

    public void setCourseModel(CourseModel courseModel) {
        this.course = courseModel;
    }
}
