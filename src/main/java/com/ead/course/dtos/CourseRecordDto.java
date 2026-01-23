package com.ead.course.dtos;

import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;
import java.util.UUID;

public record CourseRecordDto(

        @NotBlank(message = "Name is mandatory!") @Size(min = 3, max = 150, message = "The size must be 3 and 150") String name,
        String description,
        @NotNull
        CourseStatus courseStatus, @NotNull CourseLevel courseLevel, @NotNull UUID userInstructor, String imageUrl) {
}
