package com.harvee.qa.pages;

import com.microsoft.playwright.*;
import com.harvee.qa.pages.LoginPage;
import com.harvee.qa.pages.ProductsPage;
import com.harvee.qa.pages.CheckoutPage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class CheckoutTest {

    static Playwright playwright;
    static Browser browser;
    Page page;
    LoginPage loginPage;
    ProductsPage productsPage;
    CheckoutPage checkoutPage;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false));
    }

    @AfterAll
    static void closeBrowser() {
        browser.close();
        playwright.close();
    }

    @BeforeEach
    void setup() {
        page = browser.newContext().newPage();
        loginPage = new LoginPage(page);
        productsPage = new ProductsPage(page);
        checkoutPage = new CheckoutPage(page);
        loginPage.navigate();
        loginPage.signin("standard_user", "secret_sauce");
    }

    @AfterEach
    void teardown() {
        page.close();
    }

    
    @Test
    void TC13_fullCheckout_totalIsItemPlusTax_andCartEmpties() {
        productsPage.addToCartByName("Sauce Labs Backpack");
        productsPage.openCart();

        checkoutPage.clickCheckout();
        checkoutPage.fillInfo("Manoj", "Kumar", "600097");
        checkoutPage.clickContinue();

        double itemTotal = checkoutPage.amountFrom(checkoutPage.itemTotalLabel);
        double tax       = checkoutPage.amountFrom(checkoutPage.taxLabel);
        double total     = checkoutPage.amountFrom(checkoutPage.totalLabel);

        // total must equal item total + tax
        assertEquals(itemTotal + tax, total, 0.001,
                "Total should equal item total + tax");

        checkoutPage.clickFinish();
        assertThat(page.locator(checkoutPage.completeHeader))
                .hasText("Thank you for your order!");

        // cart should be empty after finishing (no badge)
        assertEquals(0, page.locator(checkoutPage.cartBadge).count(),
                "Cart should be empty after order completion");
    }

    
    @ParameterizedTest(name = "missing field -> {2}")
    @CsvSource({
            // first, last, postal, expectedError
            "'',    Kumar, 600097, Error: First Name is required",
            "Manoj, '',    600097, Error: Last Name is required",
            "Manoj, Kumar, '',     Error: Postal Code is required"
    })
    void TC14_missingCheckoutFields_showError(String first, String last,
                                              String postal, String expectedError) {
        productsPage.addToCartByName("Sauce Labs Backpack");
        productsPage.openCart();

        checkoutPage.clickCheckout();
        checkoutPage.fillInfo(first, last, postal);
        checkoutPage.clickContinue();

        assertThat(page.locator(checkoutPage.errorMessage)).hasText(expectedError);
    }

   
    @Test
    void TC21_directUrlAfterLogout_isBlocked() {
        // we are logged in from setup; now log out by clearing session via new context
        page.navigate("https://www.saucedemo.com/");      // go to login (logged out state)

        // try to open inventory directly
        page.navigate("https://www.saucedemo.com/inventory.html");

        // app should block and show the login error
        assertThat(page.locator("[data-test='error']"))
                .containsText("You can only access '/inventory.html' when you are logged in");
    }
}
