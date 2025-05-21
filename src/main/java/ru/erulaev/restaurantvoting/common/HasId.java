package ru.erulaev.restaurantvoting.common;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.util.Assert;

public interface HasId {

    Integer getId();

    void setId(Integer id);

    @Schema(hidden = true)
    default boolean isNew() {
        return getId() == null;
    }

    default int id() {
        Assert.notNull(getId(), "Entity must has id");
        return getId();
    }
}