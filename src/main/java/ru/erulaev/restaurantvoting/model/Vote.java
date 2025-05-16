package ru.erulaev.restaurantvoting.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "vote")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Vote extends AbstractBaseEntity {

    @Column(name = "registered", nullable = false)
    @NotNull
    private LocalDateTime registered = LocalDateTime.now();

    @ManyToOne
    private User user;

    @ManyToOne
    private Restaurant restaurant;

    public Vote(Integer id) {
        super(id);
    }

    public String toString() {
        return "Vote:" + id;
    }
}
