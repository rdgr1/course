package com.ead.course.services.impl;

import com.ead.course.client.AuthUserClient;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.services.CourseUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
public class CourseUserServiceImpl implements CourseUserService {
    final CourseUserRepository repo;
    final AuthUserClient authUserClient;

    public CourseUserServiceImpl(CourseUserRepository repo, AuthUserClient authUserClient) {
        this.repo = repo;
        this.authUserClient = authUserClient;
    }

    @Override
    public boolean existByCourseAndUserId(CourseModel courseUserModel, UUID userId) {
        return repo.existsByCourseAndUserId(courseUserModel,userId);
    }
    @Transactional
    @Override
    public CourseUserModel saveAndSendSubscriptionUserInCourse(CourseUserModel courseUserModel) {
        courseUserModel = repo.save(courseUserModel);
        // Send to AuthUser MS
        authUserClient.postSubscriptionInCourse(courseUserModel.getCourseModel().getCourseId(), courseUserModel.getUserId());
        return  courseUserModel;
    }
}
