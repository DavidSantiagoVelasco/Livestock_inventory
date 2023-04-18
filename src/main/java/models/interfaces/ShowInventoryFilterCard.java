package models.interfaces;

import javafx.scene.layout.AnchorPane;

public class ShowInventoryFilterCard {

    private final String type;
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
}
