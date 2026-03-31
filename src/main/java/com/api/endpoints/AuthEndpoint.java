package com.api.endpoints;

import com.api.models.LoginRequest;

import io.qameta.allure.Step;
import io.restassured.response.Response;

/**
 * Encapsulates authentication-related API operations.
 *
 * Separated from UserEndpoint because auth is a different domain —
 * it deals with credentials and tokens, not user CRUD.
 * In a real application, auth endpoints often have different
 * rate limits, headers, and error handling.
 */
public class AuthEndpoint extends BaseApi {

    private static final String LOGIN_PATH = "/api/login";
    private static final String REGISTER_PATH = "/api/register";

    /**
     * Authenticates a user and returns a token.
     * POST /api/login
     *
     * Success: {"token": "QpwL5tke4Pnpja7X4"} with status 200
     * Failure: {"error": "Missing password"} with status 400
     *
     * @param loginRequest object containing email and password
     * @return raw Response for assertion in tests
     */
    @Step("POST login — email: {loginRequest.email}")
    public static Response login(LoginRequest loginRequest) {
        return getRequest()
                .body(loginRequest)
                .when()
                .post(LOGIN_PATH);
    }

    /**
     * Registers a new user account.
     * POST /api/register
     *
     * Uses the same request payload as login.
     * Success returns: {"id": 4, "token": "QpwL5tke4Pnpja7X4"}
     *
     * @param registerRequest object containing email and password
     * @return raw Response for assertion in tests
     */
    @Step("POST register — email: {registerRequest.email}")
    public static Response register(LoginRequest registerRequest) {
        return getRequest()
                .body(registerRequest)
                .when()
                .post(REGISTER_PATH);
    }
}