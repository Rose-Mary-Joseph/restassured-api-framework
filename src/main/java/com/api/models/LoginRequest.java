package com.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request payload for POST /api/login and POST /api/register.
 *
 * Separated from User because login/register use different fields
 * (email + password) than user CRUD operations (name + job).
 * Keeping them as distinct models prevents confusion and
 * makes the API contract explicit in code.
 */
public class LoginRequest {

    /** User's email address. Required for both login and register. */
    @JsonProperty("email")
    private String email;

    /** User's password. Required for both login and register. */
    @JsonProperty("password")
    private String password;

    /** Default constructor for Jackson deserialization. */
    public LoginRequest() {
    }

    /**
     * Convenience constructor for building request payloads inline.
     * Usage: new LoginRequest("eve.holt@reqres.in", "cityslicka")
     *
     * @param email    the user's email
     * @param password the user's password
     */
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail()              { return email; }
    public void setEmail(String email)    { this.email = email; }

    public String getPassword()                 { return password; }
    public void setPassword(String password)    { this.password = password; }
}