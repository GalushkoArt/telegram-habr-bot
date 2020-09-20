package tech.mtright.telegramhabrbot.bot.my_subscriptions_menu.handlers.callbacks.unsubscribe;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tech.mtright.telegramhabrbot.bot.my_subscriptions_menu.handlers.callbacks.HandlerMyHubSubscriptionsCallback;
import tech.mtright.telegramhabrbot.models.Tag;
import tech.mtright.telegramhabrbot.models.UserProfileData;
import tech.mtright.telegramhabrbot.repositories.TagRepository;

import java.util.Set;

@Component
public class HandlerUnsubscribeMeFromTagCallback extends AbstractHandlerUnsubscribeMeCallback<Tag> {

    public HandlerUnsubscribeMeFromTagCallback(TagRepository repository, HandlerMyHubSubscriptionsCallback handler) {
        super(repository, handler);
    }

    @Override
    public String getHandledCallback() {
        return "FromTag";
    }

    @Override
    public String getTarget() {
        return "Тег";
    }

    @Override
    @Transactional
    public String execute(int id, long chatId) {
        Tag t = repository.getOne(id);
        UserProfileData profileData = dataService.getUserProfileData(chatId);
        Set<Tag> set = profileData.getSubscribedTags();
        String answer = getAnswer(set, id, t);
        dataService.saveUserProfileData(profileData);
        dataCache.saveUserProfileData(chatId, profileData);
        return answer;
    }
}
