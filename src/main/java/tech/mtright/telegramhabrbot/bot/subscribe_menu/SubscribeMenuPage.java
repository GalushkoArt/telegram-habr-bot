package tech.mtright.telegramhabrbot.bot.subscribe_menu;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import tech.mtright.telegramhabrbot.TelegramHabrBot;
import tech.mtright.telegramhabrbot.bot.AbstractPage;
import tech.mtright.telegramhabrbot.bot.BotState;
import tech.mtright.telegramhabrbot.bot.MenuPage;
import tech.mtright.telegramhabrbot.bot.main_menu.MainMenuPage;
import tech.mtright.telegramhabrbot.cache.DataCache;
import tech.mtright.telegramhabrbot.cache.UserData;
import tech.mtright.telegramhabrbot.models.UserProfileData;
import tech.mtright.telegramhabrbot.services.ReplyMessagesService;

import java.util.List;

import static tech.mtright.telegramhabrbot.bot.BotState.SUBSCRIBE;
import static tech.mtright.telegramhabrbot.bot.subscribe_menu.SubscribeCallbackButtons.*;

@MenuPage
public class SubscribeMenuPage extends AbstractPage {
    @UserData
    private DataCache dataCache;

    public SubscribeMenuPage(MainMenuPage parent, TelegramHabrBot bot, ReplyMessagesService messagesService) {
        super(parent, bot, messagesService);
    }

    @Override
    public SendMessage getPage(long chatId) {
        SendMessage replyToUser = messagesService.getReplyMessage(chatId, "reply.askSubscription");
        replyToUser.setReplyMarkup(getInlineMessageButtons(chatId));
        return replyToUser;
    }

    public InlineKeyboardMarkup getInlineMessageButtons(long chatId) {
        UserProfileData userData = dataCache.getUserProfileData(chatId);
        String fullSubscriptionText;
        if (userData.isFullSubscription()) {
            fullSubscriptionText = "Отписаться от всего";
        } else {
            fullSubscriptionText = "Подписаться на всё";
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonAllSubscribe = new InlineKeyboardButton().setText(fullSubscriptionText);
        InlineKeyboardButton buttonSubscribeHub = new InlineKeyboardButton().setText("На хаб");
        InlineKeyboardButton buttonSubscribeTag = new InlineKeyboardButton().setText("На топик");
        InlineKeyboardButton buttonSubscribeAuthor = new InlineKeyboardButton().setText("На автора");
        InlineKeyboardButton buttonSubscribeCompany = new InlineKeyboardButton().setText("На компанию");
        InlineKeyboardButton buttonBack = new InlineKeyboardButton().setText("Назад");

        buttonAllSubscribe.setCallbackData(SUBSCRIBE_ALL_BUTTON);
        buttonSubscribeHub.setCallbackData(SUBSCRIBE_HUB_BUTTON);
        buttonSubscribeTag.setCallbackData(SUBSCRIBE_TAG_BUTTON);
        buttonSubscribeAuthor.setCallbackData(SUBSCRIBE_AUTHOR_BUTTON);
        buttonSubscribeCompany.setCallbackData(SUBSCRIBE_COMPANY_BUTTON);
        buttonBack.setCallbackData(BACK_OFF_SUBSCRIPTION_BUTTON);

        List<InlineKeyboardButton> keyboardRow1 = List.of(buttonAllSubscribe);
        List<InlineKeyboardButton> keyboardRow2 = List.of(buttonSubscribeHub, buttonSubscribeTag);
        List<InlineKeyboardButton> keyboardRow3 = List.of(buttonSubscribeAuthor, buttonSubscribeCompany);
        List<InlineKeyboardButton> keyboardRow4 = List.of(buttonBack);
        List<List<InlineKeyboardButton>> rowList = List.of(keyboardRow1, keyboardRow2, keyboardRow3, keyboardRow4);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    @Override
    public BotState getState() {
        return SUBSCRIBE;
    }
}
