package ru.erulaev.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "vote", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "registered"}))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Vote extends AbstractBaseEntity {

    @Column(name = "registered", nullable = false, columnDefinition = "datetime default now()")
    @NotNull
    private LocalDateTime registered = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

    public String toString() {
        return "Vote:" + id;
    }
}
