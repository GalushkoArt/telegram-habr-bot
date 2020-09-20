package tech.mtright.telegramhabrbot.bot.subscribe_menu.handlers.callbacks;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.mtright.telegramhabrbot.bot.BotState;
import tech.mtright.telegramhabrbot.services.ReplyMessagesService;

import static tech.mtright.telegramhabrbot.bot.BotState.SUBSCRIBE_COMPANY;
import static tech.mtright.telegramhabrbot.bot.subscribe_menu.SubscribeCallbackButtons.SUBSCRIBE_COMPANY_BUTTON;

@Component
@Log4j2
public class HandlerSubscribeCompanySubscribeCallback extends AbstractHandlerSubscribeCallback {
    @Autowired
    private ReplyMessagesService messagesService;

    @Override
    public String getHandledCallback() {
        return SUBSCRIBE_COMPANY_BUTTON;
    }

    @Override
    public BotState getHandledState() {
        return SUBSCRIBE_COMPANY;
    }

    @Override
    public String getReplyText() {
        return messagesService.getReplyText("reply.askCompanySubscription");
    }
}
