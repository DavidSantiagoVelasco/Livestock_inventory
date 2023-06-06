package models.responses;

import javafx.collections.ObservableList;
import models.interfaces.Finance;

public class FilterFinancesResponse {

    private final ObservableList<Finance> finances;
    private final double totalIncomes;
    private final double totalExpenses;

    public FilterFinancesResponse(ObservableList<Finance> finances, double totalIncomes, double totalExpenses) {
        this.finances = finances;
        this.totalIncomes = totalIncomes;
        this.totalExpenses = totalExpenses;
    }

    public ObservableList<Finance> getFinances() {
        return finances;
    }

    public double getTotalIncomes() {
        return totalIncomes;
    }

    public double getTotalExpenses() {
        return totalExpenses;
    }
}
