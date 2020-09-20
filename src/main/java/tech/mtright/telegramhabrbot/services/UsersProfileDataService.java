package tech.mtright.telegramhabrbot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.mtright.telegramhabrbot.models.UserProfileData;
import tech.mtright.telegramhabrbot.repositories.UserProfileDataRepository;

import javax.transaction.Transactional;
import java.util.List;


@Service
@Transactional
public class UsersProfileDataService {
    private final UserProfileDataRepository profileRepository;

    @Autowired
    public UsersProfileDataService(UserProfileDataRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public List<UserProfileData> getAllProfiles() {
        return profileRepository.findAll();
    }

    public UserProfileData createUserProfile(long chatId) {
        UserProfileData user = UserProfileData.builder().chatId(chatId).fullSubscription(false).build();
        profileRepository.save(user);
        return user;
    }

    public void saveUserProfileData(UserProfileData userProfileData) {
        profileRepository.save(userProfileData);
    }

    public void deleteUsersProfileData(int profileDataId) {
        profileRepository.deleteById(profileDataId);
    }

    public UserProfileData getUserProfileData(long chatId) {
        return profileRepository.findByChatId(chatId);
    }
}
