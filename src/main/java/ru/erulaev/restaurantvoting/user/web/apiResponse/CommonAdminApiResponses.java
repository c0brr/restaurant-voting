package ru.erulaev.restaurantvoting.user.web.apiResponse;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import ru.erulaev.restaurantvoting.user.web.apiResponse.schema.ProblemDetailSchema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({@ApiResponse(responseCode = "401", description = "Authorization required for this request",
        content = @Content(schema = @Schema(implementation = ProblemDetailSchema.class))),
        @ApiResponse(responseCode = "403", description = "ADMIN role required for this request",
                content = @Content(schema = @Schema(implementation = ProblemDetailSchema.class))),
        @ApiResponse(responseCode = "500", description = "Iternal Server Error",
                content = @Content(schema = @Schema(implementation = ProblemDetailSchema.class)))})
public @interface CommonAdminApiResponses {}