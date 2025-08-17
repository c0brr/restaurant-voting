package ru.erulaev.restaurantvoting.user.web.response;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import ru.erulaev.restaurantvoting.user.web.response.schema.ProblemDetailSchema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(responseCode = "200", description = "Requested entity is found")
@ApiResponse(responseCode = "404", description = "Requested entity is not found",
        content = @Content(schema = @Schema(implementation = ProblemDetailSchema.class)))
public @interface SearchResultApiResponses {}
