package tech.mtright.telegramhabrbot.bot.my_subscriptions_menu.handlers.callbacks.unsubscribe;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface UnsubscribeHandler {
    String getHandledCallback();

    String getTarget();

    BotApiMethod<?> getAnswer(CallbackQuery callbackQuery);
}
