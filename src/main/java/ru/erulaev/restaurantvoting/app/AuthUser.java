package ru.erulaev.restaurantvoting.app;

import lombok.Getter;
import org.springframework.lang.NonNull;
import ru.erulaev.restaurantvoting.user.model.User;

@Getter
public class AuthUser extends org.springframework.security.core.userdetails.User {

    private final User user;

    public AuthUser(@NonNull User user) {
        super(user.getEmail(), user.getPassword(), user.isEnabled(), true, true, true, user.getRoles());
        this.user = user;
    }

    public long id() {
        return user.id();
    }

    @Override
    public String toString() {
        return "AuthUser:" + id() + '[' + user.getEmail() + ']';
    }
}