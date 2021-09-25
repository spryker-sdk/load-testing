const scenarios = [
    {
        title: 'Storefront',
        tests: [
            { id: 'Home', title: 'Home page'},
            { id: 'Nope', title: 'Empty index'},
            { id: 'CatalogSearch', title: 'Search page'},
            { id: 'Pdp', title: 'PDP page'},
            { id: 'AddToGuestCart', title: 'Add to guest cart request'},
            { id: 'AddToCustomerCart', title: 'Add to customer cart request'},
            { id: 'PlaceOrder', title: 'Place order request'},
            { id: 'PlaceOrderCustomer', title: 'Place order customer request'},
        ],
    },
    {
        title: 'API',
        tests: [
            { id: 'CsvFixturesAddToCartApi', title: 'Download Fixtures in Csv File for Add To Cart(1 item)'},
            { id: 'CsvFixturesAddToCartLessOrEqualFiftyItemsApi', title: 'Download Fixtures in Csv File for Add To Cart <= 50 Items'},
            { id: 'CsvFixturesAddToCartFiftyToSeventyItemsApi', title: 'Download Fixtures in Csv File for Add To Cart >= 50 & <= 70 Items'},
            { id: 'AddItemToCartApi', title: 'Add item to cart'},
            { id: 'AddItemToCartWithLessOrEqualFiftyItemsApi', title: 'Add Item To Cart with <= 50 items'},
            { id: 'AddItemToCartWithFiftyToSeventyItemsApi', title: 'Add Item To Cart with >= 50 & <= 70 items'},
            { id: 'HomeApi', title: 'Home request'},
            { id: 'CatalogSearchProductOffersApi', title: 'Catalog Search Product Offers'},
            { id: 'ProductOffersForProductsConcreteApi', title: 'Product Offers For Products Concrete'},
            { id: 'CartApi', title: 'Add to cart request'},
            { id: 'GuestCartApi', title: 'Add to guest cart request'},
            { id: 'CatalogSearchApi', title: 'Search request'},
            { id: 'PdpApi', title: 'PDP request'},
            { id: 'GuestCheckoutApi', title: 'Guest checkout request'},
            { id: 'CheckoutApi', title: 'Checkout request'},
        ],
    },
];

module.exports = scenarios;
