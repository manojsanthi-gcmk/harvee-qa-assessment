package com.harvee.qa.pages;

import com.microsoft.playwright.Page;

/**
 * Page Object covering the cart + checkout flow:
 *   cart -> checkout info form -> overview (totals) -> finish (complete).
 */
public class CheckoutPage {

    public final Page page;

    // ---- locators ----
    public final String checkoutButton = "[data-test='checkout']";
    public final String firstName      = "[data-test='firstName']";
    public final String lastName       = "[data-test='lastName']";
    public final String postalCode     = "[data-test='postalCode']";
    public final String continueButton = "[data-test='continue']";
    public final String finishButton   = "[data-test='finish']";
    public final String errorMessage   = "[data-test='error']";

    public final String itemTotalLabel = "[data-test='subtotal-label']"; // "Item total: $xx.xx"
    public final String taxLabel        = "[data-test='tax-label']";      // "Tax: $x.xx"
    public final String totalLabel      = "[data-test='total-label']";    // "Total: $xx.xx"
    public final String completeHeader  = "[data-test='complete-header']"; // "Thank you for your order!"
    public final String cartBadge       = "[data-test='shopping-cart-badge']";

    public CheckoutPage(Page page) {
        this.page = page;
    }

    public void clickCheckout() {
        page.locator(checkoutButton).click();
    }

    public void fillInfo(String first, String last, String postal) {
        page.locator(firstName).fill(first);
        page.locator(lastName).fill(last);
        page.locator(postalCode).fill(postal);
    }

    public void clickContinue() {
        page.locator(continueButton).click();
    }

    public void clickFinish() {
        page.locator(finishButton).click();
    }

    /** Extract the dollar amount from a label like "Item total: $39.98". */
    public double amountFrom(String locator) {
        String text = page.locator(locator).textContent();   // e.g. "Item total: $39.98"
        String number = text.substring(text.indexOf("$") + 1).trim();
        return Double.parseDouble(number);
    }
}
