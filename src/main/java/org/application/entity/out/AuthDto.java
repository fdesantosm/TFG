package org.application.entity.out;

import lombok.Data;

@Data
public class AuthDto {
    private String accessToken;
    private String tokenType = "Bearer ";

    public AuthDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
