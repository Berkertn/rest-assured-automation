package Pages;

public class ListPage extends BasePage {
    private String listID;

    public ListPage(String name, String boardId) {
        super(name);
        createURL = String.format("/1/boards/%s/lists", boardId);
    }

    public ListPage() {
        createURL = "/1/cards";
    }

    public void setListID(String listID) {
        this.listID = listID;
    }

    public String getListID() {
        return listID;
    }
}
