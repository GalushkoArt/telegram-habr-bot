package tech.mtright.telegramhabrbot.services;

import tech.mtright.telegramhabrbot.models.Post;

import java.util.List;

public interface NotificationSenderService {
    void sendNotifications(List<Long> usersChatId, Post post);
}
