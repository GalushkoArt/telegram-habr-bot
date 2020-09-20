package tech.mtright.telegramhabrbot.bot.my_subscriptions_menu.handlers.callbacks;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import tech.mtright.telegramhabrbot.bot.CallbackHandler;

public interface MySubscriptionCallbackHandler extends CallbackHandler {
    void showSubscriptions(long chatId);

    InlineKeyboardMarkup getButtonForItem(int id);
}
