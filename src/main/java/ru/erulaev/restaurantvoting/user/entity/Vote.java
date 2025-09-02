package ru.erulaev.restaurantvoting.user.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.erulaev.restaurantvoting.common.entity.BaseEntity;

import java.time.LocalDate;

@NamedQueries({
        @NamedQuery(name = Vote.COUNT, query =
                "SELECT COUNT(v) FROM Vote v WHERE v.creationDate = :creationDate AND v.restaurant.id = :restaurantId"),
        @NamedQuery(name = Vote.DELETE, query =
                "DELETE FROM Vote v WHERE v.user.id = :userId AND v.creationDate = :creationDate"),
        @NamedQuery(name = Vote.GET_BY_USER_AND_DATE, query =
                "SELECT v FROM Vote v WHERE v.user.id = :userId AND v.creationDate = :creationDate"),
        @NamedQuery(name = Vote.GET_ALL_BY_USER, query =
                "SELECT v FROM Vote v WHERE v.user.id = :userId ORDER BY v.creationDate DESC"),
        @NamedQuery(name = Vote.IS_EXISTED, query =
                "SELECT (COUNT(v) > 0) from Vote v where v.user.id = :userId and v.creationDate = :creationDate")
})
@Entity
@Table(name = "vote",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "creation_date"}, name = "uk_user_date"),
        indexes = @Index(name = "date_restaurant_idx", columnList = "creation_date, restaurant_id"))
@NoArgsConstructor
@Getter
@Setter
public class Vote extends BaseEntity {

    static final String COUNT = "Vote.getCountByCreationDateAndRestaurantId";
    static final String DELETE = "Vote.delete";
    static final String GET_BY_USER_AND_DATE = "Vote.getByUserIdAndCreationDate";
    static final String GET_ALL_BY_USER = "Vote.getAllByUserId";
    static final String IS_EXISTED = "Vote.existsByUserIdAndCreationDate";

    @Column(name = "creation_date", nullable = false, columnDefinition = "date default current_date")
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurant;

    public Vote(Integer id, LocalDate creationDate, User user, Restaurant restaurant) {
        this.id = id;
        this.creationDate = creationDate;
        this.user = user;
        this.restaurant = restaurant;
    }

    public Integer getUserId() {
        return user.getId();
    }

    public Integer getRestaurantId() {
        return restaurant.getId();
    }
}