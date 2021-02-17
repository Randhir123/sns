package com.randhirks.sns.service;

import com.randhirks.sns.model.ChangeNotification;
import com.randhirks.sns.model.NotificationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;

@Service
public class NotificationService {

    @Value("${sns.topic.arn}")
    private String topicArn;

    @Value("${aws.region}")
    private String awsRegion;

    private SnsClient snsClient;

    private void init() {
        snsClient = SnsClient.builder()
                .region(Region.of(awsRegion))
                .build();

    }

    public ChangeNotification sendNotification(NotificationRequest notificationRequest) {

        ChangeNotification changeNotification = ChangeNotification.builder()
                .sentMessage(notificationRequest.getMessage()).build();

        try {
            init();

            PublishRequest request = PublishRequest.builder()
                    .message(notificationRequest.getMessage())
                    .topicArn(topicArn)
                    .build();

            PublishResponse result = snsClient.publish(request);
            System.out.println(result.messageId() + " Message sent. Status was " + result.sdkHttpResponse().statusCode());
            changeNotification.setId(result.messageId());
        } catch (SnsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
        }
        return changeNotification;
    }
}
