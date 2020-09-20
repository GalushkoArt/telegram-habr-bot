package tech.mtright.telegramhabrbot.bot.subscribe_menu.handlers.callbacks;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import tech.mtright.telegramhabrbot.TelegramHabrBot;
import tech.mtright.telegramhabrbot.bot.CallbackHandler;
import tech.mtright.telegramhabrbot.bot.subscribe_menu.SubscribeMenuPage;
import tech.mtright.telegramhabrbot.cache.DataCache;
import tech.mtright.telegramhabrbot.cache.UserData;
import tech.mtright.telegramhabrbot.models.UserProfileData;
import tech.mtright.telegramhabrbot.services.ReplyMessagesService;
import tech.mtright.telegramhabrbot.services.UsersProfileDataService;

import static tech.mtright.telegramhabrbot.bot.subscribe_menu.SubscribeCallbackButtons.SUBSCRIBE_ALL_BUTTON;

@Component
@Log4j2
public class HandlerSubscribeAllCallback implements CallbackHandler {
    @UserData
    private DataCache dataCache;
    @Autowired
    private UsersProfileDataService dataService;
    @Autowired
    private ReplyMessagesService messagesService;
    @Autowired
    private SubscribeMenuPage subscribeMenuPage;

    @Override
    public String getHandledCallback() {
        return SUBSCRIBE_ALL_BUTTON;
    }

    @Override
    @Transactional
    public BotApiMethod<?> getAnswer(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        TelegramHabrBot bot = subscribeMenuPage.getBot();
        UserProfileData userProfileData = dataCache.getUserProfileData(chatId);
        String inlineMessageId = callbackQuery.getInlineMessageId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        String answer;
        if (!userProfileData.isFullSubscription()) {
            userProfileData.setFullSubscription(true);
            answer = messagesService.getReplyText("reply.nowYouSubscribedAll");
        } else {
            userProfileData.setFullSubscription(false);
            answer = messagesService.getReplyText("reply.nowYouUnsubscribedAll");
        }
        dataService.saveUserProfileData(userProfileData);
        EditMessageText editMessage = messagesService.getEditMessageText(inlineMessageId, chatId, messageId, subscribeMenuPage.getInlineMessageButtons(chatId), messagesService.getReplyText("reply.askSubscription"));
        bot.sendByMethod(editMessage);
        return messagesService.getAnswerCallbackQuery(answer, true, callbackQuery);
    }
}
