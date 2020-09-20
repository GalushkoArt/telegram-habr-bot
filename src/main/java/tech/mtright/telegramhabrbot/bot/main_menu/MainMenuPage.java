package tech.mtright.telegramhabrbot.bot.main_menu;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import tech.mtright.telegramhabrbot.TelegramHabrBot;
import tech.mtright.telegramhabrbot.bot.AbstractPage;
import tech.mtright.telegramhabrbot.bot.BotState;
import tech.mtright.telegramhabrbot.services.ReplyMessagesService;

import java.util.ArrayList;
import java.util.List;

import static tech.mtright.telegramhabrbot.bot.BotState.WELCOME;

@Component
public class MainMenuPage extends AbstractPage {
    public MainMenuPage(TelegramHabrBot bot, ReplyMessagesService messagesService) {
        super(null, bot, messagesService);
    }

    @Override
    public SendMessage getPage(long chatId) {
        return messagesService.getReplyMessage(chatId, "reply.mainMenu").setReplyMarkup(getMainMenuKeyboard());
    }

    public ReplyKeyboardMarkup getMainMenuKeyboard() {
        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        row1.add(new KeyboardButton("Подписаться"));
        row2.add(new KeyboardButton("Мои подписки"));
        row3.add(new KeyboardButton("Сохраненные статьи"));
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    @Override
    public BotState getState() {
        return WELCOME;
    }
}
