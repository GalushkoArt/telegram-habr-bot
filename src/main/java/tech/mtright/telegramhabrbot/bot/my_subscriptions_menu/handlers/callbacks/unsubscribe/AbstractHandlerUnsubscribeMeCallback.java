package tech.mtright.telegramhabrbot.bot.my_subscriptions_menu.handlers.callbacks.unsubscribe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import tech.mtright.telegramhabrbot.TelegramHabrBot;
import tech.mtright.telegramhabrbot.bot.my_subscriptions_menu.handlers.callbacks.HandlerMyHubSubscriptionsCallback;
import tech.mtright.telegramhabrbot.cache.DataCache;
import tech.mtright.telegramhabrbot.cache.UserData;
import tech.mtright.telegramhabrbot.models.Hub;
import tech.mtright.telegramhabrbot.models.Subscribable;
import tech.mtright.telegramhabrbot.models.UserProfileData;
import tech.mtright.telegramhabrbot.repositories.HubRepository;
import tech.mtright.telegramhabrbot.services.ReplyMessagesService;
import tech.mtright.telegramhabrbot.services.UsersProfileDataService;

import java.util.Set;

public abstract class AbstractHandlerUnsubscribeMeCallback<T extends Subscribable> implements UnsubscribeHandler {
    protected final JpaRepository<T, Integer> repository;
    protected final HandlerMyHubSubscriptionsCallback handler;
    @UserData
    protected DataCache dataCache;
    @Autowired
    protected ReplyMessagesService messagesService;
    @Autowired
    protected UsersProfileDataService dataService;
    @Autowired
    private TelegramHabrBot bot;

    public AbstractHandlerUnsubscribeMeCallback(JpaRepository<T, Integer> repository, HandlerMyHubSubscriptionsCallback handler) {
        this.repository = repository;
        this.handler = handler;
    }

    @Override
    public abstract String getHandledCallback();

    @Override
    public abstract String getTarget();

    @Override
    @Transactional
    public BotApiMethod<?> getAnswer(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        int dataId = Integer.parseInt(callbackQuery.getData().split(":")[2]);
        String inlineMessageId = callbackQuery.getInlineMessageId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        String answer = execute(dataId, chatId);
        EditMessageText editMessage = messagesService.getEditMessageText(inlineMessageId, chatId, messageId,
                handler.getButtonForItem(dataId), getTarget() + ": " + repository.getOne(dataId));
        bot.sendByMethod(editMessage);
        return messagesService.getAnswerCallbackQuery(answer, false, callbackQuery);
    }


    public abstract String execute(int id, long chatId);

    public String getAnswer(Set<? extends Subscribable> set, int id, Subscribable s) {
        if (set != null && set.removeIf(item -> item.getId() == id)) {
            return messagesService.getReplyText("reply.nowYouUnsubscribed", s.getName());
        } else {
            return messagesService.getReplyText("reply.youDontSubscribed", s.getName());
        }
    }
}
