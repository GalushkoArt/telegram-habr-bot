package tech.mtright.telegramhabrbot.bot.saved_posts_menu.handlers.callbacks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import tech.mtright.telegramhabrbot.bot.CallbackHandler;
import tech.mtright.telegramhabrbot.cache.DataCache;
import tech.mtright.telegramhabrbot.cache.UserData;
import tech.mtright.telegramhabrbot.models.Post;
import tech.mtright.telegramhabrbot.models.UserProfileData;
import tech.mtright.telegramhabrbot.repositories.PostRepository;
import tech.mtright.telegramhabrbot.services.ReplyMessagesService;
import tech.mtright.telegramhabrbot.services.UsersProfileDataService;

import java.util.Set;

import static tech.mtright.telegramhabrbot.bot.saved_posts_menu.SavePostCallbackButtons.SAVE_POST_BUTTON;

@Component
public class HandlerSavePostCallback implements CallbackHandler {
    @Autowired
    private ReplyMessagesService messagesService;
    @Autowired
    private UsersProfileDataService dataService;
    @Autowired
    private PostRepository postRepository;
    @UserData
    private DataCache dataCache;

    @Override
    public String getHandledCallback() {
        return SAVE_POST_BUTTON.split(":")[0];
    }

    @Override
    @Transactional
    public BotApiMethod<?> getAnswer(CallbackQuery callbackQuery) {
        String answer;
        Long chatId = callbackQuery.getMessage().getChatId();
        int callbackPostId = Integer.parseInt(callbackQuery.getData().split(":")[1]);
        UserProfileData userProfileData = dataService.getUserProfileData(chatId);
        if (isPostSaved(userProfileData.getSavedPosts(), callbackPostId)) {
            userProfileData.getSavedPosts().removeIf(post -> post.getPostId() == callbackPostId);
            answer = messagesService.getReplyText("reply.postNoLongerSaved");
        } else {
            Post post = postRepository.findByPostId(callbackPostId);
            userProfileData.getSavedPosts().add(post);
            answer = messagesService.getReplyText("reply.postSavedNow");
        }
        dataService.saveUserProfileData(userProfileData);
        dataCache.saveUserProfileData(chatId, userProfileData);
        return messagesService.getAnswerCallbackQuery(answer, false, callbackQuery);
    }

    private boolean isPostSaved(Set<Post> savedPosts, int searchPostId) {
        return savedPosts.stream().map(Post::getPostId).anyMatch(postId -> searchPostId == postId);
    }

}
