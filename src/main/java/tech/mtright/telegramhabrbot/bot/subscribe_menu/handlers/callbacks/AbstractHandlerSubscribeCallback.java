package tech.mtright.telegramhabrbot.bot.subscribe_menu.handlers.callbacks;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import tech.mtright.telegramhabrbot.TelegramHabrBot;
import tech.mtright.telegramhabrbot.bot.BotState;
import tech.mtright.telegramhabrbot.bot.CallbackHandler;
import tech.mtright.telegramhabrbot.cache.DataCache;
import tech.mtright.telegramhabrbot.cache.UserData;

@Component
@Log4j2
public abstract class AbstractHandlerSubscribeCallback implements CallbackHandler {
    @UserData
    private DataCache dataCache;
    @Autowired
    private TelegramHabrBot bot;

    @Override
    public abstract String getHandledCallback();

    public abstract BotState getHandledState();

    public abstract String getReplyText();

    @Override
    @Transactional
    public BotApiMethod<?> getAnswer(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        dataCache.setUsersCurrentBotState(chatId, getHandledState());
        bot.deleteMessage(chatId, messageId);
        return new SendMessage(chatId, getReplyText());
    }
}
