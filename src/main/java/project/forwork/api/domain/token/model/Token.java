package project.forwork.api.domain.token.model;

import lombok.Builder;
import lombok.Getter;


@Getter
public class Token {
    private final String token;
    private final long ttl;

    @Builder
    public Token(String token, long ttl) {
        this.token = token;
        this.ttl = ttl;
    }
}
