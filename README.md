# Load testing tool for Spryker
[![Build Status](https://github.com/spryker-sdk/load-testing/workflows/CI/badge.svg?branch=master)](https://github.com/spryker-sdk/load-testing/actions?query=workflow%3ACI+branch%3Amaster)
[![Minimum PHP Version](http://img.shields.io/badge/php-%3E%3D%207.3-8892BF.svg)](https://php.net/)
[![PHPStan](https://img.shields.io/badge/PHPStan-enabled-brightgreen.svg?style=flat)](https://github.com/phpstan/phpstan)

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


## Tests generator based on Swagger .yaml files
There's a tool for generating tests from Swagger .yaml files: for frontend API, backend API and data import API.

You can put as many as you want files into directories: `tests-generator/swagger-config/frontend-api`, `tests-generator/swagger-config/backend-api` and `tests-generator/swagger-config/data-import-api`.

You can provide default values to parameters/headers/body with `example` property.

Also, to make a chain of requests you can use `prepareDataSteps` property. It uses already defined classes with requests. For example:
```
prepareDataSteps:
    - CreateCustomer
    - CreateAccessToken
    - CreateCart
    - AddToCart
    - Checkout
```

From this example you can use `orderId` session variable in you next request in the chain because it will be saved to the gatling session from the class `CheckoutRequestApi`. All of them are stored in `resources/scenarios/spryker/requests`. You can create your own.

### Run
Front-end API:
`npm run frontend-api-tests:generate`

Back-end API:
`npm run backend-api-tests:generate `

Data import API:
`npm run data-import-api-tests:generate`


## Links
- [gatling.io](https://gatling.io)
