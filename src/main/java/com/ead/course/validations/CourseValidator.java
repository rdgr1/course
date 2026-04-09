
package com.ead.course.validations;

import com.ead.course.client.AuthUserClient;
import com.ead.course.dtos.CourseRecordDto;
import com.ead.course.enums.UserType;
import com.ead.course.services.impl.CourseServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.UUID;

@Component
public class CourseValidator implements Validator {

    Logger logger = LogManager.getLogger(CourseValidator.class);

    private final Validator validator;
    private final CourseServiceImpl courseService;
    private final AuthUserClient authUserClient;

    public CourseValidator(@Qualifier("defaultValidator") Validator validator, CourseServiceImpl courseService, AuthUserClient authUserClient) {
        this.validator = validator;
        this.courseService = courseService;
        this.authUserClient = authUserClient;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object o, Errors errors) {
        CourseRecordDto courseRecordDto = (CourseRecordDto) o;
        validator.validate(courseRecordDto,errors);
        if (!errors.hasErrors()){
            validateCourseName(courseRecordDto, errors);
            validateUserInstructor(courseRecordDto.userInstructor(), errors);
        }
    }

    private void validateCourseName(CourseRecordDto courseRecordDto, Errors errors){
        if (courseService.existsByName(courseRecordDto.name())){
            errors.rejectValue("name", "courseNameConflict", "Course Name is Already Taken.");
            logger.error("Error Validation courseName: {}", courseRecordDto.name());
        }
    }

    private void validateUserInstructor(UUID userInstructor, Errors errors){
        var responseUserInstructor = authUserClient.getOneUserById(userInstructor).getBody();
        assert responseUserInstructor != null;
        if ( responseUserInstructor.userType().equals(UserType.STUDENT) ||
             responseUserInstructor.userType().equals(UserType.USER) ){
            errors.rejectValue("userInstructor", "UserInstructorError", "User must be INSTRUCTOR or ADMIN");
            logger.error("Error Validation userInstructor: {}", userInstructor);
        }
    }
}
