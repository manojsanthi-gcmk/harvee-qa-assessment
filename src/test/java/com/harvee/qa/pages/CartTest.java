package com.harvee.qa.pages;

import com.microsoft.playwright.*;
import com.harvee.qa.pages.LoginPage;
import com.harvee.qa.pages.ProductsPage;
import org.junit.jupiter.api.*;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


public class CartTest {

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
    void TC10_addTwoItems_removeOne_checkBadgeAndContents() {
      
        productsPage.addToCartByName("Sauce Labs Backpack");
        productsPage.addToCartByName("Sauce Labs Bike Light");
        assertEquals(2, productsPage.cartCount(), "Badge should show 2 after adding 2 items");

       
        productsPage.removeFromCartByName("Sauce Labs Bike Light");
        assertEquals(1, productsPage.cartCount(), "Badge should show 1 after removing 1 item");

      
        productsPage.openCart();
        List<String> cartItems = page.locator(".inventory_item_name").allTextContents();
        assertEquals(1, cartItems.size(), "Cart should contain exactly 1 item");
        assertEquals("Sauce Labs Backpack", cartItems.get(0), "Remaining item should be the Backpack");
    }
    
    
    
    
    
}
