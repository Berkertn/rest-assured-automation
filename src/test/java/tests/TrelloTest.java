package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static Data.UserInfo.getApiKey;
import static Data.UserInfo.getApiToken;
import static Utils.RandomUtil.getRandomNumber;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class TrelloTest {
    @Test
    public void trelloEndToEndTest() {
        RestAssured.baseURI = "https://api.trello.com";
        JSONObject requestBody = new JSONObject();
        requestBody.put("", "");
        //create a new board
        Response createBoardResponse = given()
                .queryParam("name", "New Board For Testing")
                .queryParam("key", getApiKey())
                .queryParam("token", getApiToken())
                .body(requestBody.toString())
                .when()
                .post("/1/boards")
                .then()
                .statusCode(200)
                .extract()
                .response();
        String boardId = createBoardResponse.jsonPath().getString("id");

        // create a new list

        Response createListResponse = given()
                .queryParam("name", "New List For Testing")
                .queryParam("key", getApiKey())
                .queryParam("token", getApiToken())
                .body(requestBody.toString())
                .when()
                .post(String.format("/1/boards/%s/lists", boardId))
                .then()
                .statusCode(200)
                .extract()
                .response();
        String listID = createListResponse.jsonPath().getString("id");

        // create 2 cards
        ArrayList<String> cardIDs = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Response createCardResponse = given()
                    .queryParam("name", "Card-" + i)
                    .queryParam("key", getApiKey())
                    .queryParam("token", getApiToken())
                    .queryParam("idList", listID)
                    .body(requestBody.toString())
                    .when()
                    .post("/1/cards")
                    .then()
                    .statusCode(200)
                    .extract()
                    .response();

            assertFalse(createCardResponse.jsonPath().getString("id").isEmpty(), "ID field is empty");
            cardIDs.add(createCardResponse.jsonPath().getString("id"));
        }

        // update a card which is selected random
        int randomNumber = getRandomNumber(cardIDs.size());
        Response updateCardResponse = given()
                .queryParam("desc", "updated.")
                .queryParam("key", getApiKey())
                .queryParam("token", getApiToken())
                .body(requestBody.toString())
                .when()
                .put("/1/cards/" + cardIDs.get(randomNumber))
                .then()
                .statusCode(200)
                .extract()
                .response();

        // delete all the cards
        for (String cardID : cardIDs) {
            Response deleteCardResponse = given()
                    .queryParam("key", getApiKey())
                    .queryParam("token", getApiToken())
                    .body(requestBody.toString())
                    .when()
                    .delete("/1/cards/" + cardID)
                    .then()
                    .statusCode(200)
                    .extract()
                    .response();
        }

        // delete the board
        Response deleteBoardResponse = given()
                .queryParam("key", getApiKey())
                .queryParam("token", getApiToken())
                .body(requestBody.toString())
                .when()
                .delete("/1/boards/" + boardId)
                .then()
                .statusCode(200)
                .extract()
                .response();
        assertTrue(deleteBoardResponse.jsonPath().getString("_value") == null, "value field is not null it has:" + deleteBoardResponse.jsonPath().getString("_value"));

    }
}
