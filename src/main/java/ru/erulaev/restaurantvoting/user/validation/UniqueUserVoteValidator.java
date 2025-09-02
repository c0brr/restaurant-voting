package ru.erulaev.restaurantvoting.user.validation;

import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import ru.erulaev.restaurantvoting.app.AuthUtil;
import ru.erulaev.restaurantvoting.user.repository.VoteRepository;
import ru.erulaev.restaurantvoting.user.to.vote.RequestVoteTo;

@Component
@AllArgsConstructor
public class UniqueUserVoteValidator implements org.springframework.validation.Validator {

    public static final String EXCEPTION_DUPLICATE_VOTE = "You've already voted today";

    private final VoteRepository voteRepository;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return RequestVoteTo.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        RequestVoteTo vote = (RequestVoteTo) target;
        if (voteRepository.existsByUserIdAndCreationDate(AuthUtil.get().id(), vote.getCreationDate())) {
            errors.rejectValue("creationDate", "", EXCEPTION_DUPLICATE_VOTE);
        }
    }
}
