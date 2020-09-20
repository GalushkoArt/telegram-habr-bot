package tech.mtright.telegramhabrbot.services;

import tech.mtright.telegramhabrbot.models.Post;

public interface PostNotificationService {
    boolean notifyUsers(Post post);
}
