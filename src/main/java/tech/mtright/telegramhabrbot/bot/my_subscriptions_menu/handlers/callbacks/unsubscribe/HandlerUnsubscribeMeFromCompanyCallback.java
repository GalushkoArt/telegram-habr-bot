package tech.mtright.telegramhabrbot.bot.my_subscriptions_menu.handlers.callbacks.unsubscribe;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tech.mtright.telegramhabrbot.bot.my_subscriptions_menu.handlers.callbacks.HandlerMyHubSubscriptionsCallback;
import tech.mtright.telegramhabrbot.models.Company;
import tech.mtright.telegramhabrbot.models.UserProfileData;
import tech.mtright.telegramhabrbot.repositories.CompanyRepository;

import java.util.Set;

@Component
public class HandlerUnsubscribeMeFromCompanyCallback extends AbstractHandlerUnsubscribeMeCallback<Company> {

    public HandlerUnsubscribeMeFromCompanyCallback(CompanyRepository repository, HandlerMyHubSubscriptionsCallback handler) {
        super(repository, handler);
    }

    @Override
    public String getHandledCallback() {
        return "FromCompany";
    }

    @Override
    public String getTarget() {
        return "Компания";
    }

    @Override
    @Transactional
    public String execute(int id, long chatId) {
        String answer;
        Company t = repository.getOne(id);
        UserProfileData profileData = dataService.getUserProfileData(chatId);
        Set<Company> set = profileData.getSubscribedCompanies();
        answer = getAnswer(set, id, t);
        dataService.saveUserProfileData(profileData);
        dataCache.saveUserProfileData(chatId, profileData);
        return answer;
    }
}
