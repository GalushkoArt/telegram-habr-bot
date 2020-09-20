package tech.mtright.telegramhabrbot.bot.saved_posts_menu;

import org.springframework.beans.factory.annotation.Autowired;
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
import tech.mtright.telegramhabrbot.models.Post;
import tech.mtright.telegramhabrbot.models.UserProfileData;
import tech.mtright.telegramhabrbot.repositories.UserProfileDataRepository;
import tech.mtright.telegramhabrbot.services.ReplyMessagesService;

import java.util.List;
import java.util.Set;

import static tech.mtright.telegramhabrbot.bot.BotState.MY_SAVED_POSTS;
import static tech.mtright.telegramhabrbot.bot.BotState.WELCOME;
import static tech.mtright.telegramhabrbot.bot.saved_posts_menu.SavePostCallbackButtons.HIDE_POST_BUTTON;
import static tech.mtright.telegramhabrbot.bot.saved_posts_menu.SavePostCallbackButtons.SAVE_POST_BUTTON;

@MenuPage
public class SavedPostsPage extends AbstractPage {
    @Autowired
    private UserProfileDataRepository profileRepository;
    @Autowired
    private MainMenuPage mainMenu;
    @UserData
    private DataCache dataCache;

    public SavedPostsPage(MainMenuPage parent, TelegramHabrBot bot, ReplyMessagesService messagesService) {
        super(parent, bot, messagesService);
    }

    @Override
    public SendMessage getPage(long chatId) {
        UserProfileData profileData = profileRepository.findByChatId(chatId);
        Set<Post> savedPosts = profileData.getSavedPosts();
        dataCache.setUsersCurrentBotState(chatId, WELCOME);
        TelegramHabrBot bot = getBot();
        if (savedPosts == null || savedPosts.size() == 0) {
            bot.sendMessage(messagesService.getReplyMessage(chatId, "reply.noSavedPosts"));
        } else {
            bot.sendMessage(chatId, messagesService.getReplyText("reply.showSavedPosts"));
            savedPosts.forEach(post -> bot.sendMessage(getMessageForPost(chatId, post)));
        }
        return mainMenu.getPage(chatId);
    }

    public InlineKeyboardMarkup getButtonsForPost(int postId) {
        InlineKeyboardButton saveButton = new InlineKeyboardButton().setText("Сохранить")
                .setCallbackData(SAVE_POST_BUTTON + postId);
        InlineKeyboardButton hideButton = new InlineKeyboardButton().setText("Скрыть")
                .setCallbackData(HIDE_POST_BUTTON);
        return new InlineKeyboardMarkup(List.of(List.of(saveButton), List.of(hideButton)));
    }

    public SendMessage getMessageForPost(long chatId, Post post) {
        return new SendMessage(chatId, post.toString()).setReplyMarkup(getButtonsForPost(post.getPostId()));
    }

    public SendMessage getMessageForPost(long chatId, Post post, String text) {
        return new SendMessage(chatId, text +"\n" +post.toString()).setReplyMarkup(getButtonsForPost(post.getPostId()));
    }

    @Override
    public BotState getState() {
        return MY_SAVED_POSTS;
    }
}
