package tech.mtright.telegramhabrbot.bot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import tech.mtright.telegramhabrbot.TelegramHabrBot;
import tech.mtright.telegramhabrbot.services.ReplyMessagesService;

@AllArgsConstructor
@Getter
public abstract class AbstractPage {
    protected final AbstractPage parent;
    protected final TelegramHabrBot bot;
    protected final ReplyMessagesService messagesService;

    public abstract SendMessage getPage(long chatId);

    public abstract BotState getState();

    public void showPage(long chatId) {
        bot.sendMessage(getPage(chatId));
    }
}
