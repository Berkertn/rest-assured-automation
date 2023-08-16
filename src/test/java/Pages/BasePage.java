package Pages;

public abstract class BasePage {
    private String createURL;

    public BasePage() {
        this.createURL = "";
    }

    public String getCreateURL() {
        return createURL;
    }
}
