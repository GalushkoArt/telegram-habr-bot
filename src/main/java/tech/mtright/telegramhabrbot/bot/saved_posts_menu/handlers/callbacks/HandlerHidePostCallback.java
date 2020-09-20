package tech.mtright.telegramhabrbot.bot.saved_posts_menu.handlers.callbacks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import tech.mtright.telegramhabrbot.bot.CallbackHandler;
import tech.mtright.telegramhabrbot.services.ReplyMessagesService;

import static tech.mtright.telegramhabrbot.bot.saved_posts_menu.SavePostCallbackButtons.HIDE_POST_BUTTON;
import static tech.mtright.telegramhabrbot.bot.saved_posts_menu.SavePostCallbackButtons.SAVE_POST_BUTTON;

@Component
public class HandlerHidePostCallback implements CallbackHandler {
    @Autowired
    private ReplyMessagesService messagesService;

    @Override
    public String getHandledCallback() {
        return HIDE_POST_BUTTON;
    }

    @Override
    public BotApiMethod<?> getAnswer(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        return messagesService.getDeleteMessage(chatId, messageId);
    }
}
