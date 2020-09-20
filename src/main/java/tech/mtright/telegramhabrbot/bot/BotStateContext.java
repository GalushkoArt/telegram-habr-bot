package tech.mtright.telegramhabrbot.bot;

import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class BotStateContext {
    private final Map<BotState, InputMessageHandler> messageHandlers = new HashMap<>();
    private final Map<String, CallbackHandler> callbackHandlers = new HashMap<>();

    @Autowired
    public BotStateContext(List<InputMessageHandler> messageHandlers, List<CallbackHandler> callbackHandlers) {
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandledState(), handler));
        callbackHandlers.forEach(handler -> this.callbackHandlers.put(handler.getHandledCallback(), handler));
    }

    public SendMessage processInputMessage(BotState currentState, Message message) {
        InputMessageHandler currentMessageHandler = messageHandlers.get(currentState);
        return currentMessageHandler.handle(message);
    }

    public BotApiMethod<?> processInputCallback(String data, CallbackQuery callbackQuery) {
        CallbackHandler callbackHandler = callbackHandlers.get(data);
        return callbackHandler.getAnswer(callbackQuery);
    }
}





