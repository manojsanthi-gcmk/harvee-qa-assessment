package com.harvee.qa.pages;

import com.microsoft.playwright.*;
import com.harvee.qa.pages.LoginPage; 
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.BeforeEach;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;



public class LoginTest {
	
	static Playwright playwright;
	static Browser browser;
	Page page;
	LoginPage loginPage;
	
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
	
	void createPage() {
		page = browser.newContext().newPage();
		loginPage = new LoginPage(page);
		
		
	}
	
	
	@Test
	
	void TC01_userCan_login() {
		loginPage.navigate();
		loginPage.signin("standard_user", "secret_sauce");
		
		assertThat(page).hasURL("https://www.saucedemo.com/inventory.html");
	}
	
	@Test
	
	void TC19_lockedOutUser_showsError() {
		    loginPage.navigate();
		    loginPage.signin("locked_out_user", "secret_sauce");
		    assertThat(page.locator(loginPage.errorMsg))
		            .hasText("Epic sadface: Sorry, this user has been locked out.");
		}
	
	@Test
	
	void TC03_InvalidPassword_showsError() {
		loginPage.navigate();
		loginPage.signin("standard_user", "12345");
		assertThat(page.locator("//h3")).hasText("Epic sadface: Username and password do not match any user in this service");
		
	}

		
		
		
		
	}
	
	
	
	
	
	
	


