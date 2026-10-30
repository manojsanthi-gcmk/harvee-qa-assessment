# A1 – Questions for the Product Owner + Assumptions

Where I did not get an answer, I state the assumption I tested against.

1. **Is the postal code validated against a specific format (length, country, numbers-only)?**
   *Assumption:* There is no format validation — the field accepts any numbers,
   letters, or alphanumeric combinations. It only shows an error when the field is
   left empty. I test that an empty postal code is rejected and any non-empty value
   is accepted.

2. **Should the first name / last name fields reject numbers and symbols?**
   *Assumption:* There is no character validation — the fields accept anything typed
   in, including symbols, letters, numbers, and alphanumeric combinations. They only
   need to be non-empty. I test the required-field errors and note the missing input
   validation on name fields as a quality risk.

3. **Which users are in scope for testing?**
   *Assumption:* Not all users. `standard_user` is the clean, defect-free account, so
   it is used for the main functional and automation flows. `locked_out_user` is used
   for the negative login test. The remaining accounts (`problem_user`,
   `performance_glitch_user`) contain intentional defects and are not suitable for
   automation, so I cover `problem_user` only in a short, time-boxed exploratory
   charter.

4. **What is the expected tax rate, and should "Total" always equal item total + tax?**
   *Assumption:* The tax is applied on top of the product cost, so the checkout
   overview total must account for both. When I test the overview page I don't check
   the product cost alone — I assert that **Total = item total + tax**. I verify this
   relationship rather than a fixed tax value, so the test still holds if the tax rate
   changes.

5. **After logout, should every inner page (inventory, cart, checkout) be blocked
   when accessed by a direct URL?**
   *Assumption:* Yes — for security, a logged-out user who types an inner URL
   (inventory/cart) is redirected back to the login page with an error, because
   logging in is the first requirement before accessing any inner page. I test this
   by opening the inventory URL directly after logout.

6. **Is there a defined browser / mobile support matrix?**
   *Assumption:* The application should support the common desktop browsers (Chrome,
   Firefox, Edge) and should also remain usable in a mobile view. I verify the mobile
   layout by reducing the browser viewport to smaller dimensions and checking the
   page stays usable, and I include one mobile-viewport usability case.
