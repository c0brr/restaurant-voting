package ru.erulaev.restaurantvoting.user.web.admin;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.erulaev.restaurantvoting.common.HasId;
import ru.erulaev.restaurantvoting.common.error.NotFoundException;
import ru.erulaev.restaurantvoting.common.repository.CoreEntityBaseRepository;
import ru.erulaev.restaurantvoting.user.web.apiResponse.CommonAdminApiResponses;

import java.net.URI;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.erulaev.restaurantvoting.common.validation.ValidationUtil.assureIdConsistent;
import static ru.erulaev.restaurantvoting.common.validation.ValidationUtil.checkNew;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@CommonAdminApiResponses
public abstract class AbstractCoreEntityController<Entity extends HasId,
        Repository extends CoreEntityBaseRepository<Entity>> {

    protected final Logger log = getLogger(getClass());

    protected final Repository repository;

    protected List<Entity> getAll() {
        log.info("getAll");
        return repository.getAll();
    }

    protected Entity get(long id) {
        log.info("get {}", id);
        return repository.getExisted(id);
    }

    protected ResponseEntity<Entity> createWithLocation(Entity entity, String url) {
        log.info("create {}", entity);
        checkNew(entity);
        entity = repository.prepareAndSave(entity);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(url + "/{id}")
                .buildAndExpand(entity.id()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(entity);
    }

    protected void delete(long id) {
        log.info("delete {}", id);
        repository.deleteExisted(id);
    }

    @Transactional
    protected void doUpdate(Entity entity, long id) {
        log.info("update {} with id={}", entity, id);
        assureIdConsistent(entity, id);
        if (!repository.existsById(id)) {
            throw new NotFoundException("Entity with id=" + id + " not found");
        }
        repository.prepareAndSave(entity);
    }

    protected List<Entity> getByContainingName(String name, Sort sort) {
        log.info("getByContainingName {}", name);
        return repository.findByNameContainingIgnoreCase(name, sort);
    }
}