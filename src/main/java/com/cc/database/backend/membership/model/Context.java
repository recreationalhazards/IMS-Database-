package com.cc.database.backend.membership.model;

import lombok.Getter;

import java.io.Serializable;
@Getter
public class Context implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String value;

}
