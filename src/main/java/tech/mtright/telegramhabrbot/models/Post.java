package tech.mtright.telegramhabrbot.models;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Post {
    @Id
    @GeneratedValue
    int id;

    int postId;
    Date date;
    String title;
    String link;
    String author;
    int votes;
    int views;
    @Nullable
    String company;
    @ManyToMany(cascade = CascadeType.ALL)
    Set<Tag> tags;
    @ManyToMany(cascade = CascadeType.ALL)
    Set<Hub> hubs;

    @Override
    public String toString() {
        String companyName = company == null ? "" : "Компания: " + company + "\n";
        return title + '\n' +
                "Автор: " + author + '\n' +
                "Дата публикации: " + date + '\n' +
                companyName +
                "Голоса " + votes + "\n" +
                "Просмотров " + views + "\n" +
                "Теги: " + tags.stream().map(Tag::toString).collect(Collectors.joining(", ")) + '\n' +
                "Хабы: " + hubs.stream().map(Hub::toString).collect(Collectors.joining(", ")) + "\n" +
                "Ссылка: " + link;
    }

    public Post(int postId, Date date, String title, String link, String author, int votes, int views, @Nullable String company, Set<Tag> tags, Set<Hub> hubs) {
        this.postId = postId;
        this.date = date;
        this.title = title;
        this.link = link;
        this.author = author;
        this.votes = votes;
        this.views = views;
        this.company = company;
        this.tags = tags;
        this.hubs = hubs;
    }
}
