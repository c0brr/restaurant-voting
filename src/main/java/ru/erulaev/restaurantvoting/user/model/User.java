package ru.erulaev.restaurantvoting.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.NonNull;
import ru.erulaev.restaurantvoting.common.HasIdAndEmail;
import ru.erulaev.restaurantvoting.common.model.NamedEntity;
import ru.erulaev.restaurantvoting.common.validation.NoHtml;

import java.util.*;

@NamedQueries({
        @NamedQuery(name = User.DELETE, query = "DELETE FROM User u WHERE u.id = :id")
})
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class User extends NamedEntity implements HasIdAndEmail {

    public static final String DELETE = "User.delete";

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
    private Date registered = new Date();

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role"}, name = "uk_user_role"))
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Role> roles = EnumSet.noneOf(Role.class);

    public User(User u) {
        this(u.id, u.name, u.email, u.password, u.enabled, u.registered, u.roles);
    }

    public User(Long id, String name, String email, String password, Role... roles) {
        this(id, name, email, password, true, new Date(), Arrays.asList(roles));
    }

    public User(Long id, String name, String email, String password,
                boolean enabled, Date registered, @NonNull Collection<Role> roles) {
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

    public boolean hasRole(Role role) {
        return roles.contains(role);
    }

    @Override
    public String toString() {
        return "User:" + id + '[' + email + ']';
    }
}