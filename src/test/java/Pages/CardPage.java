package Pages;

public class CardPage extends BasePage {
    private String cardID;

    public CardPage(String name) {
        super(name);
        createURL = "/1/cards";
    }
    public CardPage() {
        createURL = "/1/cards";
    }

    public void setCardID(String cardID) {
        this.cardID = cardID;
        updateURL = "/1/cards/" + cardID;
        deleteURL = "/1/cards/" + cardID;
    }

    public String getCardID() {
        return cardID;
    }
}
