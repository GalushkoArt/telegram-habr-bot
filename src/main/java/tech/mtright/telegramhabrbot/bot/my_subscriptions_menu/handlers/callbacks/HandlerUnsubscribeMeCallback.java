package tech.mtright.telegramhabrbot.bot.my_subscriptions_menu.handlers.callbacks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import tech.mtright.telegramhabrbot.TelegramHabrBot;
import tech.mtright.telegramhabrbot.bot.CallbackHandler;
import tech.mtright.telegramhabrbot.bot.my_subscriptions_menu.handlers.callbacks.unsubscribe.UnsubscribeHandler;
import tech.mtright.telegramhabrbot.services.ReplyMessagesService;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class HandlerUnsubscribeMeCallback implements CallbackHandler {
    private final Map<String, UnsubscribeHandler> handlers;
    @Autowired
    private TelegramHabrBot bot;
    @Autowired
    private ReplyMessagesService messagesService;

    @Autowired
    public HandlerUnsubscribeMeCallback(List<UnsubscribeHandler> handlers) {
        this.handlers = handlers.stream().collect(Collectors.toMap(UnsubscribeHandler::getHandledCallback, Function.identity()));
    }

    @Override
    public String getHandledCallback() {
        return "unsubscribeMe";
    }

    @Override
    public BotApiMethod<?> getAnswer(CallbackQuery callbackQuery) {
        String dataForHandler = callbackQuery.getData().split(":")[1];
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        UnsubscribeHandler handler = handlers.get(dataForHandler);
        bot.sendByMethod(handler.getAnswer(callbackQuery));
        return messagesService.getDeleteMessage(chatId, messageId);
    }
}
