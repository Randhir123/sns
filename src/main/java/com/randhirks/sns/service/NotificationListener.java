package com.randhirks.sns.service;

import com.google.gson.Gson;
import com.randhirks.sns.model.ChangeNotification;
import com.randhirks.sns.model.SNSMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;

@Service
public class NotificationListener {

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${sqs.id}")
    private String queueUrl;

    private SqsClient sqsClient;

    private void init() {
        sqsClient = SqsClient.builder()
                .region(Region.of(awsRegion))
                .build();
    }

    public ChangeNotification receiveMessage(ChangeNotification changeNotification) {
        try {
            init();
            ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .maxNumberOfMessages(1)
                    .build();

            Message message = sqsClient.receiveMessage(receiveMessageRequest).messages().get(0);
            Gson gson = new Gson();
            SNSMessage snsMessage = gson.fromJson(message.body(), SNSMessage.class);
            changeNotification.setReceivedMessage(snsMessage.getMessage());
        } catch (SqsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
        }
        return changeNotification;
    }
}
