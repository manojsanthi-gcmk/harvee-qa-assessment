# A1 – Automation Strategy + Biggest Risk

## Strategy (under 150 words)

**Automate at the UI layer:** the repeatable, deterministic, business-critical
flows that run on every build — login (standard + locked), product sorting (verified
across the whole list), add/remove from cart with badge checks, full checkout with
the total = item total + tax check, the missing-field errors (one parameterized
test), and the logged-out direct-URL security check.

**API / unit layer (if the client has one):** price and tax calculation and
input validation are best verified below the UI — faster and less brittle than
driving the browser.

**Keep manual:** usability, accessibility, mobile-layout look-and-feel, and
exploratory testing — these need human judgment and are poor automation candidates.

**Biggest remaining risk I would report:** the price/tax and sorting logic are
financial and ordering correctness issues; a wrong sort or total directly misleads
customers, so these deserve the highest priority and dedicated lower-layer coverage.
