package ru.erulaev.restaurantvoting.user.web.regular;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import ru.erulaev.restaurantvoting.app.schema.ProblemDetailSchema;
import ru.erulaev.restaurantvoting.user.service.VoteService;
import ru.erulaev.restaurantvoting.user.to.vote.RequestVoteTo;
import ru.erulaev.restaurantvoting.user.to.vote.ResponseVoteTo;
import ru.erulaev.restaurantvoting.app.apiResponse.BodyAndDataApiResponses;
import ru.erulaev.restaurantvoting.app.apiResponse.CommonRegularApiResponses;
import ru.erulaev.restaurantvoting.app.apiResponse.SearchResultApiResponses;
import ru.erulaev.restaurantvoting.user.web.validation.UniqueUserVoteValidator;

import java.net.URI;

import static ru.erulaev.restaurantvoting.common.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = CurrentVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
@Tag(name = "Current vote controller", description = "Management for user's vote by current date")
@ApiResponses(@ApiResponse(responseCode = "403", description = "USER role required for this request",
        content = @Content(schema = @Schema(implementation = ProblemDetailSchema.class))))
@CommonRegularApiResponses
public class CurrentVoteController {

    static final String REST_URL = "/api/current-vote";

    private final VoteService voteService;
    private final UniqueUserVoteValidator userVoteValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(userVoteValidator);
    }

    @GetMapping
    @Operation(summary = "To get vote",
            description = "Returns user current vote's data (vote's ID, voting date, user's ID, restaurant's ID) by his authentication")
    @SearchResultApiResponses
    public ResponseEntity<ResponseVoteTo> get(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get for user {} for today", authUser);
        return ResponseEntity.of(voteService.getCurrent(authUser.id()));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "To create vote", description = "Creates a new vote")
    @ApiResponse(responseCode = "201", description = "Vote is created")
    @ApiResponse(responseCode = "404", description = "Restaurant for voting is not found",
            content = @Content(schema = @Schema(implementation = ProblemDetailSchema.class)))
    @BodyAndDataApiResponses
    public ResponseEntity<ResponseVoteTo> createWithLocation(@Parameter(description = "Vote's data (restaurant's ID)")
                                                             @Valid @RequestBody RequestVoteTo requestVoteTo,
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
    @Operation(summary = "To delete vote", description = "Deletes user's vote by his authentication")
    @ApiResponse(responseCode = "204", description = "Vote is deleted")
    @ApiResponse(responseCode = "404", description = "Vote is not found",
            content = @Content(schema = @Schema(implementation = ProblemDetailSchema.class)))
    @ApiResponse(responseCode = "422", description = "Voting deadline violation",
            content = @Content(schema = @Schema(implementation = ProblemDetailSchema.class)))
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        log.info("delete current from user {}", authUser);
        voteService.deleteCurrent(authUser.id());
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "To change choice", description = "Changes restaurant's ID at user's vote by his authentication")
    @ApiResponse(responseCode = "204", description = "Choice has been changed")
    @ApiResponse(responseCode = "404", description = "Vote or restaurant for voting is not found",
            content = @Content(schema = @Schema(implementation = ProblemDetailSchema.class)))
    @ApiResponse(responseCode = "422", description = "Voting deadline violation",
            content = @Content(schema = @Schema(implementation = ProblemDetailSchema.class)))
    public void patch(@Parameter(description = "Restaurant's ID") @RequestParam long restaurantId,
                      @AuthenticationPrincipal AuthUser authUser) {
        log.info("change choice for user {}", authUser);
        voteService.changeChoice(restaurantId, authUser.id());
    }
}