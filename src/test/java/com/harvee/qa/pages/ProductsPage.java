package com.harvee.qa.pages;

import com.microsoft.playwright.Page;

import com.microsoft.playwright.Locator;


public class ProductsPage {
	
	public final Page page;
	
	public final String sortDropdown = "//select[@class='product_sort_container']";
	public final String productPrices = "//div[@class='inventory_item_price']";
	public final String productNames = "//div[@class='inventory_item_name ']";
	 public final String cartBadge    = "[data-test='shopping-cart-badge']";
	 public final String cartLink     = "[data-test='shopping-cart-link']";

	
	public ProductsPage(Page page) {
		this.page = page;
	}
	
	public void sortBy(String option) {
		page.locator(sortDropdown).selectOption(option);
		
	}
	
	 public void addToCartByName(String productName) {
	        // the Add button sits in the same item card as the product name
	        String id = productName.toLowerCase().replace(" ", "-").replace(".", "")
	                .replace("(", "").replace(")", "");
	        page.locator("[data-test='add-to-cart-" + id + "']").click();
	    }

	    
	    public void removeFromCartByName(String productName) {
	        String id = productName.toLowerCase().replace(" ", "-").replace(".", "")
	                .replace("(", "").replace(")", "");
	        page.locator("[data-test='remove-" + id + "']").click();
	    }

	    
	    public int cartCount() {
	        Locator badge = page.locator(cartBadge);
	        if (badge.count() == 0) return 0;
	        return Integer.parseInt(badge.textContent().trim());
	    }

	    public void openCart() {
	        page.locator(cartLink).click();
	    }
	}
	


