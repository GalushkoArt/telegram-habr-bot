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
public class Hub implements Subscribable {
    @Id
    @GeneratedValue
    int id;
    @Column(unique = true)
    String name;

    public Hub(String name) {
        this.name = name;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    Set<UserProfileData> users;

    @Override
    public String toString() {
        return name;
    }
}
