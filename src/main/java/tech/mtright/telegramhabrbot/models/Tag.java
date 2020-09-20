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
public class Tag implements Subscribable {
    @Id
    @GeneratedValue
    int id;
    @Column(unique = true)
    String name;

    @ManyToMany(cascade = CascadeType.ALL)
    Set<UserProfileData> users;

    public Tag(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
