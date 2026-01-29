package com.ead.course.dtos;

import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;
public record ModuleRecordDto(
        @NotBlank(message = "Name is mandatory")
        String name,
        @NotBlank(message = "Description is mandatory")
        String description
) {
}
