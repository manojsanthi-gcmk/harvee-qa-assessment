# Section B – AI Output and Client Communication

## B1. Review of the AI-generated "cart total" test

### Problems I found
1. **Unnecessary `waitForTimeout(3000)`** – Playwright already auto-waits for
   elements, so a fixed 3-second sleep is not needed. It makes the test flaky and
   slow, and can cause timeout errors.
2. **Ambiguous `text=Add to cart`** – it only says "Add to cart", not which specific
   product to add. There are several such buttons, so it just clicks one by luck.
   A specific `data-test` selector for the product should be used.
3. **Weak assertion** – `expect(total).toBeTruthy()` only checks that the total
   element is present; it does not validate that the total is **correct**
   (item total + tax). A meaningless assertion gives false confidence.
4. **No assertion on the final/confirmation page** – after continuing through
   checkout there is no check that the order actually completed
   ("Thank you for your order!").
5. **Locators in the test body** – raw selectors (`#user-name`, `.shopping_cart_link`)
   are scattered through the test; they should live in a Page Object.
6. **No config** – the URL and credentials are hard-coded in the test instead of
   living in config / environment variables.
7. **No isolation or cleanup** – there is no fresh context per test and no teardown,
   so the test is not guaranteed to run independently.

### Rewritten test (Playwright + JUnit, POM-style, meaningful assertion)
```java
@Test
void cartTotalEqualsItemTotalPlusTax() {
    loginPage.navigate();
    loginPage.signin(Config.standardUser(), Config.password());
    assertThat(page).hasURL(Pattern.compile(".*inventory.html"));   // login verified

    productsPage.addToCartByName("Sauce Labs Backpack");
    productsPage.openCart();

    checkoutPage.clickCheckout();
    checkoutPage.fillInfo("Manoj", "Kumar", "600097");
    checkoutPage.clickContinue();

    double itemTotal = checkoutPage.amountFrom(checkoutPage.itemTotalLabel);
    double tax       = checkoutPage.amountFrom(checkoutPage.taxLabel);
    double total     = checkoutPage.amountFrom(checkoutPage.totalLabel);

    // meaningful assertion: total must equal item total + tax
    assertEquals(itemTotal + tax, total, 0.001,
            "Total must equal item total + tax");
}
```
Key changes: no fixed sleep (auto-wait), a real value assertion, POM with
`data-test` locators, config for URL/users, and login verified before proceeding.

---

## B2. Evaluation set for an AI feature

**Chosen scenario: CI failure triage bot** – it reads each failed test log and labels
it as a *product defect*, *flaky test* or *environment problem*, then posts to Slack.

| # | Check | How I would score it |
|---|-------|----------------------|
| 1 | **Label accuracy** – defect / flaky / environment is labelled correctly | **Automatic**: run against a labelled set of past failures and compare the bot's label to the known correct label (accuracy per label). |
| 2 | **Costly mis-label** – a real **product defect** must never be labelled "flaky" (that hides a genuine bug). This mistake costs the most. | **Rubric**: weight this error heavily; review every defect→flaky and defect→environment miss, since missing a real defect is worse than a false alarm. |
| 3 | **Unseen failures** – a failure type it has never seen before (e.g. a brand-new crash) | **Human review**: check it flags low-confidence / "unknown" instead of guessing a wrong label. |
| 4 | **Secrets in logs** – logs can contain tokens, device serials, credentials; the Slack post must not leak them | **Automatic**: scan the posted message with a secret/PII pattern check; pass only if nothing sensitive is exposed. |

*Example from my work:* if a whole OTA test suite fails because the OTA build never
installed, that is not many separate flaky failures — it's **one blocker (high
priority)**. A good triage bot must recognise that pattern and label it a product/
environment blocker, not dozens of unrelated flaky tests. I would check exactly this
case in the evaluation set.

Checks 1 and 4 have clear pass/fail, so I run them automatically on every model/prompt
change; checks 2 and 3 need judgement, so I sample them by rubric / human review.

---

## B3. Defect report – sorting by price is wrong

**Title:** Sorting by "Price (low to high)" does not order products by ascending price

**Environment:** SauceDemo release 4.2.0, Chrome (latest), Windows 11, desktop

**Pre-conditions:** Logged in as `standard_user`, on the Products page

**Steps to reproduce:**
1. Log in as standard_user.
2. On the Products page, open the sort dropdown.
3. Select **"Price (low to high)"**.
4. Read the product prices top to bottom.

**Expected result:** Products are ordered by ascending price (lowest first).

**Actual result:** Order is wrong – an item at **$29.99** appears before an item at
**$15.99**. Reproducible on **every run since 4.2.0**.

**Severity:** **High.** Price sorting is a core e-commerce function. A wrong order
directly misleads customers at the point of a purchase decision, and it is a
business-level correctness issue. It reproduces on every run (consistent, not
intermittent), which raises confidence that it is a real product defect rather than
flakiness.

**Evidence to attach:** screenshot of the sorted list with the out-of-order prices
highlighted, the automated test output/log showing the failing comparison, and the
build number (4.2.0).

---

## B4. Message to the (non-technical) product manager

> **Subject: Friday release – quick update before we ship**
>
> Hi [PM],
>
> Quick update before tomorrow's release. Testing this version found **one real bug**:
> sorting products by "Price: low to high" shows items in the wrong order (a higher-
> priced item appears before a cheaper one). It happens on every run, so customers
> would notice. There is also **one checkout test that fails about 1 in 10 runs and
> passes on retry** – likely a timing/stability issue, which I'm still investigating.
>
> The team is already preparing a **patch build** to fix the sort bug, currently about
> halfway done. My recommendation: rather than ship the broken sort on Friday, give us
> **the weekend** to finish and verify the fix in the patch build. We'll run these
> specific cases over Sat–Sun, merge to the main build once it's confirmed, and deliver
> a demo on **Monday**.
>
> Happy to walk you through it – ping me with any concerns.
>
> Thanks,
> Manojkumar
