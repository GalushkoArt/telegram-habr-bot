package tech.mtright.telegramhabrbot.bot.my_subscriptions_menu.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import tech.mtright.telegramhabrbot.TelegramHabrBot;
import tech.mtright.telegramhabrbot.bot.BotState;
import tech.mtright.telegramhabrbot.bot.InputMessageHandler;
import tech.mtright.telegramhabrbot.bot.my_subscriptions_menu.MySubscriptionsMenuPage;
import tech.mtright.telegramhabrbot.cache.DataCache;
import tech.mtright.telegramhabrbot.cache.UserData;
import tech.mtright.telegramhabrbot.services.ReplyMessagesService;

import static tech.mtright.telegramhabrbot.bot.BotState.MY_SUBSCRIPTIONS;
import static tech.mtright.telegramhabrbot.bot.BotState.WELCOME;

@Component
public class MySubscriptionsMenuHandler implements InputMessageHandler {
    @Autowired
    private MySubscriptionsMenuPage mySubscriptionsMenuPage;
    @Autowired
    private ReplyMessagesService messagesService;
    @Autowired
    private TelegramHabrBot bot;

    @Override
    public SendMessage handle(Message message) {
        String text = message.getText();
        Long chatId = message.getChatId();
        if (text.equals("Мои подписки")) {
            return mySubscriptionsMenuPage.getPage(chatId);
        } else if (text.equalsIgnoreCase("назад")) {
            Integer messageId = message.getMessageId();
            bot.deleteMessage(chatId, messageId);
            return mySubscriptionsMenuPage.getParent().getPage(chatId);
        }
        return new SendMessage(chatId, messagesService.getReplyText("reply.dontUnderstand"));
    }

    @Override
    public BotState getHandledState() {
        return MY_SUBSCRIPTIONS;
    }
}
