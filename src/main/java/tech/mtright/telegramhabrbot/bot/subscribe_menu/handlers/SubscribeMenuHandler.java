package tech.mtright.telegramhabrbot.bot.subscribe_menu.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import tech.mtright.telegramhabrbot.TelegramHabrBot;
import tech.mtright.telegramhabrbot.bot.BotState;
import tech.mtright.telegramhabrbot.bot.InputMessageHandler;
import tech.mtright.telegramhabrbot.bot.subscribe_menu.SubscribeMenuPage;
import tech.mtright.telegramhabrbot.services.ReplyMessagesService;

@Component
public class SubscribeMenuHandler implements InputMessageHandler {
    @Autowired
    private SubscribeMenuPage subscribeMenuPage;
    @Autowired
    private ReplyMessagesService messagesService;
    @Autowired
    private TelegramHabrBot bot;

    @Override
    public SendMessage handle(Message message) {
        String text = message.getText();
        Long chatId = message.getChatId();
        if (text.equals("Подписаться")) {
            return subscribeMenuPage.getPage(chatId);
        } else if (text.equalsIgnoreCase("назад")) {
            Integer messageId = message.getMessageId();
            bot.deleteMessage(chatId, messageId);
            return subscribeMenuPage.getParent().getPage(chatId);
        }
        return new SendMessage(chatId, messagesService.getReplyText("reply.dontUnderstand"));
    }

    @Override
    public BotState getHandledState() {
        return BotState.SUBSCRIBE;
    }
}
