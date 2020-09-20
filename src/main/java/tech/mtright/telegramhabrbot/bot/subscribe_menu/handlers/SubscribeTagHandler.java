package tech.mtright.telegramhabrbot.bot.subscribe_menu.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import tech.mtright.telegramhabrbot.TelegramHabrBot;
import tech.mtright.telegramhabrbot.bot.BotState;
import tech.mtright.telegramhabrbot.bot.InputMessageHandler;
import tech.mtright.telegramhabrbot.bot.subscribe_menu.SubscribeMenuPage;
import tech.mtright.telegramhabrbot.cache.DataCache;
import tech.mtright.telegramhabrbot.cache.UserData;
import tech.mtright.telegramhabrbot.models.Tag;
import tech.mtright.telegramhabrbot.models.UserProfileData;
import tech.mtright.telegramhabrbot.services.SearchService;
import tech.mtright.telegramhabrbot.services.UsersProfileDataService;

import java.util.Optional;

import static tech.mtright.telegramhabrbot.bot.BotState.SUBSCRIBE;
import static tech.mtright.telegramhabrbot.bot.BotState.SUBSCRIBE_TAG;

@Component
public class SubscribeTagHandler implements InputMessageHandler {
    @Autowired
    private SubscribeMenuPage subscribeMenuPage;
    @Autowired
    private SearchService searchService;
    @Autowired
    private UsersProfileDataService dataService;
    @UserData
    private DataCache dataCache;

    @Override
    public SendMessage handle(Message message) {
        String text = message.getText();
        Long chatId = message.getChatId();
        TelegramHabrBot bot = subscribeMenuPage.getBot();
        dataCache.setUsersCurrentBotState(chatId, SUBSCRIBE);
        if (text.equalsIgnoreCase("назад")) {
            return subscribeMenuPage.getPage(chatId);
        }
        Optional<Tag> relevantTag = searchService.getIfRelevant(text);
        if (relevantTag.isEmpty()) {
            bot.sendMessage(chatId, "Не могу найти этот тег");
        } else {
            UserProfileData profileData = dataService.getUserProfileData(chatId);
            profileData.getSubscribedTags().add(relevantTag.get());
            dataService.saveUserProfileData(profileData);
            dataCache.saveUserProfileData(chatId, profileData);
            bot.sendMessage(chatId, "Теперь вы подписаны на " + relevantTag.get().getName());
        }
        return subscribeMenuPage.getPage(chatId);
    }

    @Override
    public BotState getHandledState() {
        return SUBSCRIBE_TAG;
    }
}
