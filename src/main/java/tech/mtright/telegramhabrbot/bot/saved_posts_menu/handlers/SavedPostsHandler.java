package tech.mtright.telegramhabrbot.bot.saved_posts_menu.handlers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import tech.mtright.telegramhabrbot.bot.BotState;
import tech.mtright.telegramhabrbot.bot.InputMessageHandler;
import tech.mtright.telegramhabrbot.bot.main_menu.MainMenuPage;
import tech.mtright.telegramhabrbot.bot.saved_posts_menu.SavedPostsPage;
import tech.mtright.telegramhabrbot.cache.DataCache;
import tech.mtright.telegramhabrbot.cache.UserData;
import tech.mtright.telegramhabrbot.models.UserProfileData;
import tech.mtright.telegramhabrbot.services.ReplyMessagesService;

import static tech.mtright.telegramhabrbot.bot.BotState.MY_SAVED_POSTS;
import static tech.mtright.telegramhabrbot.bot.BotState.WELCOME;

@Component
@Log4j2
public class SavedPostsHandler implements InputMessageHandler {
    @Autowired
    private SavedPostsPage savedPostsPage;
    @Autowired
    private MainMenuPage mainMenuPage;
    @UserData
    private DataCache dataCache;

    @Override
    public SendMessage handle(Message message) {
        String text = message.getText();
        Long chatId = message.getChatId();
        dataCache.setUsersCurrentBotState(chatId, WELCOME);
        if (text.equals("Сохраненные статьи")) {
            return savedPostsPage.getPage(chatId);
        }
        log.error("Unexpected behavior on Saved Posts page when " + message.getText());
        savedPostsPage.getBot().sendMessage(chatId, "Неожиданное поведение! Пожалуйста напишите об этом @MTRight");
        return mainMenuPage.getPage(chatId);
    }

    @Override
    public BotState getHandledState() {
        return MY_SAVED_POSTS;
    }
}
