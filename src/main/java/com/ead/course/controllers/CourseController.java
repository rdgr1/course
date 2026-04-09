/*
 * Copyright (c) 2026. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.ead.course.controllers;

import com.ead.course.dtos.CourseRecordDto;
import com.ead.course.models.CourseModel;
import com.ead.course.services.impl.CourseServiceImpl;
import com.ead.course.specifications.SpecificationTemplate;
import com.ead.course.validations.CourseValidator;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin("*")
@RequestMapping("/courses")
public class CourseController {
    final CourseServiceImpl service;
    final CourseValidator courseValidator;

    Logger logger = LogManager.getLogger(CourseController.class);
    public CourseController(CourseServiceImpl service, CourseValidator courseValidator) {
        this.service = service;
        this.courseValidator = courseValidator;
    }


    @PostMapping
    public ResponseEntity<?> saveCourse(@RequestBody CourseRecordDto courseRecordDto, Errors errors) {
        logger.debug("POST saveCourse received {}", courseRecordDto);
        courseValidator.validate(courseRecordDto, errors);
        if (errors.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.getAllErrors());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(courseRecordDto));
    }

    @GetMapping
    public ResponseEntity<Page<CourseModel>> getAllCourses(SpecificationTemplate.CourseSpec spec, Pageable pageable, @RequestParam(required = false) UUID userId) {
        Page<CourseModel> courseModelPage = (userId != null)
                ? service.findAll(SpecificationTemplate.courseUserId(userId).and(spec), pageable)
                : service.findAll(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(courseModelPage);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Optional<CourseModel>> getOneCourse(@PathVariable UUID courseId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findById(courseId));
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<?> deleteCourse(@PathVariable UUID courseId) {
        logger.debug("DELETE deleteCourse courseId: {}", courseId);
        service.delete(service.findById(courseId).get());
        return ResponseEntity.status(HttpStatus.OK).body("Course Deleted Successfully!");
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<CourseModel> updateCourse(@PathVariable UUID courseId, @RequestBody @Valid CourseRecordDto dto) {
        logger.debug("PUT updateCourse courseId: {}, received: {}", courseId, dto);
        return ResponseEntity.status(HttpStatus.OK).body(service.update(dto, service.findById(courseId).get()));
    }
}
