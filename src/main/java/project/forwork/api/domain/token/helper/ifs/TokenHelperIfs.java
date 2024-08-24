package project.forwork.api.domain.token.helper.ifs;

import project.forwork.api.domain.token.model.Token;
import project.forwork.api.domain.user.model.User;

public interface TokenHelperIfs {
    Token issueAccessToken(User user);
    Token issueCsrfToken(User user);
    Token issueRefreshToken(User user);
    Long validationTokenWithThrow(String token);
}
