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
import tech.mtright.telegramhabrbot.models.Company;
import tech.mtright.telegramhabrbot.models.UserProfileData;
import tech.mtright.telegramhabrbot.services.SearchService;
import tech.mtright.telegramhabrbot.services.UsersProfileDataService;

import java.util.List;
import java.util.stream.Collectors;

import static tech.mtright.telegramhabrbot.bot.BotState.SUBSCRIBE;
import static tech.mtright.telegramhabrbot.bot.BotState.SUBSCRIBE_COMPANY;

@Component
public class SubscribeCompanyHandler implements InputMessageHandler {
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
        List<Company> companies = searchService.searchCompaniesByName(text);
        if (companies == null) {
            bot.sendMessage(chatId, "Внутренняя ошибка сервера! Напишите об этом @MTRight");
            return subscribeMenuPage.getPage(chatId);
        } else {
            if (companies.size() == 0) {
                bot.sendMessage(chatId, "Не могу найти компанию с названием " + text);
                return subscribeMenuPage.getPage(chatId);
            } else if (companies.size() == 1) {
                UserProfileData profileData = dataService.getUserProfileData(chatId);
                profileData.getSubscribedCompanies().add(companies.get(0));
                dataService.saveUserProfileData(profileData);
                dataCache.saveUserProfileData(chatId, profileData);
                bot.sendMessage(chatId, "Теперь вы подписаны на " + companies.get(0).getName());
                return subscribeMenuPage.getPage(chatId);
            } else {
                dataCache.setUsersCurrentBotState(chatId, getHandledState());
                String companiesName = companies.stream().map(Company::getName).collect(Collectors.joining(", "));
                return new SendMessage(chatId, "Уточните, на какую компанию вы хотели подписаться: " + companiesName);
            }
        }
    }

    @Override
    public BotState getHandledState() {
        return SUBSCRIBE_COMPANY;
    }
}
