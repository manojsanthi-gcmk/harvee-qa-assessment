package com.harvee.qa.pages;

import com.microsoft.playwright.Page;

public class LoginPage {
	
	public final Page page;
	
	public final String UsernameInput = "#user-name";
	public final String PasswordInput = "#password";
	public final String loginbtn = "#login-button";
	public final String errorMsg = "[data-test='error']";

	
	public LoginPage(Page page) {
		this.page = page;
		
	}
	
	public void navigate() {
		page.navigate("https://www.saucedemo.com/");
		
	}
	
	public void signin(String username, String password) {
		
		page.locator(UsernameInput).fill(username);
		page.locator(PasswordInput).fill(password);
    	
    	page.locator(loginbtn).click();
    	
    	
    }
	

}
