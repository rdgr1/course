package com.ead.course.dtos;

import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.SQLOrder;

import java.util.Set;
import java.util.UUID;

public record CourseRecordDto(
        @NotBlank(message = "Name is mandatory!")
        @Size(min = 3, max = 150, message = "The size must be 3 and 150")
        String name,
        @NotBlank(message = "Description is mandatory!")
        @Size(min = 10, max = 255, message = "The size must be 10 and 255")
        String description,
        @NotNull(message = "Course Status is mandatory!")
        CourseStatus courseStatus,
        @NotNull(message = "Course Level is mandatory!")
        CourseLevel courseLevel,
        @NotNull(message = "User Instructor is mandatory!")
        UUID userInstructor,
        String imageUrl
){}
