# Load testing tool for Spryker

The tool contains predefined test scenarios that are specific to Spryker.
Test runs based on Gatling.io, an open-source tool.

Web UI helps to manage runs. Multiple target projects are supported simultaneously.

The tool can be used as a package integrated into the Spryker project or a standalone package.

## Prerequisites
- Java 8+
- Node 10.10+

## Table of content
- [Gatling](docs/load-testing-with-gatling/1-gatling-overview.md)
- [Installing Gatling](docs/load-testing-with-gatling/2-installing-gatling.md)
- [Running and using Gatling](docs/load-testing-with-gatling/3-running-and-using-gatling.md)

## Available test

For *Yves*:
- `Home` - request to the Home page
- `Nope` - empty request
- `AddToCustomerCart` - scenario to add a random product from fixtures to a user cart
- `AddToGuestCart` - scenario to add a random product from fixtures to a guest cart
- `CatalogSearch` - search request for a random product from fixtures
- `Pdp` - request a random product detail page from fixtures
- `PlaceOrder` - request to place an order
- `PlaceOrderCustomer` - scenario to place an order

For *Glue API*:
- `CatalogSearchApi` - search request for a random product from fixtures
- `CheckoutApi` - scenario to checkout for logged user
- `GuestCheckoutApi` - scenario to checkout for guest user
- `CartApi` - scenario to add a product to the cart for logged user
- `GuestCartApi` - scenario to add a product to cart for guest user
- `PdpApi` - request a random product detail page from fixtures


## Links
- [gatling.io](https://gatling.io)
