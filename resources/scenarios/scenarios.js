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
        title: 'API [FULL FLOW]',
        tests: [
            { id: 'AddItemToCartFullFlowApi', title: 'Add item to cart'},
            { id: 'AddItemToCartLessOrEqualFiftyItemsFullFlowApi', title: 'Add Item To Cart with <= 50 Items'},
            { id: 'CheckoutFullFlowApi', title: 'Checkout request'},
            { id: 'CheckoutFullFlowLessOrEqualFiftyItemsApi', title: 'Checkout request with <= 50 items'},
            { id: 'CatalogSearchProductOffersApi', title: 'Catalog Search Product Offers'},
            { id: 'CatalogSearchSuggestionsProductOffersApi', title: 'Catalog Search Suggestions Product Offers'},
            { id: 'MerchantCategoryTreesApi', title: 'Merchant Category Trees'},
            { id: 'MerchantSearchApi', title: 'Merchant Search Request'},
            { id: 'CartsFullFlowApi', title: 'Get Customer carts'},
            { id: 'CartByIdFullFlowApi', title: 'Get Customer cart by id'},
            { id: 'CheckoutDataFullFlowApi', title: 'Provide Checkout data request'},
            { id: 'CartMerchantTimeslotFullFlowApi', title: 'Cart merchant timeslots request'},
            { id: 'UpdateCartItemFullFlowApi', title: 'Update Cart Item request'},
            { id: 'UpdateCartFullFlowApi', title: 'Update Cart request'},
            { id: 'PdpApi', title: 'PDP request'},
        ],
    },
    {
        title: 'API [ONLY LOAD-TEST BRANCH]',
        tests: [
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
