package com.api.endpoints;

import com.api.models.User;

import io.qameta.allure.Step;
import io.restassured.response.Response;

/**
 * Encapsulates all HTTP operations for the /api/users resource.
 *
 * This is the API equivalent of a Page Object from Project 1.
 * Instead of page elements and click actions, we have endpoints and HTTP methods.
 *
 * WHY wrap API calls in a class?
 *   - Tests call userEndpoint.createUser(user) instead of writing raw RestAssured chains
 *   - If the endpoint URL changes from /api/users to /api/v2/users,
 *     you update ONE constant, not 30 test methods
 *   - Request building logic (headers, auth tokens) stays here, tests stay clean
 *
 * Each method returns a raw Response object so the TEST decides what to assert.
 * This keeps the endpoint class flexible — same createUser() works for
 * testing success (201), validation errors (400), and auth failures (401).
 */
public class UserEndpoint extends BaseApi {

    /** Base path for user operations. Single source of truth. */
    private static final String USERS_PATH = "/api/users";

    /** Path template for single user operations. %d is replaced with the user ID. */
    private static final String USER_BY_ID_PATH = "/api/users/%d";

    // ==================== GET ====================

    /**
     * Retrieves a paginated list of users.
     * GET /api/users?page={pageNumber}
     *
     * @param pageNumber the page to retrieve (1-based)
     * @return raw Response for assertion in tests
     */
    @Step("GET list of users — page {pageNumber}")
    public static Response getUsers(int pageNumber) {
        return getRequest()
                .queryParam("page", pageNumber)
                .when()
                .get(USERS_PATH);
    }

    /**
     * Retrieves a single user by their ID.
     * GET /api/users/{id}
     *
     * @param userId the unique identifier of the user
     * @return raw Response — will be 200 if found, 404 if not
     */
    @Step("GET single user — ID: {userId}")
    public static Response getUserById(int userId) {
        return getRequest()
                .when()
                .get(String.format(USER_BY_ID_PATH, userId));
    }

    // ==================== POST ====================

    /**
     * Creates a new user.
     * POST /api/users
     *
     * Jackson automatically converts the User object to JSON:
     *   new User("Rose", "QA Engineer") → {"name":"Rose","job":"QA Engineer"}
     *
     * The .body(user) call triggers this serialization.
     *
     * @param user the User object containing name and job
     * @return raw Response — 201 on success with id and createdAt
     */
    @Step("POST create new user — {user}")
    public static Response createUser(User user) {
        return getRequest()
                .body(user)
                .when()
                .post(USERS_PATH);
    }

    // ==================== PUT ====================

    /**
     * Performs a full update of an existing user.
     * PUT /api/users/{id}
     *
     * PUT means "replace the entire resource" — you send ALL fields,
     * even the ones that haven't changed. Compare with PATCH which
     * sends only the changed fields.
     *
     * @param userId the ID of the user to update
     * @param user   the updated User object
     * @return raw Response — 200 on success with updatedAt timestamp
     */
    @Step("PUT update user — ID: {userId}")
    public static Response updateUser(int userId, User user) {
        return getRequest()
                .body(user)
                .when()
                .put(String.format(USER_BY_ID_PATH, userId));
    }

    // ==================== PATCH ====================

    /**
     * Performs a partial update of an existing user.
     * PATCH /api/users/{id}
     *
     * Unlike PUT, PATCH only updates the fields you send.
     * Useful when you want to change just the job title
     * without resending the name.
     *
     * @param userId the ID of the user to patch
     * @param user   the User object with only the fields to update
     * @return raw Response — 200 on success with updatedAt timestamp
     */
    @Step("PATCH partial update user — ID: {userId}")
    public static Response patchUser(int userId, User user) {
        return getRequest()
                .body(user)
                .when()
                .patch(String.format(USER_BY_ID_PATH, userId));
    }

    // ==================== DELETE ====================

    /**
     * Deletes a user by their ID.
     * DELETE /api/users/{id}
     *
     * Successful deletion returns 204 (No Content) — meaning
     * "the request succeeded but there's nothing to send back."
     *
     * @param userId the ID of the user to delete
     * @return raw Response — 204 on success, empty body
     */
    @Step("DELETE user — ID: {userId}")
    public static Response deleteUser(int userId) {
        return getRequest()
                .when()
                .delete(String.format(USER_BY_ID_PATH, userId));
    }
}