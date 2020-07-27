package com.kderyabin.core.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Currency;

/**
 * Ready to display model with owed amount.
 */
@ToString
@Getter
@Setter
public class RefundmentModel {
    /**
     * Debtor's name
     */
    private String debtor;
    /**
     * Name of the person to whom debtor owes money
     */
    private String creditor;
    /**
     * Owed amount
     */
    private Double amount;
    /**
     * Currency
     */
    private Currency currency;

    /**
     * String representation of owed amount with currency
     *
     * @return Amount to display
     */
    public String getDisplayAmount() {
        return amount + " " + currency.getCurrencyCode();
    }
}
