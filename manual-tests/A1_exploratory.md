# A1 – Exploratory Testing Charter (15 minutes)

**Charter:** Explore the `problem_user` account across the shop-and-checkout flow to
discover UI and functional anomalies that scripted cases might miss.

- **Account:** `problem_user` / `secret_sauce`
- **Time box:** 15 minutes
- **Areas:** login, product images/names, add/remove-to-cart button states, cart
  contents, checkout form.

## What to look for
- Product **images** – are they all correct, or duplicated/wrong?
- **Add to cart / Remove** button states – does the button update correctly, or get
  stuck / affect the wrong item?
- **Cart integrity** – do the items in the cart match what was added?
- **Checkout form** – do the first name / last name / postal fields accept and submit
  as expected, or behave oddly?
- **Sorting** – does the sort order actually change?

## Notes / observations (found during the session)

1. **Wrong product images** – after logging in as problem_user, all product images
   show a dog photo instead of the actual product images.
2. **Remove button not working (first product)** – trying to remove the first product
   from the Products page does nothing; the button does not respond.
3. **Cannot remove after adding** – once a new product is added to the cart, it cannot
   be removed.
4. **Last name field broken on checkout** – on the "Your Information" page you cannot
   type letters in the Last Name field; attempting to type in Last Name also fully
   erases what was entered in the First Name field.
5. **Sorting completely broken** – in the sort container none of the filters work:
   Price (low→high), Price (high→low), Name (A→Z) and Name (Z→A) all fail to sort.

## Follow-up
Each confirmed anomaly is turned into a scripted test case with steps, expected vs
actual and a severity, then filed as a defect. The most severe here are the broken
cart (add/remove) and broken sorting, which block the core shop-and-checkout flow.
