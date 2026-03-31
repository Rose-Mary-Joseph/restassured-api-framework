package com.api.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.api.endpoints.AuthEndpoint;
import com.api.models.LoginRequest;
import com.api.models.LoginResponse;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;

/**
 * Test suite for authentication endpoints (login and register).
 *
 * Demonstrates:
 *   - Positive and negative test cases for auth flows
 *   - Deserializing responses into Java objects using .as()
 *   - Testing error messages for invalid requests
 *
 * IMPORTANT: Reqres only accepts pre-defined emails for login/register.
 * Using any other email returns a 400 error. This is by design —
 * it's a fake API for testing, not a real auth system.
 */
@Feature("Authentication API")
public class AuthTests {

    // ==================== LOGIN TESTS ====================

    @Test(description = "Verify successful login returns a token")
    @Story("Login")
    @Severity(SeverityLevel.BLOCKER)
    public void testSuccessfulLogin() {
        LoginRequest request = new LoginRequest("eve.holt@reqres.in", "cityslicka");

        Response response = AuthEndpoint.login(request);
        response.then().statusCode(200);

        // Deserialize JSON response directly into a Java object.
        // {"token": "QpwL5tke4Pnpja7X4"} → LoginResponse with getToken() = "QpwL5tke4Pnpja7X4"
        // This is cleaner than jsonPath().getString("token") when you have a model.
        LoginResponse loginResponse = response.as(LoginResponse.class);

        Assert.assertNotNull(loginResponse.getToken(),
                "Token should not be null on successful login");
        Assert.assertFalse(loginResponse.getToken().isEmpty(),
                "Token should not be empty");
    }

    @Test(description = "Verify login without password returns 400 with error message")
    @Story("Login")
    @Severity(SeverityLevel.CRITICAL)
    public void testLoginWithoutPassword() {
        // Sending only email, no password
        LoginRequest request = new LoginRequest("eve.holt@reqres.in", null);

        Response response = AuthEndpoint.login(request);
        response.then().statusCode(400);

        LoginResponse loginResponse = response.as(LoginResponse.class);

        Assert.assertEquals(loginResponse.getError(), "Missing password",
                "Error message mismatch");
        Assert.assertNull(loginResponse.getToken(),
                "Token should be null on failed login");
    }

    @Test(description = "Verify login without email returns 400 with error message")
    @Story("Login")
    @Severity(SeverityLevel.CRITICAL)
    public void testLoginWithoutEmail() {
        LoginRequest request = new LoginRequest(null, "cityslicka");

        Response response = AuthEndpoint.login(request);
        response.then().statusCode(400);

        LoginResponse loginResponse = response.as(LoginResponse.class);

        Assert.assertEquals(loginResponse.getError(), "Missing email or username",
                "Error message mismatch");
    }

    // ==================== REGISTER TESTS ====================

    @Test(description = "Verify successful registration returns id and token")
    @Story("Register")
    @Severity(SeverityLevel.BLOCKER)
    public void testSuccessfulRegister() {
        LoginRequest request = new LoginRequest("eve.holt@reqres.in", "pistol");

        Response response = AuthEndpoint.register(request);
        response.then().statusCode(200);

        // Using jsonPath here to show the alternative to .as()
        int id = response.jsonPath().getInt("id");
        String token = response.jsonPath().getString("token");

        Assert.assertTrue(id > 0, "ID should be a positive integer");
        Assert.assertNotNull(token, "Token should not be null");
    }

    @Test(description = "Verify registration without password returns 400")
    @Story("Register")
    @Severity(SeverityLevel.CRITICAL)
    public void testRegisterWithoutPassword() {
        LoginRequest request = new LoginRequest("eve.holt@reqres.in", null);

        Response response = AuthEndpoint.register(request);
        response.then().statusCode(400);

        String error = response.jsonPath().getString("error");
        Assert.assertEquals(error, "Missing password");
    }
}