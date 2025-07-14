package ru.erulaev.restaurantvoting.user.web.admin;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.erulaev.restaurantvoting.common.HasId;
import ru.erulaev.restaurantvoting.user.service.FoodService;

import java.net.URI;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.erulaev.restaurantvoting.common.validation.ValidationUtil.checkNew;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractFoodController<Entity extends HasId, To extends HasId> {

    protected final Logger log = getLogger(getClass());

    protected final FoodService<Entity, To> service;

    protected List<To> getAll(long parentId) {
        log.info("getAll for parent entity {}", parentId);
        return service.getAll(parentId);
    }

    protected To get(long id, long parentId) {
        log.info("get {} from parent entity {}", id, parentId);
        return service.get(id, parentId);
    }

    protected ResponseEntity<To> createWithLocation(Entity entity, long parentId, String url) {
        log.info("create {} for parent entity {}", entity, parentId);
        checkNew(entity);
        To entityTo = service.save(entity, parentId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(url + "/{id}")
                .buildAndExpand(parentId, entityTo.id()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(entityTo);
    }

    protected void delete(long id, long parentId) {
        log.info("delete {} from parent entity {}", id, parentId);
        service.delete(id, parentId);
    }
}
