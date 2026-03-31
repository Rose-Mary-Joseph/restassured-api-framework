package com.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a User entity in the Reqres API.
 *
 * This POJO (Plain Old Java Object) serves as the data model for both
 * serialization (Java → JSON for requests) and deserialization
 * (JSON → Java for responses).
 *
 * Jackson handles the conversion automatically:
 *   - Sending a request: new User("John", "Engineer") → {"name":"John","job":"Engineer"}
 *   - Reading a response: {"id":1,"name":"John"} → User object with getId() = 1
 *
 * WHY use POJOs instead of raw JSON strings?
 *   1. Type safety — compiler catches typos like user.getNmae() 
 *   2. IDE autocomplete — no guessing field names
 *   3. Reusability — same model works for create, update, and assertions
 *   4. Readability — user.getName() is clearer than jsonPath.get("data.name")
 *
 * @JsonIgnoreProperties(ignoreUnknown = true)
 *   Tells Jackson: "If the API returns extra fields not in this class, don't crash."
 *   APIs evolve over time — new fields get added. Without this annotation,
 *   deserialization fails on any unexpected field.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    /** Server-generated unique identifier. Present in responses only. */
    @JsonProperty("id")
    private int id;

    /** User's full name. Used in both create and update requests. */
    @JsonProperty("name")
    private String name;

    /** User's job title. Used in both create and update requests. */
    @JsonProperty("job")
    private String job;

    /** Timestamp when the user was created. Returned by POST /api/users. */
    @JsonProperty("createdAt")
    private String createdAt;

    /** Timestamp when the user was last updated. Returned by PUT /api/users/{id}. */
    @JsonProperty("updatedAt")
    private String updatedAt;

    /**
     * Default no-arg constructor.
     * Required by Jackson for deserialization — it creates an empty object first,
     * then fills in the fields from JSON.
     */
    public User() {
    }

    /**
     * Convenience constructor for creating request payloads.
     * Usage: new User("Rose", "QA Automation Engineer")
     *
     * @param name the user's full name
     * @param job  the user's job title
     */
    public User(String name, String job) {
        this.name = name;
        this.job = job;
    }

    // ==================== GETTERS & SETTERS ====================
    // Jackson needs these to read/write field values during conversion.

    public int getId()              { return id; }
    public void setId(int id)      { this.id = id; }

    public String getName()             { return name; }
    public void setName(String name)    { this.name = name; }

    public String getJob()            { return job; }
    public void setJob(String job)    { this.job = job; }

    public String getCreatedAt()                { return createdAt; }
    public void setCreatedAt(String createdAt)  { this.createdAt = createdAt; }

    public String getUpdatedAt()                { return updatedAt; }
    public void setUpdatedAt(String updatedAt)  { this.updatedAt = updatedAt; }

    /**
     * String representation for logging and debugging.
     * When you print a User object, you'll see its field values
     * instead of a cryptic memory address like "User@3f2a1c".
     */
    @Override
    public String toString() {
        return "User{name='" + name + "', job='" + job + "', id=" + id + "}";
    }
}