package api

import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.builder.ResponseSpecBuilder
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import io.restassured.specification.ResponseSpecification

object Specification {
    fun requestSpec(url: String): RequestSpecification {
        return RequestSpecBuilder()
            .setBaseUri(url)
            .setContentType(ContentType.JSON)
            .build()
    }

    fun responseStatusCodeSpec(statusCode: Int): ResponseSpecification {
        return ResponseSpecBuilder()
            .expectStatusCode(statusCode)
            .build()
    }

    fun installSpecification(requestSpec: RequestSpecification, responseSpec: ResponseSpecification) {
        RestAssured.requestSpecification = requestSpec
        RestAssured.responseSpecification = responseSpec
    }
}