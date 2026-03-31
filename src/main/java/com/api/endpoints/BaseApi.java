package com.api.endpoints;

import com.api.config.ConfigReader;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * Base configuration for all API calls.

 * WHAT IS RequestSpecification?
 * Think of it as a "template" for API requests. Instead of writing
 * the base URL, content type, and headers in every single test,
 * we define them ONCE here. Every request automatically gets these settings.
 * 
 * It's like setting your home address in Google Maps as default —
 * you don't type it every time, just the destination.
 */
public class BaseApi {

    protected static RequestSpecification requestSpec;

    static {
        // This block runs ONCE when the class is first loaded.
        // Sets up the default configuration for all API requests.

        ConfigReader config = ConfigReader.getInstance();

        RequestSpecBuilder builder = new RequestSpecBuilder()
                // Every request goes to this base URL
                // e.g., "https://reqres.in" + "/api/users" = full URL
                .setBaseUri(config.getBaseUrl())

                // We're sending JSON in request bodies
                .setContentType(ContentType.JSON)

                // We expect JSON back from the server
                .setAccept(ContentType.JSON)
                .addHeader("x-api-key", config.getApiKey())
                // Allure filter — automatically captures every request/response
                // in the report. You'll see the full URL, headers, body, status
                // code without writing any extra logging code.
                .addFilter(new AllureRestAssured());

        // Optional: log everything to console for debugging
        if (config.shouldLogAll()) {
            builder.log(io.restassured.filter.log.LogDetail.ALL);
        }

        requestSpec = builder.build();
    }

    /**
     * Returns a fresh request with all the base settings applied.
     * 
     * WHY given() every time?
     * RestAssured uses a builder pattern: given() → when() → then()
     * 
     * given() = setup (headers, body, params)
     * when()  = action (GET, POST, PUT, DELETE)
     * then()  = assert (status code, body content)
     * 
     * Example:
     *   getRequest()
     *     .when().get("/api/users/2")
     *     .then().statusCode(200);
     */
    protected static io.restassured.specification.RequestSpecification getRequest() {
        return RestAssured.given().spec(requestSpec);
    }
}