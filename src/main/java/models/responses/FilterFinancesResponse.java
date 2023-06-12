package models.responses;

import javafx.collections.ObservableList;
import models.interfaces.Finance;

public record FilterFinancesResponse(ObservableList<Finance> finances, double totalIncomes, double totalExpenses) {

}
