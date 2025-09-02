package ru.erulaev.restaurantvoting.user.web.swagger.schema;

import lombok.Getter;

import java.util.Map;

@Getter
public abstract class BindExceptionProblemDetailSchema extends ProblemDetailSchema {

    private Map<String, String> invalid_params;
}
