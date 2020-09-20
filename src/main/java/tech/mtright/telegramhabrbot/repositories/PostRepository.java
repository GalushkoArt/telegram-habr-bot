package tech.mtright.telegramhabrbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.mtright.telegramhabrbot.models.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Post findByPostId(int postId);
}
