const scenarios = [
    {
        title: 'Storefront',
        tests: [
            { id: 'Home', title: 'Home page'},
            { id: 'Products', title: 'Products page'},
            { id: 'Pdp', title: 'PDP page'},
            { id: 'Results', title: 'Results page'},
            { id: 'Cart', title: 'Cart page'},
            { id: 'Checkout', title: 'Checkout page'},
        ],
    },
    {
        title: 'API',
        tests: [
            { id: 'CatalogSearchProductOffersApi', title: 'Catalog Search Product Offers'},
            { id: 'PdpApi', title: 'PDP request'},
            { id: 'CheckoutApi', title: 'Checkout request'},
            { id: 'CheckoutWithLessOrEqualFiftyItemsApi', title: 'Checkout With Less Or Equal Fifty Items request'},
            { id: 'CheckoutWithFiftyToSeventyItemsApi', title: 'Checkout With Fifty To Seventy Items request'},
            { id: 'CsvFixturesAddToCartApi', title: 'Download Fixtures in Csv File for Add To Cart(1 item)'},
            { id: 'CsvFixturesAddToCartLessOrEqualFiftyItemsApi', title: 'Download Fixtures in Csv File for Add To Cart <= 50 Items'},
            { id: 'CsvFixturesAddToCartFiftyToSeventyItemsApi', title: 'Download Fixtures in Csv File for Add To Cart >= 50 & <= 70 Items'},
            { id: 'AddItemToCartApi', title: 'Add item to cart'},
            { id: 'AddItemToCartWithLessOrEqualFiftyItemsApi', title: 'Add Item To Cart with <= 50 items'},
            { id: 'AddItemToCartWithFiftyToSeventyItemsApi', title: 'Add Item To Cart with >= 50 & <= 70 items'},
        ],
    },
];

module.exports = scenarios;
