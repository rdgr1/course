package com.ead.course.client;

import com.ead.course.dtos.CourseUserRecordDto;
import com.ead.course.dtos.ResponsePageDto;
import com.ead.course.dtos.SubscriptionRecordDto;
import com.ead.course.dtos.UserRecordDto;
import com.ead.course.exceptions.NotFoundException;
import com.ead.course.models.CourseUserModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.UUID;

@Component
public class AuthUserClient {
    Logger logger = LogManager.getLogger(AuthUserClient.class);

    @Value("${ead.api.url.user}")
    String baseUrlAuthUser;

    final RestClient client;

    public AuthUserClient(RestClient.Builder client) {
        this.client = client.build();
    }

    public Page<UserRecordDto> getAllUsersByCourse(UUID courseId, Pageable pageable){
        String url = baseUrlAuthUser + "/users?courseId=" + courseId + "&page=" + pageable.getPageNumber() + "&size="
                + pageable.getPageSize() + "&sort=" + pageable.getSort().toString().replaceAll(":\\s*", ",");
        try {
            return client
                    .get()
                    .uri(url)
                    .retrieve()
                    .body(new ParameterizedTypeReference<ResponsePageDto<UserRecordDto>>() {
                    });
        } catch (RestClientException e){
            logger.error("Error Request RestClient with cause: {}", e);
            throw new RuntimeException("Error Request RestClient", e);
        }
    }

    public ResponseEntity<UserRecordDto> getOneUserById(UUID userId){
        String url = baseUrlAuthUser + "/users/" + userId;
        try {
            return client
                    .get()
                    .uri(url)
                    .retrieve()
                    .onStatus(
                            status -> status.value() == 404, (request, response) -> {
                                logger.error("Error: User not found: {}", userId);
                                throw new NotFoundException("Error: User not found.");
                            }
                    ).toEntity(UserRecordDto.class);
        } catch (RestClientException e) {
            logger.error("Error Request GET RestClient with cause: {}", e);
            throw new RuntimeException("Error Request GET RestClient", e);
        }
    }

    public void postSubscriptionInCourse(UUID courseId, UUID userId){
        String url = baseUrlAuthUser + "/users/" + userId + "/courses/subscription";
        try {
            var courseUserDto = new CourseUserRecordDto(userId,courseId);
                    client
                    .post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(courseUserDto)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException e){
            logger.error("Error Request POST RestClient with cause: {}", userId);
            throw new RuntimeException("Error Request POST RestClient", e);
        }
    }
}
