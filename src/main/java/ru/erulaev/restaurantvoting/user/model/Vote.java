package ru.erulaev.restaurantvoting.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.erulaev.restaurantvoting.common.model.BaseEntity;

import java.time.LocalDate;

@NamedQueries({
        @NamedQuery(name = Vote.COUNT, query =
                "SELECT COUNT(v) FROM Vote v WHERE v.created = :created AND v.restaurant.id = :restaurantId"),
        @NamedQuery(name = Vote.GET, query = "SELECT v FROM Vote v WHERE v.id = :id AND v.user.id = :userId")
})
@Entity
@Table(name = "vote",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "created"}, name = "uk_user_vote_created"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Vote extends BaseEntity {

    public static final String COUNT = "Vote.getCountByDateAndRestaurantId";
    public static final String GET = "Vote.get";

    @Column(name = "created", nullable = false, columnDefinition = "date default '2025-06-06'")
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurant;

    public Vote(Long id, LocalDate created, User user, Restaurant restaurant) {
        super(id);
        this.created = created;
        this.user = user;
        this.restaurant = restaurant;
    }

    public long getUserId() {
        return user.getId();
    }

    public long getRestaurantId() {
        return restaurant.getId();
    }
}