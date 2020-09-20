package tech.mtright.telegramhabrbot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.mtright.telegramhabrbot.models.Post;
import tech.mtright.telegramhabrbot.services.PostNotificationService;

@RestController
@RequestMapping("/posts")
public class NewPostController {
    @Autowired
    private PostNotificationService notificationService;

    @PostMapping(value = "/newPost")
    public String receiveNewPost(@RequestBody Post post) {
        notificationService.notifyUsers(post);
        return "ACCEPTED";
    }
}
