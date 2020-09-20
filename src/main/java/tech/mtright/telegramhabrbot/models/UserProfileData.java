package tech.mtright.telegramhabrbot.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileData {
    @Id
    @GeneratedValue
    int Id;
    long chatId;
    boolean fullSubscription;

    @ManyToMany(cascade = CascadeType.ALL)
    Set<Post> savedPosts;
    @ManyToMany(cascade = CascadeType.ALL)
    Set<Tag> subscribedTags;
    @ManyToMany(cascade = CascadeType.ALL)
    Set<Author> subscribedAuthors;
    @ManyToMany(cascade = CascadeType.ALL)
    Set<Hub> subscribedHubs;
    @ManyToMany(cascade = CascadeType.ALL)
    Set<Company> subscribedCompanies;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfileData that = (UserProfileData) o;
        return chatId == that.chatId &&
                fullSubscription == that.fullSubscription;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, fullSubscription);
    }
}
