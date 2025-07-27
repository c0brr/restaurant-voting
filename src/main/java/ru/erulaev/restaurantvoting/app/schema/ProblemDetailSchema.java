package ru.erulaev.restaurantvoting.app.schema;

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
