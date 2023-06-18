import api.Specification
import api.resource.Resource
import api.user.UserData
import api.user.UserRegister
import api.user.UserRegisterError
import api.user.UserRegisterSuccess
import io.restassured.RestAssured.given
import jdk.jfr.Description
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Brief demonstration of work with Rest Assured
 * All the test data is taken from https://reqres.in, hopefully it'll explain hardcoding
 * */

class TestDemonstration {
    @Test
    @Description("Avatar link and user email.")
    fun checkAvatarAndDomainNameTest() {
        Specification.installSpecification(requestSpec = Specification.requestSpec(Constants.BASE_URL), responseSpec = Specification.responseStatusCodeSpec(200))

        val users = given()
            .`when`()
            .get("/api/users?page=2")
            .then()
            .extract().body().jsonPath().getList("data", UserData::class.java)

        users.forEach { user ->
            assertTrue(user.id.toString() in user.avatar, "User ${user.firstName}'s avatar URL ${user.avatar} should contain the user id ${user.id}.")
            val emailEnding = "@reqres.in"
            assertTrue(user.email.endsWith(emailEnding), "User email ${user.email} should ends with $emailEnding")
        }
    }

    @Test
    @Description("Response body for successful registration.")
    fun successfulRegistrationTest() {
        Specification.installSpecification(requestSpec = Specification.requestSpec(Constants.BASE_URL), responseSpec = Specification.responseStatusCodeSpec(200))
        val expectedId = 4
        val expectedToken = "QpwL5tke4Pnpja7X4"
        val user = UserRegister("eve.holt@reqres.in", "pistol")

        val userRegisterResponse = given()
            .body(user)
            .`when`()
            .post("/api/register")
            .then()
            .extract().`as`(UserRegisterSuccess::class.java)

        assertEquals(expectedId, userRegisterResponse.id, "User id should be equal to $expectedId")
        assertEquals(expectedToken, userRegisterResponse.token, "Registration token should be equal to $expectedToken")
    }

    @Test
    @Description("Status code and response body for unsuccessful registration attempt.")
    fun unsuccessfulRegistrationTest() {
        Specification.installSpecification(requestSpec = Specification.requestSpec(Constants.BASE_URL), responseSpec = Specification.responseStatusCodeSpec(400))
        val user = UserRegister("sydney@fife", "")
        val expectedError = "Missing password"

        val userRegisterResponse = given()
            .body(user)
            .`when`()
            .post("/api/register")
            .then()
            .extract().`as`(UserRegisterError::class.java)

        assertEquals(expectedError, userRegisterResponse.error, "Error should be equal to $expectedError")
    }

    @Test
    @Description("Status code when deleting user.")
    fun deleteUserResponseCodeTest() {
        val userId = 2
        val expectedStatusCode = 204
        Specification.installSpecification(requestSpec = Specification.requestSpec(Constants.BASE_URL), responseSpec = Specification.responseStatusCodeSpec(expectedStatusCode))

        given()
            .`when`()
            .delete("/api/users/$userId")
            .then()
    }

    @Test
    @Description("Resource list is sorted by year.")
    fun checkResourceListIsSortedByYearTest() {
        Specification.installSpecification(requestSpec = Specification.requestSpec(Constants.BASE_URL), responseSpec = Specification.responseStatusCodeSpec(200))

        val resourceList = given()
            .`when`()
            .get("/api/unknown")
            .then()
            .extract().jsonPath().getList("data", Resource::class.java)

        val years = resourceList.map { resource ->
            resource.year
        }
        assertTrue(years.isNotEmpty(), "Asserted list shouldn't be empty")
        val sortedYears = years.sorted()
        assertEquals(sortedYears, years, "Resource list should be sorted ascending")
    }
}