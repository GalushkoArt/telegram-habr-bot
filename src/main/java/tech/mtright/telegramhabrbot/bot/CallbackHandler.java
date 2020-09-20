package tech.mtright.telegramhabrbot.bot;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface CallbackHandler {
    String getHandledCallback();

    BotApiMethod<?> getAnswer(CallbackQuery callbackQuery);
}
