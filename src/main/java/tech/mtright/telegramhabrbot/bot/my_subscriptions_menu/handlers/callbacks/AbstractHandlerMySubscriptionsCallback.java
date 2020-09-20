package tech.mtright.telegramhabrbot.bot.my_subscriptions_menu.handlers.callbacks;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import tech.mtright.telegramhabrbot.TelegramHabrBot;
import tech.mtright.telegramhabrbot.bot.my_subscriptions_menu.MySubscriptionsMenuPage;
import tech.mtright.telegramhabrbot.cache.DataCache;
import tech.mtright.telegramhabrbot.cache.UserData;
import tech.mtright.telegramhabrbot.models.Hub;
import tech.mtright.telegramhabrbot.models.Subscribable;
import tech.mtright.telegramhabrbot.models.UserProfileData;

import java.util.List;
import java.util.Set;

@Log4j2
@Component
public abstract class AbstractHandlerMySubscriptionsCallback implements MySubscriptionCallbackHandler {
    @Autowired
    private TelegramHabrBot bot;
    @Autowired
    private MySubscriptionsMenuPage mySubscriptionsMenuPage;

    @Override
    @Transactional
    public void showSubscriptions(long chatId) {
        Set<? extends Subscribable> set = getSet(chatId);
        if (set != null) {
            set.stream()
                    .map(item -> new SendMessage(chatId, getTarget() + ": " + item.getName()).setReplyMarkup(getButtonForItem(item.getId())))
                    .forEach(bot::sendMessage);
        }
    }

    public abstract String getTarget();

    public abstract String getCallBackForItem();

    @Override
    public InlineKeyboardMarkup getButtonForItem(int id) {
        InlineKeyboardButton button = new InlineKeyboardButton().setText("Отписаться")
                .setCallbackData(getCallBackForItem() + id);
        return new InlineKeyboardMarkup(List.of(List.of(button)));
    }

    @Override
    public abstract String getHandledCallback();

    public abstract String getNoItemsAnswer();

    public abstract String getAnswerBeforeItems();

    public abstract Set<? extends Subscribable> getSet(long chatId);

    @Override
    @Transactional
    public BotApiMethod<?> getAnswer(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        Set<?> set = getSet(chatId);
        if (set == null || set.size() == 0) {
            bot.sendMessage(chatId, getNoItemsAnswer());
        } else {
            bot.sendMessage(chatId, getAnswerBeforeItems());
            showSubscriptions(chatId);
        }
        bot.deleteMessage(chatId, messageId);
        return mySubscriptionsMenuPage.getPage(chatId);
    }
}
