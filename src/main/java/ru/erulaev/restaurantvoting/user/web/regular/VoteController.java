package ru.erulaev.restaurantvoting.user.web.regular;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.erulaev.restaurantvoting.app.AuthUser;
import ru.erulaev.restaurantvoting.user.service.VoteService;
import ru.erulaev.restaurantvoting.user.to.RequestVoteTo;
import ru.erulaev.restaurantvoting.user.to.ResponseVoteTo;

import java.net.URI;

import static ru.erulaev.restaurantvoting.common.validation.ValidationUtil.assureIdConsistent;
import static ru.erulaev.restaurantvoting.common.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class VoteController {

    static final String REST_URL = "/api/votes";

    private final VoteService voteService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseVoteTo> createWithLocation(@Valid @RequestBody RequestVoteTo requestVoteTo,
                                                             @AuthenticationPrincipal AuthUser authUser) {
        log.info("create {} from user {}", requestVoteTo, authUser);
        checkNew(requestVoteTo);
        ResponseVoteTo responseVoteTo = voteService.save(requestVoteTo, authUser.getUser());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(responseVoteTo.id()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(responseVoteTo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id, @AuthenticationPrincipal AuthUser authUser) {
        log.info("delete {} from user {}", id, authUser);
        voteService.delete(id, authUser.getUser());
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody RequestVoteTo requestVoteTo,
                       @PathVariable long id,
                       @AuthenticationPrincipal AuthUser authUser) {
        log.info("update {} with id={} for user {}", requestVoteTo, id, authUser);
        assureIdConsistent(requestVoteTo, id);
        voteService.update(requestVoteTo, authUser.getUser());
    }
}