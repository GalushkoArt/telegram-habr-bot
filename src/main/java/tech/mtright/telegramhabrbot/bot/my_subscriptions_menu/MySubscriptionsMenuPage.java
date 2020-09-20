package tech.mtright.telegramhabrbot.bot.my_subscriptions_menu;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import tech.mtright.telegramhabrbot.TelegramHabrBot;
import tech.mtright.telegramhabrbot.bot.AbstractPage;
import tech.mtright.telegramhabrbot.bot.BotState;
import tech.mtright.telegramhabrbot.bot.MenuPage;
import tech.mtright.telegramhabrbot.bot.main_menu.MainMenuPage;
import tech.mtright.telegramhabrbot.services.ReplyMessagesService;

import java.util.List;

import static tech.mtright.telegramhabrbot.bot.BotState.MY_SUBSCRIPTIONS;
import static tech.mtright.telegramhabrbot.bot.my_subscriptions_menu.MySubscriptionsCallbackButtons.*;

@MenuPage
public class MySubscriptionsMenuPage extends AbstractPage {
    public MySubscriptionsMenuPage(MainMenuPage parent, TelegramHabrBot bot, ReplyMessagesService messagesService) {
        super(parent, bot, messagesService);
    }

    @Override
    public SendMessage getPage(long chatId) {
        SendMessage replyToUser = messagesService.getReplyMessage(chatId, "reply.askMySubscription");
        replyToUser.setReplyMarkup(getInlineMessageButtons());
        return replyToUser;
    }

    public InlineKeyboardMarkup getInlineMessageButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton allSubscriptions = new InlineKeyboardButton().setText("Все подписки");
        InlineKeyboardButton hubSubscriptions = new InlineKeyboardButton().setText("На хабы");
        InlineKeyboardButton tagSubscriptions = new InlineKeyboardButton().setText("На теги");
        InlineKeyboardButton authorSubscriptions = new InlineKeyboardButton().setText("На авторов");
        InlineKeyboardButton companySubscriptions = new InlineKeyboardButton().setText("На компанию");
        InlineKeyboardButton buttonBack = new InlineKeyboardButton().setText("Назад");

        allSubscriptions.setCallbackData(ALL_MY_SUBSCRIPTIONS_BUTTON);
        hubSubscriptions.setCallbackData(MY_HUB_SUBSCRIPTIONS_BUTTON);
        tagSubscriptions.setCallbackData(MY_TAG_SUBSCRIPTIONS_BUTTON);
        authorSubscriptions.setCallbackData(MY_AUTHOR_SUBSCRIPTIONS_BUTTON);
        companySubscriptions.setCallbackData(MY_COMPANY_SUBSCRIPTIONS_BUTTON);
        buttonBack.setCallbackData(BACK_OFF_MY_SUBSCRIPTIONS_BUTTON);

        List<InlineKeyboardButton> keyboardRow1 = List.of(allSubscriptions);
        List<InlineKeyboardButton> keyboardRow2 = List.of(hubSubscriptions, tagSubscriptions);
        List<InlineKeyboardButton> keyboardRow3 = List.of(authorSubscriptions, companySubscriptions);
        List<InlineKeyboardButton> keyboardRow4 = List.of(buttonBack);
        List<List<InlineKeyboardButton>> rowList = List.of(keyboardRow1, keyboardRow2, keyboardRow3, keyboardRow4);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    @Override
    public BotState getState() {
        return MY_SUBSCRIPTIONS;
    }
}
