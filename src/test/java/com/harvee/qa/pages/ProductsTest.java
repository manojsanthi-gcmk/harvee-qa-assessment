package com.harvee.qa.pages;

import com.microsoft.playwright.*;
import com.harvee.qa.pages.LoginPage;
import com.harvee.qa.pages.ProductsPage;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductsTest {

    static Playwright playwright;
    static Browser browser;
    Page page;
    LoginPage loginPage;
    ProductsPage productsPage;

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
        loginPage.navigate();
        loginPage.signin("standard_user", "secret_sauce");
    }

    @AfterEach
    void teardown() {
        page.close();
    }

    @Test
    void TC08_sortPriceLowToHigh() {
        productsPage.sortBy("lohi");

        List<String> priceTexts = page.locator(productsPage.productPrices).allTextContents();
        List<Double> prices = priceTexts.stream()
                .map(p -> Double.parseDouble(p.replace("$", "")))
                .toList();

        for (int i = 0; i < prices.size() - 1; i++) {
            assertTrue(prices.get(i) <= prices.get(i + 1),
                    "List not sorted ascending at index " + i);
        }
    }

    @Test
    void TC06_sortNameZtoA() {
        productsPage.sortBy("za");  

        List<String> names = page.locator(productsPage.productNames).allTextContents();

       
        List<String> expected = new ArrayList<>(names);
        expected.sort(Collections.reverseOrder());
        assertEquals(expected, names, "Products not sorted Z to A");
    }
}
