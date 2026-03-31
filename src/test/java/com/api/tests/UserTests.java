package com.api.tests;

import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.api.endpoints.UserEndpoint;
import com.api.models.User;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import io.restassured.response.Response;

/**
 * Test suite for the /api/users resource.
 * Covers all CRUD operations: Create, Read, Update, Delete.
 *
 * TWO WAYS TO ASSERT in RestAssured:
 *
 * 1. Hamcrest matchers (chained with .then().body()):
 *      response.then().body("data.first_name", equalTo("Janet"));
 *    Pros: Clean, readable, built into RestAssured
 *    Cons: Limited for complex logic
 *
 * 2. Extract + TestNG Assert:
 *      String name = response.jsonPath().getString("data.first_name");
 *      Assert.assertEquals(name, "Janet");
 *    Pros: Full control, better error messages
 *    Cons: More verbose
 *
 * This test class uses BOTH styles so you're comfortable with either.
 * In interviews, mention you know both and choose based on the situation.
 *
 * @Feature and @Story are Allure annotations that organize tests
 * into a hierarchy in the report. Feature → Story → Test.
 */
@Feature("User Management API")
public class UserTests {

    // ==================== GET TESTS ====================

    @Test(description = "Verify fetching a list of users returns paginated results")
    @Story("GET Users")
    @Severity(SeverityLevel.BLOCKER)
    public void testGetUsersList() {
        Response response = UserEndpoint.getUsers(1);

        // Hamcrest style — chained assertions, reads like English
        response.then()
                .statusCode(200)
                .body("page", equalTo(1))
                .body("per_page", greaterThan(0))
                .body("data", hasSize(greaterThan(0)))
                .body("data[0].email", notNullValue());
    }

    @Test(description = "Verify users list response matches the expected JSON schema")
    @Story("GET Users")
    @Severity(SeverityLevel.CRITICAL)
    public void testUsersListSchema() {
        Response response = UserEndpoint.getUsers(1);

        // Schema validation — checks EVERY field, type, and required property
        // in one line. The schema file does the heavy lifting.
        response.then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/users-list-schema.json"));
    }

    @Test(description = "Verify fetching a single user returns correct user data")
    @Story("GET Users")
    @Severity(SeverityLevel.BLOCKER)
    public void testGetSingleUser() {
        Response response = UserEndpoint.getUserById(2);

        // Extract + Assert style — useful when you need the value for logging
        // or further processing, not just assertion
        response.then().statusCode(200);

        String email = response.jsonPath().getString("data.email");
        String firstName = response.jsonPath().getString("data.first_name");
        int id = response.jsonPath().getInt("data.id");

        Assert.assertEquals(id, 2, "User ID mismatch");
        Assert.assertEquals(firstName, "Janet", "First name mismatch");
        Assert.assertTrue(email.contains("@"), "Email format is invalid");
    }

    @Test(description = "Verify single user response matches the expected JSON schema")
    @Story("GET Users")
    @Severity(SeverityLevel.CRITICAL)
    public void testSingleUserSchema() {
        Response response = UserEndpoint.getUserById(2);

        response.then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/single-user-schema.json"));
    }

    @Test(description = "Verify requesting a non-existent user returns 404")
    @Story("GET Users")
    @Severity(SeverityLevel.NORMAL)
    public void testGetUserNotFound() {
        // User ID 999 doesn't exist — API should return 404
        Response response = UserEndpoint.getUserById(999);

        response.then()
                .statusCode(404)
                .body("$", anEmptyMap());
        // "$" means the root of the JSON response
        // anEmptyMap() = the response body is {}
    }

    // ==================== POST TESTS ====================

    @Test(description = "Verify creating a new user returns 201 with correct data")
    @Story("POST Create User")
    @Severity(SeverityLevel.BLOCKER)
    public void testCreateUser() {
        User newUser = new User("Rose", "QA Automation Engineer");

        Response response = UserEndpoint.createUser(newUser);

        response.then()
                .statusCode(201)
                .body("name", equalTo("Rose"))
                .body("job", equalTo("QA Automation Engineer"))
                .body("id", notNullValue())
                .body("createdAt", notNullValue());
    }

    @Test(description = "Verify create user response matches the expected JSON schema")
    @Story("POST Create User")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreateUserSchema() {
        User newUser = new User("Edwin", "Senior Software Engineer");

        Response response = UserEndpoint.createUser(newUser);

        response.then()
                .statusCode(201)
                .body(matchesJsonSchemaInClasspath("schemas/create-user-schema.json"));
    }

    // ==================== PUT TESTS ====================

    @Test(description = "Verify full update of an existing user returns updated data")
    @Story("PUT Update User")
    @Severity(SeverityLevel.CRITICAL)
    public void testUpdateUser() {
        User updatedUser = new User("Rose", "Lead QA Engineer");

        Response response = UserEndpoint.updateUser(2, updatedUser);

        response.then()
                .statusCode(200)
                .body("name", equalTo("Rose"))
                .body("job", equalTo("Lead QA Engineer"))
                .body("updatedAt", notNullValue());
    }

    // ==================== PATCH TESTS ====================

    @Test(description = "Verify partial update changes only the specified field")
    @Story("PATCH Update User")
    @Severity(SeverityLevel.NORMAL)
    public void testPatchUser() {
        // Only updating the job — name is not sent
        User partialUpdate = new User();
        partialUpdate.setJob("SDET");

        Response response = UserEndpoint.patchUser(2, partialUpdate);

        response.then()
                .statusCode(200)
                .body("job", equalTo("SDET"))
                .body("updatedAt", notNullValue());
    }

    // ==================== DELETE TESTS ====================

    @Test(description = "Verify deleting a user returns 204 No Content")
    @Story("DELETE User")
    @Severity(SeverityLevel.CRITICAL)
    public void testDeleteUser() {
        Response response = UserEndpoint.deleteUser(2);

        // 204 = success but no response body
        response.then()
                .statusCode(204);

        // Verify the body is truly empty
        Assert.assertTrue(response.getBody().asString().isEmpty(),
                "Delete response body should be empty");
    }
}