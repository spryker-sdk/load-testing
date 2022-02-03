const scenarios = [
    {
        title: 'Storefront',
        status: 'inactive',
        tests: [
            { id: 'Home', title: 'Home page', route: '', requestType: ''},
            { id: 'Products', title: 'Products page', route: '', requestType: ''},
            { id: 'Pdp', title: 'PDP page', route: '', requestType: ''},
            { id: 'Results', title: 'Results page', route: '', requestType: ''},
            { id: 'Cart', title: 'Cart page', route: '', requestType: ''},
            { id: 'Checkout', title: 'Checkout page', route: '', requestType: ''},
        ],
    },
    {
        title: 'Frontend API',
        status: 'active',
        tests: [
            { id: 'UpdateCustomerPasswordFrontendApi', title: 'UpdateCustomerPasswordFrontendApi', route: 'customer-password', requestType: 'patch'},
            { id: 'CreateAccessTokensFrontendApi', title: 'CreateAccessTokensFrontendApi', route: 'access-tokens', requestType: 'post'},
            { id: 'CreateRefreshTokensFrontendApi', title: 'CreateRefreshTokensFrontendApi', route: '', requestType: ''},
            { id: 'CreateCustomerForgottenPasswordFrontendApi', title: 'CreateCustomerForgottenPasswordFrontendApi', route: '', requestType: ''},
            { id: 'DeleteCustomersFrontendApi', title: 'DeleteCustomersFrontendApi', route: '', requestType: ''},
            { id: 'CreateCustomersFrontendApi', title: 'CreateCustomersFrontendApi', route: '', requestType: ''},
            { id: 'CreateCartsFrontendApi', title: 'CreateCartsFrontendApi', route: '', requestType: ''},
            { id: 'CreateCheckoutDataFrontendApi', title: '[full flow]CreateCheckoutDataFrontendApi', route: '', requestType: ''},
            { id: 'CreateCheckoutFrontendApi', title: '[full flow]Checkout request', route: '', requestType: ''},
            { id: 'CreateCheckoutFiftyItemsFrontendApi', title: '[full flow]Checkout request with 50 items', route: '', requestType: ''},
            { id: 'CreateCheckoutSeventyItemsFrontendApi', title: '[full flow]Checkout request with 70 items', route: '', requestType: ''},
            { id: 'CreateInstacartCallbacksFrontendApi', title: 'CreateInstacartCallbacksFrontendApi', route: '', requestType: ''},
            { id: 'AddItemToCartFullFlowApi', title: '[full flow]Add item to cart', route: '', requestType: ''},
            { id: 'CreateCartMerchantTimeslotReservationsFrontendApi', title: 'CreateCartMerchantTimeslotReservationsFrontendApi', route: '', requestType: ''},

            { id: 'GetCollectionOfStoresFrontendApi', title: 'GetCollectionOfStoresFrontendApi', route: '', requestType: ''},
            { id: 'GetStoresFrontendApi', title: 'GetStoresFrontendApi', route: '', requestType: ''},
            { id: 'GetCollectionOfCategoryTreesFrontendApi', title: 'GetCollectionOfCategoryTreesFrontendApi', route: '', requestType: ''},
            { id: 'GetCategoryNodesFrontendApi', title: 'Get Category Nodes', route: '', requestType: ''},
            { id: 'GetCustomersFrontendApi', title: 'GetCustomersFrontendApi', route: '', requestType: ''},
            { id: 'GetCollectionOfCustomersFrontendApi', title: 'GetCollectionOfCustomersFrontendApi', route: '', requestType: ''},
            { id: 'PdpApi', title: 'PDP request', route: '', requestType: ''},
            { id: 'CartByIdFullFlowApi', title: '[full flow]Get Customer cart by id', route: '', requestType: ''},
            { id: 'GetCollectionOfConcreteProductsConcreteProductImageSetsFrontendApi', title: 'GetCollectionOfConcreteProductsConcreteProductImageSetsFrontendApi', route: '', requestType: ''},
            { id: 'GetOrdersFrontendApi', title: '[full flow]GetOrdersFrontendApi', route: '', requestType: ''},
            { id: 'GetCollectionOfHealthCheckServicesFrontendApi', title: 'GetCollectionOfHealthCheckServicesFrontendApi', route: '', requestType: ''},
            { id: 'GetMerchantsFrontendApi', title: 'GetMerchantsFrontendApi', route: '', requestType: ''},
            { id: 'GetCollectionOfMerchantsFrontendApi', title: 'GetCollectionOfMerchantsFrontendApi', route: '', requestType: ''},
            { id: 'GetCollectionOfMerchantsMerchantAddressesFrontendApi', title: 'GetCollectionOfMerchantsMerchantAddressesFrontendApi', route: '', requestType: ''},
            { id: 'GetProductOffersFrontendApi', title: 'GetProductOffersFrontendApi', route: '', requestType: ''},
            { id: 'GetCollectionOfConcreteProductsProductOffersFrontendApi', title: 'GetCollectionOfConcreteProductsProductOffersFrontendApi', route: '', requestType: ''},
            { id: 'GetCollectionOfPreviewPickupTimeslotsFrontendApi', title: 'GetCollectionOfPreviewPickupTimeslotsFrontendApi', route: '', requestType: ''},
            { id: 'GetCollectionOfCartsCartMerchantTimeslotsFrontendApi', title: 'GetCollectionOfCartsCartMerchantTimeslotsFrontendApi', route: '', requestType: ''},
            { id: 'GetCollectionOfCartsDeliveryTimeslotsFrontendApi', title: 'GetCollectionOfCartsDeliveryTimeslotsFrontendApi', route: '', requestType: ''},
            { id: 'GetCollectionOfCatalogSearchProductOffersFrontendApi', title: 'GetCollectionOfCatalogSearchProductOffersFrontendApi', route: '', requestType: ''},
            { id: 'GetCollectionOfCatalogSearchSuggestionsProductOffersFrontendApi', title: 'GetCollectionOfCatalogSearchSuggestionsProductOffersFrontendApi', route: '', requestType: ''},
            // { id: 'GetCollectionOfProductOffersAlternativeProductOffersFrontendApi', title: 'GetCollectionOfProductOffersAlternativeProductOffersFrontendApi', route: '', requestType: ''},
            { id: 'MerchantSearchApi', title: 'Merchant Search Request', route: '', requestType: ''},
            { id: 'GetCollectionOfMerchantsMerchantOpeningHoursFrontendApi', title: 'GetCollectionOfMerchantsMerchantOpeningHoursFrontendApi', route: '', requestType: ''},
            { id: 'GetCollectionOfMerchantCategoryTreesFrontendApi', title: 'GetCollectionOfMerchantCategoryTreesFrontendApi', route: '', requestType: ''},
            { id: 'GetCollectionOfProductOffersProductOfferPricesFrontendApi', title: 'GetCollectionOfProductOffersProductOfferPricesFrontendApi', route: '', requestType: ''},
            { id: 'GetCollectionOfPreviewDeliveryTimeslotsFrontendApi', title: 'GetCollectionOfPreviewDeliveryTimeslotsFrontendApi', route: '', requestType: ''},
            { id: 'GetCollectionOfCustomersCartsFrontendApi', title: 'GetCollectionOfCustomersCartsFrontendApi', route: '', requestType: ''},
            { id: 'GetCollectionOfCustomersOrdersFrontendApi', title: 'GetCollectionOfCustomersOrdersFrontendApi', route: '', requestType: ''},
            { id: 'GetProductMeasurementUnitsFrontendApi', title: 'GetProductMeasurementUnitsFrontendApi', route: '', requestType: ''},
            { id: 'GetCollectionOfConcreteProductsSalesUnitsFrontendApi', title: 'GetCollectionOfConcreteProductsSalesUnitsFrontendApi', route: '', requestType: ''},
            { id: 'GetCustomerPaymentMethodsFrontendApi', title: 'GetCustomerPaymentMethodsFrontendApi', route: '', requestType: ''},
            { id: 'GetCollectionOfCustomerPaymentMethodsFrontendApi', title: 'GetCollectionOfCustomerPaymentMethodsFrontendApi', route: '', requestType: ''},

            { id: 'UpdateCustomersFrontendApi', title: 'UpdateCustomersFrontendApi', route: '', requestType: ''},
            { id: 'UpdateCartsItemsFrontendApi', title: '[full flow]UpdateCartsItemsFrontendApi', route: '', requestType: ''},
            // { id: 'UpdateCartItemFullFlowApi', title: '[full flow]Update Cart Item request', route: '', requestType: ''},
            { id: 'UpdateCartFullFlowApi', title: '[full flow]Update Cart request', route: '', requestType: ''},
            { id: 'DeleteCartsItemsFrontendApi', title: 'Delete Carts Items', route: '', requestType: ''},
            { id: 'DeleteRefreshTokensFrontendApi', title: 'DeleteRefreshTokensFrontendApi', route: '', requestType: ''},
            { id: 'DeleteCustomerPaymentMethodsFrontendApi', title: 'DeleteCustomerPaymentMethodsFrontendApi', route: '', requestType: ''},

            { id: 'ImportMerchantsApi', title: 'Import Merchants', route: '', requestType: ''},
            { id: 'ImportCategoriesApi', title: 'Import Categories', route: '', requestType: ''},
            { id: 'ImportProductsApi', title: 'Import Products', route: '', requestType: ''},
            { id: 'ImportProductImagesApi', title: 'Import Product Images', route: '', requestType: ''},
            { id: 'ImportProductPricesApi', title: 'Import Product Prices', route: '', requestType: ''},
            { id: 'ImportProductOffersApi', title: 'Import Product Offers', route: '', requestType: ''},
            { id: 'ImportProductCategoriesApi', title: 'Import Product Categories', route: '', requestType: ''},
            { id: 'ImportHealthCheckApi', title: 'Import Health check', route: '', requestType: ''},

            // { id: 'CheckoutFullFlowLessOrEqualFiftyItemsApi', title: '[full flow]Checkout request with <= 50 items', route: '', requestType: ''},
            // { id: 'CatalogSearchProductOffersApi', title: 'Catalog Search Product Offers', route: '', requestType: ''},
            // { id: 'CatalogSearchSuggestionsProductOffersApi', title: 'Catalog Search Suggestions Product Offers', route: '', requestType: ''},
            // { id: 'MerchantCategoryTreesApi', title: 'Merchant Category Trees', route: '', requestType: ''},

            // { id: 'CartsFullFlowApi', title: '[full flow]Get Customer carts', route: '', requestType: ''},
            // { id: 'CartMerchantTimeslotFullFlowApi', title: '[full flow]Cart merchant timeslots request', route: '', requestType: ''},

            { id: 'CheckoutApi', title: '[load-test branch]Checkout request', route: '', requestType: ''},
            { id: 'CheckoutWithLessOrEqualFiftyItemsApi', title: '[load-test branch]Checkout With Less Or Equal Fifty Items request', route: '', requestType: ''},
            { id: 'CheckoutWithFiftyToSeventyItemsApi', title: '[load-test branch]Checkout With Fifty To Seventy Items request', route: '', requestType: ''},
            { id: 'CsvFixturesAddToCartApi', title: '[load-test branch]Download Fixtures in Csv File for Add To Cart(1 item)', route: '', requestType: ''},
            { id: 'CsvFixturesAddToCartLessOrEqualFiftyItemsApi', title: '[load-test branch]Download Fixtures in Csv File for Add To Cart <= 50 Items', route: '', requestType: ''},
            { id: 'CsvFixturesAddToCartFiftyToSeventyItemsApi', title: '[load-test branch]Download Fixtures in Csv File for Add To Cart >= 50 & <= 70 Items', route: '', requestType: ''},
            { id: 'AddItemToCartApi', title: '[load-test branch]Add item to cart', route: '', requestType: ''},
            { id: 'AddItemToCartWithLessOrEqualFiftyItemsApi', title: '[load-test branch]Add Item To Cart with <= 50 items', route: '', requestType: ''},
            { id: 'AddItemToCartWithFiftyToSeventyItemsApi', title: '[load-test branch]Add Item To Cart with >= 50 & <= 70 items', route: '', requestType: ''},
        ],
    },
    {
        title: 'Backend API',
        status: 'active',
        tests: [
            { id: 'TokenBackendApi', title: 'Token request', route: '', requestType: ''},
            { id: 'CodeAuthorizeBackendApi', title: 'Code Authorize request', route: '', requestType: ''},
        ],
    },
];

module.exports = scenarios;
