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
                "SELECT COUNT(v) FROM Vote v WHERE v.date = :date AND v.restaurant.id = :restaurantId"),
        @NamedQuery(name = Vote.DELETE, query =
                "DELETE FROM Vote v WHERE v.user.id = :userId AND v.date = :date"),
        @NamedQuery(name = Vote.GET_BY_USER_ID_AND_DATE, query =
                "SELECT v FROM Vote v WHERE v.user.id = :userId AND v.date = :date"),
        @NamedQuery(name = Vote.GET_BY_USER_ID, query =
                "SELECT v FROM Vote v WHERE v.user.id = :userId ORDER BY v.date DESC")
})
@Entity
@Table(name = "vote",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "date"}, name = "uk_user_date"),
        indexes = @Index(name = "date_restaurant", columnList = "date, restaurant_id"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Vote extends BaseEntity {

    public static final String COUNT = "Vote.getCountByDateAndRestaurantId";
    public static final String DELETE = "Vote.delete";
    public static final String GET_BY_USER_ID_AND_DATE = "Vote.getByUserIdAndDate";
    public static final String GET_BY_USER_ID = "Vote.findAllByUserId";

    @Column(name = "date", nullable = false, columnDefinition = "date default current_date")
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurant;

    public Vote(Long id, LocalDate date, User user, Restaurant restaurant) {
        super(id);
        this.date = date;
        this.user = user;
        this.restaurant = restaurant;
    }

    public Long getUserId() {
        return user.getId();
    }

    public Long getRestaurantId() {
        return restaurant.getId();
    }
}