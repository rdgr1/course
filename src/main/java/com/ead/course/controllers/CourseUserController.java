package com.ead.course.controllers;

import com.ead.course.services.impl.CourseServiceImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping
@RestController
public class CourseUserController {
    final CourseServiceImpl courseService;

    public CourseUserController(CourseServiceImpl courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/courses/{courseId}/users")
    public ResponseEntity<Object> getAllUsersByCourse(@PageableDefault(sort = "userId", direction = Sort.Direction.ASC) Pageable pageable, @PathVariable UUID courseId) {
        return ResponseEntity.status(HttpStatus.OK).body("");
    }


    @PostMapping("/courses/{courseId}/users/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable UUID courseId){
        var courseModelOpt = courseService.findById(courseId);
        // Verifications with state transfer TODO
        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }


}
