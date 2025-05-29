package ru.erulaev.restaurantvoting.user.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.erulaev.restaurantvoting.common.model.NamedEntity;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "restaurant", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Restaurant extends NamedEntity {

    @OneToMany
    @JoinColumn(name = "restaurant_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OrderBy("date DESC")
    private List<Menu> menus;

    @OneToMany
    @JoinColumn(name = "restaurant_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Vote> votes;

    public Restaurant(String name) {
        super(name);
        this.name = name;
    }

    public String toString() {
        return "Restaurant:" + id + '[' + name + ']';
    }
}
