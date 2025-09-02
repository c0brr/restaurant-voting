package ru.erulaev.restaurantvoting.user.web.swagger.schema;

import lombok.Getter;

import java.net.URI;

@Getter
public abstract class ProblemDetailSchema {

    protected URI type;

    protected String title;

    protected int status;

    protected String detail;

    protected URI instance;
}
