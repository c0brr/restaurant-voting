package ru.erulaev.restaurantvoting.user.web.admin;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.erulaev.restaurantvoting.common.HasId;
import ru.erulaev.restaurantvoting.user.service.FoodService;
import ru.erulaev.restaurantvoting.user.web.swagger.CommonAdminApiResponses;

import java.net.URI;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.erulaev.restaurantvoting.common.validation.ValidationUtil.checkNew;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@CommonAdminApiResponses
public abstract class AbstractFoodController<E extends HasId, T extends HasId> {

    protected final Logger log = getLogger(getClass());

    protected final FoodService<E, T> service;

    protected List<T> getAll(int parentId) {
        log.info("getAll for parent entity {}", parentId);
        return service.getAll(parentId);
    }

    protected T get(int id, int parentId) {
        log.info("get {} from parent entity {}", id, parentId);
        return service.get(id, parentId);
    }

    protected ResponseEntity<T> createWithLocation(E entity, int parentId, String url) {
        log.info("create {} for parent entity {}", entity, parentId);
        checkNew(entity);
        T entityTo = service.save(entity, parentId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(url + "/{id}")
                .buildAndExpand(parentId, entityTo.id()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(entityTo);
    }

    protected void delete(int id, int parentId) {
        log.info("delete {} from parent entity {}", id, parentId);
        service.delete(id, parentId);
    }
}
