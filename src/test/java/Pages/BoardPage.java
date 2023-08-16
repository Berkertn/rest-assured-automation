package Pages;

public class BoardPage extends BasePage {
    private String boardID;

    public BoardPage(String name) {
        super(name);
        createURL = "/1/boards";
    }

    public BoardPage() {
        createURL = "/1/cards";
    }

    public void setBoardID(String boardID) {
        this.boardID = boardID;
        deleteURL = "/1/boards/" + boardID;
    }

    public String getBoardID() {
        return boardID;
    }
}
