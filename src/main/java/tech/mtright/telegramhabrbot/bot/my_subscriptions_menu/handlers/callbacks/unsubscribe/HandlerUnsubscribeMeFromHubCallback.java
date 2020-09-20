package tech.mtright.telegramhabrbot.bot.my_subscriptions_menu.handlers.callbacks.unsubscribe;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tech.mtright.telegramhabrbot.bot.my_subscriptions_menu.handlers.callbacks.HandlerMyHubSubscriptionsCallback;
import tech.mtright.telegramhabrbot.models.Hub;
import tech.mtright.telegramhabrbot.models.UserProfileData;
import tech.mtright.telegramhabrbot.repositories.HubRepository;

import java.util.Set;

@Component
public class HandlerUnsubscribeMeFromHubCallback extends AbstractHandlerUnsubscribeMeCallback<Hub> {

    public HandlerUnsubscribeMeFromHubCallback(HubRepository repository, HandlerMyHubSubscriptionsCallback handler) {
        super(repository, handler);
    }

    @Override
    public String getHandledCallback() {
        return "FromHub";
    }

    @Override
    public String getTarget() {
        return "Хаб";
    }

    @Override
    @Transactional
    public String execute(int id, long chatId) {
        Hub t = repository.getOne(id);
        UserProfileData profileData = dataService.getUserProfileData(chatId);
        Set<Hub> set = profileData.getSubscribedHubs();
        String answer = getAnswer(set, id, t);
        dataService.saveUserProfileData(profileData);
        dataCache.saveUserProfileData(chatId, profileData);
        return answer;
    }
}
