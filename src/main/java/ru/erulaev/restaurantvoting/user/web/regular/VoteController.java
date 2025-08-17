package ru.erulaev.restaurantvoting.user.web.regular;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.erulaev.restaurantvoting.app.AuthUser;
import ru.erulaev.restaurantvoting.user.service.VoteService;
import ru.erulaev.restaurantvoting.user.to.vote.ResponseVoteToWithRestaurantName;
import ru.erulaev.restaurantvoting.user.web.response.CommonRegularApiResponses;
import ru.erulaev.restaurantvoting.user.web.response.schema.ProblemDetailSchema;

import java.util.List;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
@Tag(name = "Vote controller", description = "Getting all user's votes")
@ApiResponses(@ApiResponse(responseCode = "403", description = "USER role required for this request",
        content = @Content(schema = @Schema(implementation = ProblemDetailSchema.class))))
@CommonRegularApiResponses
public class VoteController {

    static final String REST_URL = "/api/my-votes";

    private final VoteService voteService;

    @GetMapping
    @Operation(summary = "To get all votes by user",
            description = "Returns votes' data (vote's ID, voting date, user's ID, restaurant's name) by authenticated user " +
                    "except votes for deleted restaurants, order by date desc.")
    @ApiResponse(responseCode = "200", description = "Request successful")
    public List<ResponseVoteToWithRestaurantName> getAll(@AuthenticationPrincipal AuthUser authUser) {
        log.info("getAll by user {}", authUser);
        return voteService.getAllByUser(authUser.id());
    }
}
