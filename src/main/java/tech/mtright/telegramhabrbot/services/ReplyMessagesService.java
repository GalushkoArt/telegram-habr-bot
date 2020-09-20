package tech.mtright.telegramhabrbot.services;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;


@Service
public class ReplyMessagesService {

    private final LocaleMessageService localeMessageService;

    public ReplyMessagesService(LocaleMessageService messageService) {
        this.localeMessageService = messageService;
    }

    public SendMessage getReplyMessage(long chatId, String replyMessage) {
        return new SendMessage(chatId, localeMessageService.getMessage(replyMessage));
    }

    public SendMessage getReplyMessage(long chatId, String replyMessage, Object... args) {
        return new SendMessage(chatId, localeMessageService.getMessage(replyMessage, args));
    }

    public String getReplyText(String replyText) {
        return localeMessageService.getMessage(replyText);
    }

    public String getReplyText(String replyText, Object... args) {
        return localeMessageService.getMessage(replyText, args);
    }

    public AnswerCallbackQuery getAnswerCallbackQuery(String text, boolean alert, CallbackQuery callbackquery) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackquery.getId())
                            .setShowAlert(alert)
                            .setText(text);
        return answerCallbackQuery;
    }

    public EditMessageText getEditMessageText(String inlineMessageId, long chatId, int messageId, InlineKeyboardMarkup replyMarkup, String text) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setInlineMessageId(inlineMessageId)
                        .setChatId(chatId)
                        .setMessageId(messageId)
                        .setText(text)
                        .setReplyMarkup(replyMarkup);
        return editMessageText;
    }

    public DeleteMessage getDeleteMessage(long chatId, int messageId) {
        return new DeleteMessage(chatId, messageId);
    }
}
