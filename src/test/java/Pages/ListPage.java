package Pages;

public class ListPage extends BasePage {
    public ListPage(String name, String boardId) {
        super(name);
        createURL = String.format("/1/boards/%s/lists", boardId);

    }
}
