package models.interfaces;

import javafx.scene.layout.AnchorPane;

public class FilterCard {

    private final String type;
    private String value;
    private final AnchorPane card;

    public FilterCard(String type, AnchorPane card) {
        this.type = type;
        this.card = card;
    }

    public String getType() {
        return type;
    }

    public AnchorPane getCard() {
        return card;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
