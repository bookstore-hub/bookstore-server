package com.rdv.server.notification.service;


import com.rdv.server.notification.to.Note;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;


@Service
public class FirebaseMessagingService {

    protected static final Log LOGGER = LogFactory.getLog(FirebaseMessagingService.class);
    private static final String NOTIFICATION = "notification";
    private static final String NOTIFICATION_WITH_DATA = "notification with data";
    private static final String BACKGROUND_NOTIFICATION = "background notification";
    private static final String CHAT_MESSAGE_NOTIFICATION = "chat message notification";


    private final FirebaseMessaging firebaseMessaging;


    public FirebaseMessagingService(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }


    public String sendNotification(Note note, String token) throws ExecutionException, InterruptedException {

        Notification notification = Notification
                .builder()
                .setTitle(note.getSubject())
                .setBody(note.getContent())
                .build();

        Message message = Message
                .builder()
                .setApnsConfig(getApnsConfig())
                .setAndroidConfig(getAndroidConfig())
                .setToken(token)
                //.setTopic()
                .setNotification(notification)
                .build();

        return sendNotificationToUser(note, message, NOTIFICATION);
    }

    public String sendNotificationWithData(Note note, String token) throws ExecutionException, InterruptedException {

        Notification notification = Notification
                .builder()
                .setTitle(note.getSubject())
                .setBody(note.getContent())
                .build();

        Message message = Message
                .builder()
                .setApnsConfig(getApnsConfig())
                .setAndroidConfig(getAndroidConfig())
                .setToken(token)
                .setNotification(notification)
                .putAllData(note.getData())
                .build();

        return sendNotificationToUser(note, message, NOTIFICATION_WITH_DATA);
    }

    public String sendNotificationWithOnlyData(Note note, String token) throws ExecutionException, InterruptedException {

        Message message = Message
                .builder()
                .setApnsConfig(getApnsConfigForOnlyData())
                .setAndroidConfig(getAndroidConfigForOnlyData())
                .setToken(token)
                .putAllData(note.getData())
                .build();

        return sendNotificationToUser(note, message, BACKGROUND_NOTIFICATION);
    }

    public String sendChatMessageNotification(Note note, Long assistanceId, String token) throws ExecutionException, InterruptedException {

        Message message = Message
                .builder()
                .setApnsConfig(getApnsConfigForChatMessages(note, assistanceId))
                .setAndroidConfig(getAndroidConfigForChatMessages(note))
                .setToken(token)
                .putAllData(note.getData())
                .build();

        return sendNotificationToUser(note, message, CHAT_MESSAGE_NOTIFICATION);
    }

    private String sendNotificationToUser(Note note, Message message, String notificationType) throws InterruptedException, ExecutionException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String notificationAsJson = null;
        try {
            notificationAsJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(message);
            LOGGER.info("Sending " + notificationType + " having title " + note.getSubject() + " and content " + note.getContent());
            firebaseMessaging.sendAsync(message).get();
        } catch (JsonProcessingException e) {
            LOGGER.info("JSON exception for " + notificationType + " having title: " + note.getSubject() + " and content " + note.getContent());
        }
        return notificationAsJson;
    }

    private AndroidConfig getAndroidConfig() {
        return AndroidConfig.builder()
                .setTtl(Duration.ofMinutes(60).toMillis())
                .setPriority(AndroidConfig.Priority.HIGH)
                .setNotification(AndroidNotification.builder().setSound("default").build())
                .build();
    }

    private AndroidConfig getAndroidConfigForOnlyData() {
        return AndroidConfig.builder()
                .setTtl(Duration.ofMinutes(60).toMillis())
                .setPriority(AndroidConfig.Priority.HIGH)
                .build();
    }

    private AndroidConfig getAndroidConfigForChatMessages(Note note) {
        Map<String, String> messageData = new HashMap<>();
        messageData.put("title", note.getSubject());
        messageData.put("body", note.getContent());

        return AndroidConfig.builder()
                .setTtl(Duration.ofMinutes(60).toMillis())
                .setPriority(AndroidConfig.Priority.HIGH)
                .putAllData(messageData)
                .build();
    }

    private ApnsConfig getApnsConfig() {
        Map<String, String> headers = new HashMap<>();
        headers.put("apns-push-type", "alert");
        headers.put("apns-priority", String.valueOf(5));
        return ApnsConfig.builder().setAps(Aps.builder().setSound("default").setContentAvailable(true).build()).putAllHeaders(headers).build();
    }

    private ApnsConfig getApnsConfigForOnlyData() {
        Map<String, String> headers = new HashMap<>();
        headers.put("apns-push-type", "background");
        headers.put("apns-priority", String.valueOf(5));
        return ApnsConfig.builder().setAps(Aps.builder().setContentAvailable(true).build()).putAllHeaders(headers).build();
    }

    private ApnsConfig getApnsConfigForChatMessages(Note note, Long assistanceId) {
        Map<String, String> headers = new HashMap<>();
        headers.put("apns-push-type", "alert");
        headers.put("apns-priority", String.valueOf(5));
        return ApnsConfig.builder().setAps(Aps.builder().setSound("default").setAlert(ApsAlert.builder().setTitle(note.getSubject()).setBody(note.getContent()).build())
                .setThreadId(String.valueOf(assistanceId)).setContentAvailable(true).build()).putAllHeaders(headers).build();
    }

}