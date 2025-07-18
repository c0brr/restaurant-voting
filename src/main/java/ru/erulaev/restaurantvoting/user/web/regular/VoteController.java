package ru.erulaev.restaurantvoting.user.web.regular;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.erulaev.restaurantvoting.app.AuthUser;
import ru.erulaev.restaurantvoting.user.service.VoteService;
import ru.erulaev.restaurantvoting.user.to.vote.ResponseVoteTo;

import java.util.List;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class VoteController {

    static final String REST_URL = "/api/my-votes";

    private final VoteService voteService;

    @GetMapping
    public List<ResponseVoteTo> getAll(@AuthenticationPrincipal AuthUser authUser) {
        log.info("getAll from user {}", authUser);
        return voteService.getAllByUser(authUser.id());
    }
}
