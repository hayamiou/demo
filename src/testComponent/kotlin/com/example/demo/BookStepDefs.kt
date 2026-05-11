package com.example.demo

import io.cucumber.java.Before
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.response.Response
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class BookStepDefs(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) {

    @LocalServerPort
    private var port: Int = 0

    private var lastResult: Response? = null
    private var lastReservationResult: Response? = null

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

    @And("the user reserves the first book")
    fun reserveFirstBook() {
        val id = lastResult!!.jsonPath().getLong("[0].id")
        lastReservationResult = RestAssured.given()
            .`when`()
            .patch("/books/$id/reserve")
            .then()
            .extract()
            .response()
    }

    @Then("the first book should be reserved")
    fun firstBookShouldBeReserved() {
        val id = lastResult!!.jsonPath().getLong("[0].id")
        val book = RestAssured.given()
            .`when`()
            .get("/books")
            .then()
            .statusCode(200)
            .extract()
            .response()
        book.jsonPath().getList<Map<String, Any>>("$")
            .first { it["id"].toString() == id.toString() }["reserved"] shouldBe true
    }

    @And("the user tries to reserve the first book again")
    fun tryReserveFirstBookAgain() {
        val id = lastResult!!.jsonPath().getLong("[0].id")
        lastReservationResult = RestAssured.given()
            .`when`()
            .patch("/books/$id/reserve")
            .then()
            .extract()
            .response()
    }

    @Then("the reservation should fail with status 400")
    fun reservationShouldFail() {
        lastReservationResult!!.statusCode shouldBe 400
    }
}