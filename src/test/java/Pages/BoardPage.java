package Pages;

public class BoardPage extends BasePage{

    public BoardPage(String name) {
        super(name);
        createURL = "/1/boards";
        deleteURL = "/1/boards/"; // + boardID
    }
}
