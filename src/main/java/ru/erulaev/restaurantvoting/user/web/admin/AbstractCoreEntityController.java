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
import ru.erulaev.restaurantvoting.user.web.swagger.CommonAdminApiResponses;

import java.net.URI;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.erulaev.restaurantvoting.common.validation.ValidationUtil.assureIdConsistent;
import static ru.erulaev.restaurantvoting.common.validation.ValidationUtil.checkNew;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@CommonAdminApiResponses
public abstract class AbstractCoreEntityController<E extends HasId, R extends CoreEntityBaseRepository<E>> {

    protected final Logger log = getLogger(getClass());

    protected final R repository;

    protected List<E> getAll() {
        log.info("getAll");
        return repository.getAll();
    }

    protected E get(int id) {
        log.info("get {}", id);
        return repository.getExisted(id);
    }

    protected ResponseEntity<E> createWithLocation(E entity, String url) {
        log.info("create {}", entity);
        checkNew(entity);
        entity = repository.prepareAndSave(entity);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(url + "/{id}")
                .buildAndExpand(entity.id()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(entity);
    }

    protected void delete(int id) {
        log.info("delete {}", id);
        repository.deleteExisted(id);
    }

    @Transactional
    protected void doUpdate(E entity, int id) {
        log.info("update {} with id={}", entity, id);
        assureIdConsistent(entity, id);
        if (!repository.existsById(id)) {
            throw new NotFoundException("Entity with id=" + id + " not found");
        }
        repository.prepareAndSave(entity);
    }

    protected List<E> getByContainingName(String name, Sort sort) {
        log.info("getByContainingName {}", name);
        return repository.findByNameContainingIgnoreCase(name, sort);
    }
}