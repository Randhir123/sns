package com.randhirks.sns.model;

import lombok.Data;

@Data
public class SNSMessage {
    private String Type;
    private String MessageId;
    private String TopicArn;
    private String Message;
}