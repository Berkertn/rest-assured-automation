package tests;

import Pages.BoardPage;
import Pages.CardPage;
import Pages.ListPage;
import Utils.RequestBuilderUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.*;
import static Utils.RandomUtil.getRandomNumber;
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
        assertFalse(createBoardResponse.jsonPath().getString("id").isEmpty(), "ID field is empty for " + boardPage.getCreateURL());
        String boardId = createBoardResponse.jsonPath().getString("id");
        boardPage.setBoardID(boardId);

        // create a new list
        ListPage listPage = new ListPage("New List For Testing", boardPage.getBoardID());
        listPage.setParamsDefault();
        requestBuilderUtil = new RequestBuilderUtil(listPage.getCreateURL(), "POST", listPage.getRequestBody(), listPage.getParams());
        Response createList = requestBuilderUtil.sendRequest();

        Response createListResponse = createList
                .then()
                .statusCode(200)
                .extract()
                .response();
        assertFalse(createListResponse.jsonPath().getString("id").isEmpty(), "ID field is empty for" + listPage.getCreateURL());
        String listID = createListResponse.jsonPath().getString("id");
        listPage.setListID(listID);

        // create 2 cards
        CardPage cardPage;
        ArrayList<String> cardIDs = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            // init card page and setting params
            cardPage = new CardPage("Card-" + i + 1);
            cardPage.setParamsDefault();
            cardPage.addParameter("idList", listPage.getListID());
            requestBuilderUtil = new RequestBuilderUtil(cardPage.getCreateURL(), "POST", cardPage.getRequestBody(), cardPage.getParams());
            Response createCard = requestBuilderUtil.sendRequest();

            Response createCardResponse = createCard
                    .then()
                    .statusCode(200)
                    .extract()
                    .response();
            assertFalse(createCardResponse.jsonPath().getString("id").isEmpty(), "ID field is empty for " + cardPage.getCreateURL());
            cardIDs.add(createCardResponse.jsonPath().getString("id"));
        }

        // update a card which is selected random
        int randomNumber = getRandomNumber(cardIDs.size());

        cardPage = new CardPage("Card-Updated");
        cardPage.setCardID(cardIDs.get(randomNumber));
        cardPage.setParamsDefault();
        cardPage.addParameter("desc", "updated.");
        requestBuilderUtil = new RequestBuilderUtil(cardPage.getUpdateURL(), "PUT", cardPage.getRequestBody(), cardPage.getParams());
        Response updateCard = requestBuilderUtil.sendRequest();

        Response updateCardResponse = updateCard
                .then()
                .statusCode(200)
                .extract()
                .response();

        // delete all the cards
        for (String cardID : cardIDs) {
            cardPage = new CardPage();
            cardPage.setCardID(cardID);
            cardPage.setParamsDefault();
            requestBuilderUtil = new RequestBuilderUtil(cardPage.getDeleteURL(), "DELETE", cardPage.getRequestBody(), cardPage.getParams());
            Response deleteCard = requestBuilderUtil.sendRequest();

            Response deleteCardResponse = deleteCard
                    .then()
                    .statusCode(200)
                    .extract()
                    .response();
        }

        // delete the board
        boardPage.setParamsDefault();
        boardPage.deleteParameter("name");
        requestBuilderUtil = new RequestBuilderUtil(boardPage.getDeleteURL(), "DELETE", boardPage.getRequestBody(), cardPage.getParams());
        Response deleteBoard = requestBuilderUtil.sendRequest();

        Response deleteBoardResponse = deleteBoard
                .then()
                .statusCode(200)
                .extract()
                .response();
        assertTrue(deleteBoardResponse.jsonPath().getString("_value") == null, "value field is not null it has:" + deleteBoardResponse.jsonPath().getString("_value"));

    }
}
