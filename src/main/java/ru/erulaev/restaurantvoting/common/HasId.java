package ru.erulaev.restaurantvoting.common;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.util.Assert;

public interface HasId {

    Long getId();

    void setId(Long id);

    @Schema(hidden = true)
    default boolean isNew() {
        return getId() == null;
    }

    default long id() {
        Assert.notNull(getId(), "Entity must has id");
        return getId();
    }
}