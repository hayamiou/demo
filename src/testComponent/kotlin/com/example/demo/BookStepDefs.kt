package com.example.demo

import io.cucumber.java.Before
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class BookStepDefs(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) {

    @LocalServerPort
    private var port: Int = 0

    private var lastResult: io.restassured.response.Response? = null

    @Before
    fun setup() {
        RestAssured.baseURI = "http://localhost:$port"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
        namedParameterJdbcTemplate.update("DELETE FROM book", MapSqlParameterSource())
    }

    @Given("the user creates the book with title {string} and author {string}")
    fun createBook(title: String, author: String) {
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body("""{"title": "$title", "author": "$author"}""")
            .`when`()
            .post("/books")
            .then()
            .statusCode(201)
    }

    @And("the user creates the book with title {string} and author {string}")
    fun createBookAnd(title: String, author: String) {
        createBook(title, author)
    }

    @When("the user gets all books")
    fun getAllBooks() {
        lastResult = RestAssured.given()
            .`when`()
            .get("/books")
            .then()
            .statusCode(200)
            .extract()
            .response()
    }

    @Then("the list should contain {int} books")
    fun listShouldContain(count: Int) {
        lastResult!!.jsonPath().getList<Any>("$").size shouldBe count
    }
}