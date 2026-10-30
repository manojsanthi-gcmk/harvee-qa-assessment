# AI Usage Log

Tool used: **Kiro (AI coding assistant)**.

I used AI as a supporting tool for structure guidance, boilerplate, and debugging
— not to generate the suite blindly. I wrote, ran, and validated every test in the
live application, and I understand each line. Below is an honest record of where AI
helped and what I accepted, corrected, or rejected.

---

## A1 – Manual test cases
- **Prompt:** "What coverage types should 20–25 SauceDemo test cases include?"
- **AI output:** Suggested positive/negative/boundary plus usability, accessibility,
  browser/screen and security cases, and a parameterized missing-fields case.
- **My action:** Wrote all 22 cases myself in Excel using my own steps and expected
  results. Used the AI list only as a coverage checklist.
- **Corrected:** AI first suggested marking almost everything "Automate = Yes".
  I changed usability / accessibility / mobile-layout / visual cases to **"No"**,
  because those need human judgment, not automation.

## A2 – Automation framework
- **Prompt:** "Give a simple POM structure for SauceDemo with Playwright + JUnit."
- **AI output:** Page Object classes (Login, Products, Checkout) and test classes
  with `@BeforeAll`/`@BeforeEach` lifecycle.
- **My action:** Built the pages and tests, ran each in the live app, and fixed
  issues myself (see below). I kept the design simple so I can explain every part.

### Things I had to correct while building
- **Locator vs value confusion:** my `signin()` first used the same name for the
  locator field and the method parameter, which broke `fill()`. Fixed by using a
  separate parameter name (`username`/`password`).
- **NullPointerException:** the page object wasn't created in `@BeforeEach`
  (`loginPage` was null). Added `loginPage = new LoginPage(page)`.
- **File vs class name:** `CheckoutPage` class in a file named `CheckOutPage.java`
  failed to compile. Renamed the file to match the class.
- **Sort verification:** I verify the **whole price list** is ascending (not just the
  first item) by converting the price text to numbers and comparing each with the
  next. For Z→A I compare against a reverse-sorted copy.
- **Missing imports:** added `org.junit.jupiter.api.Assertions.*` for `assertTrue`,
  and `Locator`, `ArrayList`, `Collections` where needed.

## Section B
- **Prompt:** "Explain what each B2 AI-evaluation point means."
- **My action:** Wrote the answers in my own words based on my QA experience.
- **B1:** I reviewed the AI-written cart-total test, listed its problems
  (hard-coded `waitForTimeout`, weak `toBeTruthy()` assertion, no POM, locators in the
  test, no isolation) and rewrote it properly.

---

## What I did manually
- Wrote all manual test cases and their expected results.
- Ran, debugged and verified every automated test against the live site.
- Applied and validated each fix before accepting it.
- Chose stable `data-test` selectors over brittle XPath.

## Limitations
- Tests run against the live public SauceDemo site; a site change or outage would
  fail them. No retry logic is implemented.
- Screenshots on failure are saved locally; richer HTML reporting (Allure) is a
  next step for a scaled suite.
