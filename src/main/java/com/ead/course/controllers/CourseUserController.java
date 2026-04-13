package com.ead.course.controllers;

import com.ead.course.client.AuthUserClient;
import com.ead.course.dtos.SubscriptionRecordDto;
import com.ead.course.dtos.UserRecordDto;
import com.ead.course.enums.UserStatus;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.services.impl.CourseServiceImpl;
import com.ead.course.services.impl.CourseUserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RequestMapping
@RestController
public class CourseUserController {
    final AuthUserClient client;
    final CourseServiceImpl courseService;
    final CourseUserServiceImpl courseUserService;

    public CourseUserController(AuthUserClient client, CourseServiceImpl courseService, CourseUserServiceImpl courseUserService) {
        this.client = client;
        this.courseService = courseService;
        this.courseUserService = courseUserService;
    }

    @GetMapping("/courses/{courseId}/users")
    public ResponseEntity<Page<UserRecordDto>> getAllUsersByCourse(@PageableDefault(sort = "userId", direction = Sort.Direction.ASC) Pageable pageable,
                                                                              @PathVariable UUID courseId){
        return ResponseEntity.status(HttpStatus.OK).body(client.getAllUsersByCourse(courseId, pageable));
    }

    @GetMapping("/courses/{courseId}/users/{userId}")
    public ResponseEntity<Object> existsByCourseIdAndUser(@PathVariable UUID courseId, @PathVariable UUID userId){
        var course = courseService.findById(courseId);
        return ResponseEntity.status(HttpStatus.OK).body(courseUserService.existByCourseAndUserId(course.get(),userId));
    }

    @PostMapping("/courses/{courseId}/users/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable UUID courseId,
                                                               @RequestBody @Valid SubscriptionRecordDto subscriptionRecordDto){
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        ResponseEntity<UserRecordDto> responseUser = client.getOneUserById(subscriptionRecordDto.userId());
        assert responseUser.getBody() != null;
        if(responseUser.getBody().userStatus().equals(UserStatus.BLOCKED)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: User is blocked.");
        }
        CourseUserModel courseUserModel =
                courseUserService.saveAndSendSubscriptionUserInCourse(courseModelOptional.get().convertToCourseUserModel(subscriptionRecordDto.userId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(courseUserModel);
    }

    @DeleteMapping("/courses/users/{userId}")
    public ResponseEntity<Object> deleteUserCourseByUser(@PathVariable UUID userId){
        if (!courseUserService.existsByUserId(userId)){
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("CourseUser not found.");
        }
        courseUserService.deleteAllByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body("CourseUser deleted successfully.");
    }
}
