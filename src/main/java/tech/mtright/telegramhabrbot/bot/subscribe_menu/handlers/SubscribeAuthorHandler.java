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
import tech.mtright.telegramhabrbot.models.Author;
import tech.mtright.telegramhabrbot.models.Company;
import tech.mtright.telegramhabrbot.models.UserProfileData;
import tech.mtright.telegramhabrbot.services.SearchService;
import tech.mtright.telegramhabrbot.services.UsersProfileDataService;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static tech.mtright.telegramhabrbot.bot.BotState.*;

@Component
public class SubscribeAuthorHandler implements InputMessageHandler {
    @Autowired
    private SubscribeMenuPage subscribeMenuPage;
    @Autowired
    private SearchService searchService;
    @Autowired
    private UsersProfileDataService dataService;
    @Autowired
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
        List<Author> authors = searchService.searchAuthorsByName(text);
        if (authors == null) {
            bot.sendMessage(chatId, "Внутренняя ошибка сервера! Напишите об этом @MTRight");
            return subscribeMenuPage.getPage(chatId);
        } else {
            if (authors.size() == 0) {
                bot.sendMessage(chatId, "Не могу найти автора по имени " + text);
                return subscribeMenuPage.getPage(chatId);
            } else if (authors.size() == 1) {
                UserProfileData profileData = dataService.getUserProfileData(chatId);
                profileData.getSubscribedAuthors().add(authors.get(0));
                dataService.saveUserProfileData(profileData);
                dataCache.saveUserProfileData(chatId, profileData);
                bot.sendMessage(chatId, format("Теперь вы подписаны на %s(%s)", authors.get(0).getFullName(), authors.get(0).getName()));
                return subscribeMenuPage.getPage(chatId);
            } else {
                dataCache.setUsersCurrentBotState(chatId, getHandledState());
                String hubsName = authors.stream().map(author -> author.getFullName() + "(" + author.getName() + ")")
                        .collect(Collectors.joining(", "));
                return new SendMessage(chatId, "Уточните, на кого вы хотели подписаться: " + hubsName);
            }
        }
    }

    @Override
    public BotState getHandledState() {
        return SUBSCRIBE_AUTHOR;
    }
}
