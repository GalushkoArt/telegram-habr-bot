package tech.mtright.telegramhabrbot.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.mtright.telegramhabrbot.cache.DataCache;
import tech.mtright.telegramhabrbot.cache.UserData;
import tech.mtright.telegramhabrbot.models.*;
import tech.mtright.telegramhabrbot.repositories.HubRepository;
import tech.mtright.telegramhabrbot.repositories.PostRepository;
import tech.mtright.telegramhabrbot.repositories.TagRepository;

import java.util.*;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Service
@Log4j2
public class HabrPostNotificationService implements PostNotificationService {
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private HubRepository hubRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UsersProfileDataService usersProfileDataService;
    @Autowired
    private NotificationSenderService senderService;

    @Override
    @Transactional
    public boolean notifyUsers(Post post) {
        setUpPost(post);
        List<UserProfileData> allProfiles = usersProfileDataService.getAllProfiles();
        List<UserProfileData> usersToNotify = new ArrayList<>();
        for (UserProfileData profile : allProfiles) {
            if (isShouldBeNotified(profile, post)) {
                usersToNotify.add(profile);
            }
        }
        postRepository.save(post);
        List<Long> usersChatId = usersToNotify.stream().map(UserProfileData::getChatId).collect(toList());
        senderService.sendNotifications(usersChatId, post);
        return true;
    }

    private boolean isShouldBeNotified(UserProfileData profile, Post post) {
        if (profile.isFullSubscription()) {
            return true;
        }
        String company = post.getCompany();
        if (isCompanyShouldBeNotified(profile.getSubscribedCompanies(), company)) {
            return true;
        }
        String author = post.getAuthor();
        if (isAuthorShouldBeNotified(profile.getSubscribedAuthors(), author)) {
            return true;
        }
        Set<String> hubs = post.getHubs().stream().map(Hub::getName).collect(toSet());
        if (isHubsShouldBeNotified(profile.getSubscribedHubs(), hubs)) {
            return true;
        }
        Set<String> tags = post.getTags().stream().map(Tag::getName).collect(toSet());
        return isTagsShouldBeNotified(profile.getSubscribedTags(), tags);
    }

    private boolean isTagsShouldBeNotified(Set<Tag> subscribedTags, Set<String> tags) {
        return subscribedTags.stream().map(Tag::getName).anyMatch(tags::contains);
    }

    private boolean isHubsShouldBeNotified(Set<Hub> subscribedHubs, Set<String> hubs) {
        return subscribedHubs.stream().map(Hub::getName).anyMatch(hubs::contains);
    }

    private boolean isAuthorShouldBeNotified(Set<Author> authors, String author) {
        return authors.stream().map(Author::getName).anyMatch(a -> a.equalsIgnoreCase(author));
    }

    private boolean isCompanyShouldBeNotified(Set<Company> companies, String company) {
        return companies.stream().map(Company::getName).anyMatch(c -> c.equalsIgnoreCase(company));
    }

    private void setUpPost(Post post) {
        Set<Hub> hubs = post.getHubs();
        Set<Hub> repoHubs = new HashSet<>();
        Iterator<Hub> hubIterator = hubs.iterator();
        while (hubIterator.hasNext()) {
            Hub repoHub = hubRepository.findByNameIgnoreCase(hubIterator.next().getName());
            if (repoHub != null) {
                hubIterator.remove();
                repoHubs.add(repoHub);
            }
        }
        hubs.addAll(repoHubs);
        Set<Tag> tags = post.getTags();
        Set<Tag> repoTags = new HashSet<>();
        Iterator<Tag> tagIterator = tags.iterator();
        while (tagIterator.hasNext()) {
            Tag repoTag = tagRepository.findByNameIgnoreCase(tagIterator.next().getName());
            if (repoTag != null) {
                tagIterator.remove();
                repoTags.add(repoTag);
            }
        }
        tags.addAll(repoTags);
    }
}
