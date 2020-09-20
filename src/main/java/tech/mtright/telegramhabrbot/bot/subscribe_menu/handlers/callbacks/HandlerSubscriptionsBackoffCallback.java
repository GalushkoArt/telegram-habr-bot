package tech.mtright.telegramhabrbot.bot.subscribe_menu.handlers.callbacks;

import org.springframework.stereotype.Component;
import tech.mtright.telegramhabrbot.bot.AbstractBackOffHandler;

import static tech.mtright.telegramhabrbot.bot.subscribe_menu.SubscribeCallbackButtons.BACK_OFF_SUBSCRIPTION_BUTTON;

@Component
public class HandlerSubscriptionsBackoffCallback extends AbstractBackOffHandler {

    @Override
    public String getHandledCallback() {
        return BACK_OFF_SUBSCRIPTION_BUTTON;
    }
}
