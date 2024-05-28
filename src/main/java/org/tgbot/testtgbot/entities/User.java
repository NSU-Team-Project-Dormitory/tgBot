package org.tgbot.testtgbot.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "bot_user")
public class User {
    @Id
    private Long chatId;

    private String firstName;

    private String lastName;

    private String username;

    private Timestamp registeredTime;

    @Override
    public String toString() {
        return "User{" +
                "chatId=" + chatId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", registeredTime=" + registeredTime +
                '}';
    }
}
