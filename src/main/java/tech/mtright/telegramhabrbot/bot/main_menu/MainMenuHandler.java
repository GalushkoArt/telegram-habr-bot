package tech.mtright.telegramhabrbot.bot.main_menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import tech.mtright.telegramhabrbot.bot.AbstractPage;
import tech.mtright.telegramhabrbot.bot.BotState;
import tech.mtright.telegramhabrbot.bot.InputMessageHandler;
import tech.mtright.telegramhabrbot.bot.MenuPage;
import tech.mtright.telegramhabrbot.cache.DataCache;
import tech.mtright.telegramhabrbot.cache.UserData;
import tech.mtright.telegramhabrbot.services.ReplyMessagesService;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static tech.mtright.telegramhabrbot.bot.BotState.*;

@Component
public class MainMenuHandler implements InputMessageHandler {
    private final Map<BotState, AbstractPage> menus;
    @UserData
    private DataCache userDataCache;
    @Autowired
    private ReplyMessagesService messagesService;

    @Autowired
    public MainMenuHandler(@MenuPage List<AbstractPage> menuPageList) {
        menus = menuPageList.stream().collect(Collectors.toMap(AbstractPage::getState, Function.identity()));
    }

    @Override
    public SendMessage handle(Message message) {
        Long chatId = message.getChatId();
        SendMessage answer;
        switch (message.getText()) {
            case "Подписаться":
                userDataCache.setUsersCurrentBotState(chatId, SUBSCRIBE);
                answer = menus.get(SUBSCRIBE).getPage(chatId);
                break;
            case "Мои подписки":
                userDataCache.setUsersCurrentBotState(chatId, MY_SUBSCRIPTIONS);
                answer = menus.get(MY_SUBSCRIPTIONS).getPage(chatId);
                break;
            case "Сохраненные статьи":
                userDataCache.setUsersCurrentBotState(chatId, MY_SAVED_POSTS);
                answer = menus.get(MY_SAVED_POSTS).getPage(chatId);
                break;
            default:
                answer = new SendMessage(chatId, messagesService.getReplyText("reply.dontUnderstand"));
        }
        return answer;
    }

    @Override
    public BotState getHandledState() {
        return WELCOME;
    }
}
