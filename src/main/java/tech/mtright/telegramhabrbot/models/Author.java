package tech.mtright.telegramhabrbot.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Author implements Subscribable {
    @Id
    @GeneratedValue
    int id;
    String fullName;
    @Column(unique = true)
    String name;

    public Author(String fullName, String name) {
        this.fullName = fullName;
        this.name = name;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    Set<UserProfileData> users;
}
