package tech.mtright.telegramhabrbot.bot.my_subscriptions_menu.handlers.callbacks.unsubscribe;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tech.mtright.telegramhabrbot.bot.my_subscriptions_menu.handlers.callbacks.HandlerMyHubSubscriptionsCallback;
import tech.mtright.telegramhabrbot.models.Author;
import tech.mtright.telegramhabrbot.models.UserProfileData;
import tech.mtright.telegramhabrbot.repositories.AuthorRepository;

import java.util.Set;

@Component
public class HandlerUnsubscribeMeFromAuthorCallback extends AbstractHandlerUnsubscribeMeCallback<Author> {

    public HandlerUnsubscribeMeFromAuthorCallback(AuthorRepository repository, HandlerMyHubSubscriptionsCallback handler) {
        super(repository, handler);
    }

    @Override
    public String getHandledCallback() {
        return "FromAuthor";
    }

    @Override
    public String getTarget() {
        return "Автор";
    }

    @Override
    @Transactional
    public String execute(int id, long chatId) {
        Author t = repository.getOne(id);
        UserProfileData profileData = dataService.getUserProfileData(chatId);
        Set<Author> set = profileData.getSubscribedAuthors();
        String answer = getAnswer(set, id, t);
        dataService.saveUserProfileData(profileData);
        dataCache.saveUserProfileData(chatId, profileData);
        return answer;
    }
}
