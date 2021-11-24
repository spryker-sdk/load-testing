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
        title: 'Frontend API',
        tests: [
            { id: 'AddItemToCartFullFlowApi', title: '[full flow]Add item to cart'},
            { id: 'AddItemToCartLessOrEqualFiftyItemsFullFlowApi', title: '[full flow]Add Item To Cart with <= 50 Items'},
            { id: 'CheckoutFullFlowApi', title: '[full flow]Checkout request'},
            { id: 'CheckoutFullFlowLessOrEqualFiftyItemsApi', title: '[full flow]Checkout request with <= 50 items'},
            { id: 'CatalogSearchProductOffersApi', title: 'Catalog Search Product Offers'},
            { id: 'CatalogSearchSuggestionsProductOffersApi', title: 'Catalog Search Suggestions Product Offers'},
            { id: 'MerchantCategoryTreesApi', title: 'Merchant Category Trees'},
            { id: 'MerchantSearchApi', title: 'Merchant Search Request'},
            { id: 'CartsFullFlowApi', title: '[full flow]Get Customer carts'},
            { id: 'CartByIdFullFlowApi', title: '[full flow]Get Customer cart by id'},
            { id: 'CheckoutDataFullFlowApi', title: '[full flow]Provide Checkout data request'},
            { id: 'CartMerchantTimeslotFullFlowApi', title: '[full flow]Cart merchant timeslots request'},
            { id: 'UpdateCartItemFullFlowApi', title: '[full flow]Update Cart Item request'},
            { id: 'UpdateCartFullFlowApi', title: '[full flow]Update Cart request'},
            { id: 'PdpApi', title: 'PDP request'},
            { id: 'CheckoutApi', title: '[load-test branch]Checkout request'},
            { id: 'CheckoutWithLessOrEqualFiftyItemsApi', title: '[load-test branch]Checkout With Less Or Equal Fifty Items request'},
            { id: 'CheckoutWithFiftyToSeventyItemsApi', title: '[load-test branch]Checkout With Fifty To Seventy Items request'},
            { id: 'CsvFixturesAddToCartApi', title: '[load-test branch]Download Fixtures in Csv File for Add To Cart(1 item)'},
            { id: 'CsvFixturesAddToCartLessOrEqualFiftyItemsApi', title: '[load-test branch]Download Fixtures in Csv File for Add To Cart <= 50 Items'},
            { id: 'CsvFixturesAddToCartFiftyToSeventyItemsApi', title: '[load-test branch]Download Fixtures in Csv File for Add To Cart >= 50 & <= 70 Items'},
            { id: 'AddItemToCartApi', title: '[load-test branch]Add item to cart'},
            { id: 'AddItemToCartWithLessOrEqualFiftyItemsApi', title: '[load-test branch]Add Item To Cart with <= 50 items'},
            { id: 'AddItemToCartWithFiftyToSeventyItemsApi', title: '[load-test branch]Add Item To Cart with >= 50 & <= 70 items'},
        ],
    },
    {
        title: 'Backend API',
        tests: [
            { id: 'PickingOrdersBackendApi', title: 'Picking orders request'},
            { id: 'UserByIdBackendApi', title: 'User By ID request'},
            { id: 'TokenBackendApi', title: 'Token request'},
            { id: 'CodeAuthorizeBackendApi', title: 'Code Authorize request'},
            { id: 'GetProductsBackendApi', title: 'Get Products request'},
        ],
    },
];

module.exports = scenarios;
