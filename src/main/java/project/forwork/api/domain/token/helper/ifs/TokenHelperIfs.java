package project.forwork.api.domain.token.helper.ifs;

import project.forwork.api.domain.token.model.Token;

public interface TokenHelperIfs {
    Token issueAccessToken(Long userId);
    Token issueRefreshToken(Long userId);
    Long validationTokenWithThrow(String token);
}
