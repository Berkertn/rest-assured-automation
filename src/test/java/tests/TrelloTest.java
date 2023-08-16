package tests;

import Pages.BoardPage;
import Pages.ListPage;
import Utils.RequestBuilderUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.*;

import static Data.UserInfo.getApiKey;
import static Data.UserInfo.getApiToken;
import static Utils.RandomUtil.getRandomNumber;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class TrelloTest {
    @Test
    public void trelloEndToEndTest() {
        // declare base URL
        RestAssured.baseURI = "https://api.trello.com";

        // get instant for board
        BoardPage boardPage = new BoardPage("New Board For Testing");


        //create a new board
        boardPage.setParamsDefault();
        RequestBuilderUtil requestBuilderUtil = new RequestBuilderUtil(boardPage.getCreateURL(), "POST", boardPage.getRequestBody(), boardPage.getParams());
        Response createBoard = requestBuilderUtil.sendRequest();

        Response createBoardResponse = createBoard
                .then()
                .statusCode(200)
                .extract()
                .response();
        String boardId = createBoardResponse.jsonPath().getString("id");

        // create a new list
        ListPage listPage = new ListPage("New List For Testing",boardId);

        listPage.setParamsDefault();
        requestBuilderUtil = new RequestBuilderUtil(listPage.getCreateURL(), "POST", listPage.getRequestBody(), listPage.getParams());
        Response createList = requestBuilderUtil.sendRequest();

        Response createListResponse = createList
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
                    .body(boardPage.getRequestBody())
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
                .body(boardPage.getRequestBody())
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
                    .body(boardPage.getRequestBody())
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
                .body(boardPage.getRequestBody())
                .when()
                .delete("/1/boards/" + boardId)
                .then()
                .statusCode(200)
                .extract()
                .response();
        assertTrue(deleteBoardResponse.jsonPath().getString("_value") == null, "value field is not null it has:" + deleteBoardResponse.jsonPath().getString("_value"));

    }
}
