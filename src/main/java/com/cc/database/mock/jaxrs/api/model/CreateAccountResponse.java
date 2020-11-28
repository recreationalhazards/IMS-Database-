package com.cc.database.mock.jaxrs.api.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccountResponse {
    @Mask
    private String accessToken;
    @Mask
    private String accessTokenSecret;
    private Integer timeToLiveSeconds;
    private String accessTokenGuid;

}
