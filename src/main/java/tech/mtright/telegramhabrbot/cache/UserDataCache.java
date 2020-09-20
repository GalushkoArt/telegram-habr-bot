package tech.mtright.telegramhabrbot.cache;

import org.springframework.beans.factory.annotation.Autowired;
import tech.mtright.telegramhabrbot.bot.BotState;
import tech.mtright.telegramhabrbot.models.UserProfileData;
import tech.mtright.telegramhabrbot.services.UsersProfileDataService;

import java.util.HashMap;
import java.util.Map;


@UserData
public class UserDataCache implements DataCache {
    private final Map<Long, BotState> usersBotStates = new HashMap<>();
    private final Map<Long, UserProfileData> usersProfileData = new HashMap<>();

    @Autowired
    private UsersProfileDataService usersProfileDataService;

    @Override
    public void setUsersCurrentBotState(long chatId, BotState botState) {
        usersBotStates.put(chatId, botState);
    }

    @Override
    public BotState getUsersCurrentBotState(long chatId) {
        BotState botState = usersBotStates.get(chatId);
        if (botState == null) {
            botState = BotState.WELCOME;
        }
        return botState;
    }

    @Override
    public UserProfileData getUserProfileData(long chatId) {
        UserProfileData userProfileData = usersProfileData.get(chatId);
        if (userProfileData == null) {
            userProfileData = new UserProfileData();
        }
        return userProfileData;
    }

    @Override
    public void saveUserProfileData(long chatId, UserProfileData userProfileData) {
        usersProfileData.put(chatId, userProfileData);
    }

    @Override
    public boolean hasUser(long chatId) {
        return usersProfileData.containsKey(chatId);
    }

    @Override
    public void loadOrCreateUser(long chatId) {
        UserProfileData userData = usersProfileDataService.getUserProfileData(chatId);
        if (userData == null) {
            userData = usersProfileDataService.createUserProfile(chatId);
        }
        usersProfileData.put(chatId, userData);
    }
}
