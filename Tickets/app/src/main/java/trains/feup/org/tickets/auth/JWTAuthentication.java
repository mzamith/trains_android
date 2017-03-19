package trains.feup.org.tickets.auth;

import java.util.List;
import java.util.Map;

import trains.feup.org.tickets.model.Pair;

/**
 * Created by mzamith on 10/03/17.
 */

public class JWTAuthentication implements Authentication {

    private static final String AUTH_HEADER = "Authorization";
    private String token;

    public JWTAuthentication(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public void applyToParams(List<Pair> queryParams, Map<String, String> headerParams) {

        if (token == null) return;

        headerParams.put(AUTH_HEADER, token);

    }
}
