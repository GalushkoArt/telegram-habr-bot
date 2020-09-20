package tech.mtright.telegramhabrbot.bot.my_subscriptions_menu.handlers.callbacks;

import org.springframework.stereotype.Component;
import tech.mtright.telegramhabrbot.bot.AbstractBackOffHandler;

import static tech.mtright.telegramhabrbot.bot.my_subscriptions_menu.MySubscriptionsCallbackButtons.BACK_OFF_MY_SUBSCRIPTIONS_BUTTON;

@Component
public class HandlerMySubscriptionsBackoffCallback extends AbstractBackOffHandler {

    @Override
    public String getHandledCallback() {
        return BACK_OFF_MY_SUBSCRIPTIONS_BUTTON;
    }

}
