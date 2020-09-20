package tech.mtright.telegramhabrbot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import tech.mtright.telegramhabrbot.TelegramHabrBot;
import tech.mtright.telegramhabrbot.bot.saved_posts_menu.SavedPostsPage;
import tech.mtright.telegramhabrbot.models.Post;
import tech.mtright.telegramhabrbot.models.UserProfileData;

import java.util.List;

@Service
public class PostNotificationSenderService implements NotificationSenderService {
    @Autowired
    private TelegramHabrBot bot;
    @Autowired
    private SavedPostsPage postsPage;

    @Override
    public void sendNotifications(List<Long> usersChatId, Post post) {
        for (Long chatId : usersChatId) {
            bot.sendMessage(postsPage.getMessageForPost(chatId, post, "Новый пост"));
        }
    }
}
