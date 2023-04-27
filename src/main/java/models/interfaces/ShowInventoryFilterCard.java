package models.interfaces;

import javafx.scene.layout.AnchorPane;

public class ShowInventoryFilterCard {

    private final String type;
    private String value;
    private final AnchorPane card;

    public ShowInventoryFilterCard(String type, AnchorPane card) {
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
