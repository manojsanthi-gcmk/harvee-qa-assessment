# SauceDemo QA Assessment – Harvee Technologies

Manual + automated test suite for the SauceDemo "shop and check out" use case.
Stack: **Java + Playwright + JUnit 5** with the **Page Object Model**.

---

## Project structure

```
saucedemo-qa-assessment/
├── README.md                     # this file
├── AI_USAGE.md                   # where/how AI was used
├── ANSWERS.md                    # Section B answers
├── manual-tests/
│   ├── Sauce_demo_test_cases.xlsx  # 22 manual test cases (A1)
│   ├── A1_questions.md             # questions + assumptions
│   ├── A1_strategy.md              # automation strategy + biggest risk
│   └── A1_exploratory.md           # 15-min exploratory charter
├── pom.xml
└── src/test/java/com/harvee/qa/
    ├── pages/                    # Page Objects (locators live here)
    │   ├── LoginPage.java
    │   ├── ProductsPage.java
    │   └── CheckoutPage.java
    └── tests/                    # Test classes
        ├── LoginTest.java
        ├── ProductsTest.java
        ├── CartTest.java
        └── CheckoutTest.java
```

---

## Setup

1. Install **Java 17+** and **Maven**.
2. Clone/unzip this project.
3. Install dependencies and the Playwright browsers:
   ```
   mvn clean install
   mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"
   ```

## How to run the tests

Run the whole suite:
```
mvn test
```
Run a single class:
```
mvn test -Dtest=LoginTest
```

- Reports: JUnit XML is produced under **`target/surefire-reports/`** after `mvn test`.
- Base URL and the test user are currently set inside the page objects / test setup
  for simplicity. **Next step:** move them into a `config.properties` / environment
  variables so they are not in the test code (noted as an improvement).
- **Screenshot on failure** can be added via a JUnit `TestWatcher` extension; it is
  listed as a planned improvement for the scaled suite.

---

## Traceability – manual case ID ↔ automated test

| Manual ID | Manual case                         | Automated test (class :: method)                      |
|-----------|-------------------------------------|-------------------------------------------------------|
| TC01      | Standard user login                 | LoginTest :: TC01_standardUserLogin                   |
| TC03      | Wrong password error                | LoginTest :: TC03_wrongPassword_showsError            |
| TC19      | Locked-out user error               | LoginTest :: TC19_lockedOutUser_showsError            |
| TC08      | Sort by price (low→high), full list | ProductsTest :: TC08_sortPriceLowToHigh               |
| TC06      | Sort by name (Z→A)                  | ProductsTest :: TC06_sortNameZtoA                     |
| TC10      | Add two items, remove one           | CartTest :: TC10_addTwoItems_removeOne                |
| TC13/TC17 | Full checkout, total = item + tax   | CheckoutTest :: TC13_fullCheckout_totalIsItemPlusTax  |
| TC14      | Missing checkout fields             | CheckoutTest :: TC14_missingCheckoutFields (3 params) |
| TC21      | Direct URL after logout (security)  | CheckoutTest :: TC21_directUrlAfterLogout_isBlocked   |

Cases not automated (usability, accessibility, mobile layout, social links,
reset-app-state visual, error truncation) are marked **"No"** in the Excel with a
reason – they need human judgment or visual verification.

---

## How this suite would grow to ~200 tests

I would keep the same Page Object structure and expand in layers. Shared setup
(login, browser lifecycle) would move into a single reusable **BaseTest** so every
new test class stays short. More page objects (menu, about, footer) would be added
as the app grows, keeping locators out of tests. Data-driven cases (different users,
products, postal formats) would use **parameterized tests** so one method covers many
inputs. Tests would be tagged (`@Tag("smoke")`, `@Tag("regression")`) so CI can run a
fast smoke set on every commit and the full suite nightly, in parallel across
browsers. Flaky areas would be isolated with stable `data-test` selectors and
web-first waits — never fixed sleeps. Reporting would move to an HTML report
(e.g. Allure) with screenshots and traces attached on failure.
```
