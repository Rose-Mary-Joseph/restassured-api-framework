package com.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response payload from POST /api/login.
 *
 * Successful login returns: {"token": "QpwL5tke4Pnpja7X4"}
 * Failed login returns:     {"error": "Missing password"}
 *
 * Both fields are present here so this model handles both outcomes.
 * In practice, only one of token/error will be populated per response.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponse {

    /** Authentication token returned on successful login. */
    @JsonProperty("token")
    private String token;

    /** Error message returned when login fails (missing email/password). */
    @JsonProperty("error")
    private String error;

    public LoginResponse() {
    }

    public String getToken()              { return token; }
    public void setToken(String token)    { this.token = token; }

    public String getError()              { return error; }
    public void setError(String error)    { this.error = error; }
}