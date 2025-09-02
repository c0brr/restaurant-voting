package ru.erulaev.restaurantvoting.user.web.swagger;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import ru.erulaev.restaurantvoting.user.web.swagger.schema.BindExceptionProblemDetailSchema;
import ru.erulaev.restaurantvoting.user.web.swagger.schema.ProblemDetailSchema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(responseCode = "400", description = "Request body has errors or missing",
        content = @Content(schema = @Schema(implementation = ProblemDetailSchema.class)))
@ApiResponse(responseCode = "422", description = "Input data is not valid",
        content = @Content(schema = @Schema(implementation = BindExceptionProblemDetailSchema.class)))
public @interface BodyAndDataApiResponses {}
