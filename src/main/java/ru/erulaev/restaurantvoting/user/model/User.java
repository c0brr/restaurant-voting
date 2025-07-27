package ru.erulaev.restaurantvoting.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.NonNull;
import ru.erulaev.restaurantvoting.common.HasIdAndEmail;
import ru.erulaev.restaurantvoting.common.model.NamedEntity;
import ru.erulaev.restaurantvoting.common.validation.NoHtml;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;


@NamedQueries({
        @NamedQuery(name = User.GET_ALL, query = "SELECT u From User u ORDER BY u.name, u.email ASC"),
        @NamedQuery(name = User.GET_BY_EMAIL_CACHED, query = User.GET_BY_EMAIL_QUERY),
        @NamedQuery(name = User.GET_BY_EMAIL, query = User.GET_BY_EMAIL_QUERY)
})
@Entity
@Table(name = "users",
        indexes = @Index(name = "email_idx", columnList = "email"))
@NoArgsConstructor
@Getter
@Setter
public class User extends NamedEntity implements HasIdAndEmail {

    static final String GET_BY_EMAIL_QUERY = "SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email = LOWER(:email)";
    static final String GET_ALL = "User.getAll";
    static final String GET_BY_EMAIL_CACHED = "User.getByEmailIgnoreCaseCached";
    static final String GET_BY_EMAIL = "User.getByEmailIgnoreCase";

    @Column(name = "email", nullable = false, unique = true)
    @Email
    @NotBlank
    @Size(max = 128)
    @NoHtml   // https://stackoverflow.com/questions/17480809
    private String email;

    @Column(name = "password", nullable = false)
    @NotBlank
    @Size(max = 128)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "enabled", nullable = false, columnDefinition = "bool default true")
    @NotNull
    private boolean enabled = true;

    @Column(name = "registered", nullable = false, columnDefinition = "timestamp default current_timestamp", updatable = false)
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant registered = Instant.now();

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role"}, name = "uk_user_role"))
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Role> roles = EnumSet.noneOf(Role.class);

    @SuppressWarnings("CopyConstructorMissesField")
    public User(User u) {
        this(u.id, u.name, u.email, u.password, u.enabled, u.registered, u.roles);
    }

    public User(Long id, String name, String email, String password, Role... roles) {
        this(id, name, email, password, true, Instant.now(), Arrays.asList(roles));
    }

    public User(Long id, String name, String email, String password,
                boolean enabled, Instant registered, @NonNull Collection<Role> roles) {
        super(id, name);
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.registered = registered;
        setRoles(roles);
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles.isEmpty() ? EnumSet.noneOf(Role.class) : EnumSet.copyOf(roles);
    }

    @Override
    public String toString() {
        return "User:" + id + '[' + email + ']';
    }
}