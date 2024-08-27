package project.forwork.api.domain.token.helper.ifs;

import project.forwork.api.domain.token.model.Token;
import project.forwork.api.domain.user.model.User;

public interface TokenHelperIfs {
    Token issueAccessToken(Long userId);
    Token issueRefreshToken(Long userId);
    Long validationTokenWithThrow(String token);
}
