package tech.mtright.telegramhabrbot.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import tech.mtright.telegramhabrbot.TelegramHabrBot;
import tech.mtright.telegramhabrbot.bot.subscribe_menu.SubscribeMenuPage;
import tech.mtright.telegramhabrbot.cache.DataCache;
import tech.mtright.telegramhabrbot.cache.UserData;

@Component
public abstract class AbstractBackOffHandler implements CallbackHandler {
    @UserData
    private DataCache dataCache;
    @Autowired
    private SubscribeMenuPage subscribeMenuPage;
    @Autowired
    private TelegramHabrBot bot;

    @Override
    public BotApiMethod<?> getAnswer(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        AbstractPage parent = subscribeMenuPage.getParent();
        dataCache.setUsersCurrentBotState(chatId, parent.getState());
        bot.deleteMessage(chatId, messageId);
        return parent.getPage(chatId);
    }
}
