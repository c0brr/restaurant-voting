package ru.erulaev.restaurantvoting.user.web.regular;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.erulaev.restaurantvoting.app.AuthUser;
import ru.erulaev.restaurantvoting.user.service.VoteService;
import ru.erulaev.restaurantvoting.user.to.vote.RequestVoteTo;
import ru.erulaev.restaurantvoting.user.to.vote.ResponseVoteTo;
import ru.erulaev.restaurantvoting.user.web.validation.UniqueUserVoteValidator;

import java.net.URI;

import static ru.erulaev.restaurantvoting.common.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = CurrentVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class CurrentVoteController {

    static final String REST_URL = "/api/current-vote";

    private final VoteService voteService;
    private final UniqueUserVoteValidator userVoteValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(userVoteValidator);
    }

    @GetMapping
    public ResponseEntity<ResponseVoteTo> get(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get for user {} for today", authUser);
        return ResponseEntity.of(voteService.getCurrent(authUser.id()));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseVoteTo> createWithLocation(@Valid @RequestBody RequestVoteTo requestVoteTo,
                                                             @AuthenticationPrincipal AuthUser authUser) {
        log.info("create {} from user {}", requestVoteTo, authUser);
        checkNew(requestVoteTo);
        ResponseVoteTo responseVoteTo = voteService.save(requestVoteTo, authUser.getUser());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(responseVoteTo);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        log.info("delete current from user {}", authUser);
        voteService.deleteCurrent(authUser.id());
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void patch(@RequestParam long restaurantId,
                      @AuthenticationPrincipal AuthUser authUser) {
        log.info("change choice for user {}", authUser);
        voteService.changeChoice(restaurantId, authUser.id());
    }
}