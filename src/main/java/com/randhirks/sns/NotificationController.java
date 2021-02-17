package com.randhirks.sns;

import com.randhirks.sns.model.ChangeNotification;
import com.randhirks.sns.model.NotificationRequest;
import com.randhirks.sns.service.NotificationListener;
import com.randhirks.sns.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @Autowired
    NotificationListener notificationListener;

    @PostMapping(path = "/send", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ChangeNotification> sendNotification(@RequestBody NotificationRequest notificationRequest) {
        ChangeNotification sent = notificationService.sendNotification(notificationRequest);
        ChangeNotification received = notificationListener.receiveMessage(sent);
        return ResponseEntity.ok(received);
    }
}
