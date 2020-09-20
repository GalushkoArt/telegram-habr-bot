package tech.mtright.telegramhabrbot.bot;

import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import tech.mtright.telegramhabrbot.bot.main_menu.MainMenuPage;
import tech.mtright.telegramhabrbot.cache.DataCache;
import tech.mtright.telegramhabrbot.cache.UserData;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static tech.mtright.telegramhabrbot.bot.BotState.*;

@Component
@Log4j2
@Setter
public class TelegramFacade {
    @UserData
    private DataCache userDataCache;
    @Autowired
    private BotStateContext botStateContext;
    @Autowired
    private MainMenuPage mainMenuPage;
    private Map<BotState, AbstractPage> menus;

    public TelegramFacade(@Lazy @MenuPage List<AbstractPage> menuPageList) {
        menus = menuPageList.stream().collect(Collectors.toMap(AbstractPage::getState, Function.identity()));
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        SendMessage replyMessage = null;
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            long chatId = callbackQuery.getMessage().getChatId();
            if (!userDataCache.hasUser(chatId)) {
                userDataCache.loadOrCreateUser(chatId);
            }
            log.info("New callbackQuery from User: {}, userId: {}, chatId: {}, with data: {}, on state: {}",
                    update.getCallbackQuery().getFrom().getUserName(), callbackQuery.getFrom().getId(), chatId,
                    update.getCallbackQuery().getData(), userDataCache.getUsersCurrentBotState(chatId));
            return processCallbackQuery(callbackQuery);
        }
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            Long chatId = message.getChatId();
            Integer userId = message.getFrom().getId();
            if (!userDataCache.hasUser(chatId)) {
                userDataCache.loadOrCreateUser(message.getChatId());
            }
            log.info("New message from User: {}, userId: {}, chatId: {},  with text: {}, on state: {}",
                    message.getFrom().getUserName(), userId, message.getChatId(), message.getText(),
                    userDataCache.getUsersCurrentBotState(chatId));
            replyMessage = handleInputMessage(message);
        }
        return replyMessage;
    }

    private SendMessage handleInputMessage(Message message) {
        String inputMsg = message.getText();
        Long chatId = message.getChatId();
        BotState botState;
        SendMessage replyMessage;

        switch (inputMsg) {
            case "/start":
                userDataCache.setUsersCurrentBotState(chatId, WELCOME);
                return mainMenuPage.getPage(chatId);
            case "Подписаться":
                userDataCache.setUsersCurrentBotState(chatId, SUBSCRIBE);
                return menus.get(SUBSCRIBE).getPage(chatId);
            case "Мои подписки":
                userDataCache.setUsersCurrentBotState(chatId, MY_SUBSCRIPTIONS);
                return menus.get(MY_SUBSCRIPTIONS).getPage(chatId);
            case "Сохраненные статьи":
                userDataCache.setUsersCurrentBotState(chatId, MY_SAVED_POSTS);
                return menus.get(MY_SAVED_POSTS).getPage(chatId);
            default:
                botState = userDataCache.getUsersCurrentBotState(chatId);
                break;
        }

        userDataCache.setUsersCurrentBotState(chatId, botState);
        replyMessage = botStateContext.processInputMessage(botState, message);
        return replyMessage;
    }


    private BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {
        String data = buttonQuery.getData().split(":")[0];
        return botStateContext.processInputCallback(data, buttonQuery);
    }
}
