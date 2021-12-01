const scenarios = [
  {
    "title": "Storefront",
    "tests": [
      {
        "id": "Home",
        "title": "Home page"
      },
      {
        "id": "Products",
        "title": "Products page"
      },
      {
        "id": "Pdp",
        "title": "PDP page"
      },
      {
        "id": "Results",
        "title": "Results page"
      },
      {
        "id": "Cart",
        "title": "Cart page"
      },
      {
        "id": "Checkout",
        "title": "Checkout page"
      }
    ]
  },
  {
    "title": "Frontend API",
    "tests": [
      {
        "id": "DeleteCartsCartCodesApi",
        "title": "DeleteCartsCartCodesApi"
      },
      {
        "id": "UpdateCustomerPasswordApi",
        "title": "UpdateCustomerPasswordApi"
      },
      {
        "id": "DeleteCartsItemsApi",
        "title": "DeleteCartsItemsApi"
      },
      {
        "id": "UpdateCartsItemsApi",
        "title": "UpdateCartsItemsApi"
      },
      {
        "id": "DeleteCartsApi",
        "title": "DeleteCartsApi"
      },
      {
        "id": "UpdateCartsApi",
        "title": "UpdateCartsApi"
      },
      {
        "id": "UpdateCustomerRestorePasswordApi",
        "title": "UpdateCustomerRestorePasswordApi"
      },
      {
        "id": "DeleteCustomersApi",
        "title": "DeleteCustomersApi"
      },
      {
        "id": "UpdateCustomersApi",
        "title": "UpdateCustomersApi"
      },
      {
        "id": "DeleteRefreshTokensApi",
        "title": "DeleteRefreshTokensApi"
      },
      {
        "id": "CreateCheckoutTestingApi",
        "title": "CreateCheckoutTestingApi"
      },
      {
        "id": "CreateCartsFixturesApi",
        "title": "CreateCartsFixturesApi"
      },
      {
        "id": "CreateCustomerFixtureApi",
        "title": "CreateCustomerFixtureApi"
      },
      {
        "id": "CreateImportMerchant_posApi",
        "title": "CreateImportMerchant_posApi"
      },
      {
        "id": "CreateImportCategoriesApi",
        "title": "CreateImportCategoriesApi"
      },
      {
        "id": "CreateImportProduct_categoriesApi",
        "title": "CreateImportProduct_categoriesApi"
      },
      {
        "id": "CreateImportProduct_offersApi",
        "title": "CreateImportProduct_offersApi"
      },
      {
        "id": "CreateImportProduct_imagesApi",
        "title": "CreateImportProduct_imagesApi"
      },
      {
        "id": "CreateImportProduct_pricesApi",
        "title": "CreateImportProduct_pricesApi"
      },
      {
        "id": "CreateImportProductsApi",
        "title": "CreateImportProductsApi"
      },
      {
        "id": "CreateCodeAuthorizeApi",
        "title": "CreateCodeAuthorizeApi"
      },
      {
        "id": "CreateFirstDataNotificationsApi",
        "title": "CreateFirstDataNotificationsApi"
      },
      {
        "id": "CreateTokenApi",
        "title": "CreateTokenApi"
      },
      {
        "id": "CreateInstacartCallbacksApi",
        "title": "CreateInstacartCallbacksApi"
      },
      {
        "id": "CreateCartsMerchantTimeslotReservationsApi",
        "title": "CreateCartsMerchantTimeslotReservationsApi"
      },
      {
        "id": "CreateCartsCartCodesApi",
        "title": "CreateCartsCartCodesApi"
      },
      {
        "id": "CreateReturnsApi",
        "title": "CreateReturnsApi"
      },
      {
        "id": "CreateCheckoutApi",
        "title": "CreateCheckoutApi"
      },
      {
        "id": "CreateCheckoutDataApi",
        "title": "CreateCheckoutDataApi"
      },
      {
        "id": "CreateCustomersAddressesApi",
        "title": "CreateCustomersAddressesApi"
      },
      {
        "id": "CreateCartsItemsApi",
        "title": "CreateCartsItemsApi"
      },
      {
        "id": "CreateCartsApi",
        "title": "CreateCartsApi"
      },
      {
        "id": "CreateCustomerForgottenPasswordApi",
        "title": "CreateCustomerForgottenPasswordApi"
      },
      {
        "id": "CreateCustomersApi",
        "title": "CreateCustomersApi"
      },
      {
        "id": "CreateRefreshTokensApi",
        "title": "CreateRefreshTokensApi"
      },
      {
        "id": "CreateAccessTokensApi",
        "title": "CreateAccessTokensApi"
      },
      {
        "id": "GetCollectionOfCustomersPaymentMethodsApi",
        "title": "GetCollectionOfCustomersPaymentMethodsApi"
      },
      {
        "id": "GetCustomersPaymentMethodsApi",
        "title": "GetCustomersPaymentMethodsApi"
      },
      {
        "id": "GetCollectionOfConcreteProductsSalesUnitsApi",
        "title": "GetCollectionOfConcreteProductsSalesUnitsApi"
      },
      {
        "id": "GetProductMeasurementUnitsApi",
        "title": "GetProductMeasurementUnitsApi"
      },
      {
        "id": "GetCollectionOfCustomersOrdersApi",
        "title": "GetCollectionOfCustomersOrdersApi"
      },
      {
        "id": "GetCollectionOfCustomersCartsApi",
        "title": "GetCollectionOfCustomersCartsApi"
      },
      {
        "id": "GetCollectionOfPreviewDeliveryTimeslotsApi",
        "title": "GetCollectionOfPreviewDeliveryTimeslotsApi"
      },
      {
        "id": "GetCollectionOfProductOffersProductOfferPricesApi",
        "title": "GetCollectionOfProductOffersProductOfferPricesApi"
      },
      {
        "id": "GetCollectionOfMerchantCategoryTreesApi",
        "title": "GetCollectionOfMerchantCategoryTreesApi"
      },
      {
        "id": "GetCollectionOfMerchantsMerchantOpeningHoursApi",
        "title": "GetCollectionOfMerchantsMerchantOpeningHoursApi"
      },
      {
        "id": "GetCollectionOfMerchantSearchApi",
        "title": "GetCollectionOfMerchantSearchApi"
      },
      {
        "id": "GetCollectionOfProductOffersAlternativeProductOffersApi",
        "title": "GetCollectionOfProductOffersAlternativeProductOffersApi"
      },
      {
        "id": "GetCollectionOfCatalogSearchSuggestionsProductOffersApi",
        "title": "GetCollectionOfCatalogSearchSuggestionsProductOffersApi"
      },
      {
        "id": "GetCollectionOfCatalogSearchProductOffersApi",
        "title": "GetCollectionOfCatalogSearchProductOffersApi"
      },
      {
        "id": "GetCollectionOfCartsCartMerchantTimeslotsApi",
        "title": "GetCollectionOfCartsCartMerchantTimeslotsApi"
      },
      {
        "id": "GetCollectionOfPreviewPickupTimeslotsApi",
        "title": "GetCollectionOfPreviewPickupTimeslotsApi"
      },
      {
        "id": "GetCollectionOfConcreteProductsProductOffersApi",
        "title": "GetCollectionOfConcreteProductsProductOffersApi"
      },
      {
        "id": "GetProductOffersApi",
        "title": "GetProductOffersApi"
      },
      {
        "id": "GetCollectionOfMerchantsMerchantAddressesApi",
        "title": "GetCollectionOfMerchantsMerchantAddressesApi"
      },
      {
        "id": "GetCollectionOfMerchantsApi",
        "title": "GetCollectionOfMerchantsApi"
      },
      {
        "id": "GetMerchantsApi",
        "title": "GetMerchantsApi"
      },
      {
        "id": "GetCollectionOfReturnsApi",
        "title": "GetCollectionOfReturnsApi"
      },
      {
        "id": "GetReturnsApi",
        "title": "GetReturnsApi"
      },
      {
        "id": "GetCollectionOfReturnReasonsApi",
        "title": "GetCollectionOfReturnReasonsApi"
      },
      {
        "id": "GetHealthCheckApi",
        "title": "GetHealthCheckApi"
      },
      {
        "id": "GetCollectionOfCustomersAddressesApi",
        "title": "GetCollectionOfCustomersAddressesApi"
      },
      {
        "id": "GetCollectionOfOrdersApi",
        "title": "GetCollectionOfOrdersApi"
      },
      {
        "id": "GetOrdersApi",
        "title": "GetOrdersApi"
      },
      {
        "id": "GetCollectionOfConcreteProductsConcreteProductImageSetsApi",
        "title": "GetCollectionOfConcreteProductsConcreteProductImageSetsApi"
      },
      {
        "id": "GetCollectionOfCartsApi",
        "title": "GetCollectionOfCartsApi"
      },
      {
        "id": "GetCartsApi",
        "title": "GetCartsApi"
      },
      {
        "id": "GetCollectionOfConcreteProductsConcreteProductPricesApi",
        "title": "GetCollectionOfConcreteProductsConcreteProductPricesApi"
      },
      {
        "id": "GetConcreteProductsApi",
        "title": "GetConcreteProductsApi"
      },
      {
        "id": "GetCollectionOfCustomersApi",
        "title": "GetCollectionOfCustomersApi"
      },
      {
        "id": "GetCustomersApi",
        "title": "GetCustomersApi"
      },
      {
        "id": "GetCategoryNodesApi",
        "title": "GetCategoryNodesApi"
      },
      {
        "id": "GetCollectionOfCategoryTreesApi",
        "title": "GetCollectionOfCategoryTreesApi"
      },
      {
        "id": "GetCollectionOfConcreteProductsConcreteProductAvailabilitiesApi",
        "title": "GetCollectionOfConcreteProductsConcreteProductAvailabilitiesApi"
      },
      {
        "id": "GetStoresApi",
        "title": "GetStoresApi"
      },
      {
        "id": "GetCollectionOfStoresApi",
        "title": "Get Collection Of Stores Api"
      },
      {
        "id": "AddItemToCartFullFlowApi",
        "title": "[full flow]Add item to cart"
      },
      {
        "id": "AddItemToCartLessOrEqualFiftyItemsFullFlowApi",
        "title": "[full flow]Add Item To Cart with <= 50 Items"
      },
      {
        "id": "CheckoutFullFlowApi",
        "title": "[full flow]Checkout request"
      },
      {
        "id": "CheckoutFullFlowLessOrEqualFiftyItemsApi",
        "title": "[full flow]Checkout request with <= 50 items"
      },
      {
        "id": "CatalogSearchProductOffersApi",
        "title": "Catalog Search Product Offers"
      },
      {
        "id": "CatalogSearchSuggestionsProductOffersApi",
        "title": "Catalog Search Suggestions Product Offers"
      },
      {
        "id": "MerchantCategoryTreesApi",
        "title": "Merchant Category Trees"
      },
      {
        "id": "MerchantSearchApi",
        "title": "Merchant Search Request"
      },
      {
        "id": "CartsFullFlowApi",
        "title": "[full flow]Get Customer carts"
      },
      {
        "id": "CartByIdFullFlowApi",
        "title": "[full flow]Get Customer cart by id"
      },
      {
        "id": "CheckoutDataFullFlowApi",
        "title": "[full flow]Provide Checkout data request"
      },
      {
        "id": "CartMerchantTimeslotFullFlowApi",
        "title": "[full flow]Cart merchant timeslots request"
      },
      {
        "id": "UpdateCartItemFullFlowApi",
        "title": "[full flow]Update Cart Item request"
      },
      {
        "id": "UpdateCartFullFlowApi",
        "title": "[full flow]Update Cart request"
      },
      {
        "id": "PdpApi",
        "title": "PDP request"
      },
      {
        "id": "CheckoutApi",
        "title": "[load-test branch]Checkout request"
      },
      {
        "id": "CheckoutWithLessOrEqualFiftyItemsApi",
        "title": "[load-test branch]Checkout With Less Or Equal Fifty Items request"
      },
      {
        "id": "CheckoutWithFiftyToSeventyItemsApi",
        "title": "[load-test branch]Checkout With Fifty To Seventy Items request"
      },
      {
        "id": "CsvFixturesAddToCartApi",
        "title": "[load-test branch]Download Fixtures in Csv File for Add To Cart(1 item)"
      },
      {
        "id": "CsvFixturesAddToCartLessOrEqualFiftyItemsApi",
        "title": "[load-test branch]Download Fixtures in Csv File for Add To Cart <= 50 Items"
      },
      {
        "id": "CsvFixturesAddToCartFiftyToSeventyItemsApi",
        "title": "[load-test branch]Download Fixtures in Csv File for Add To Cart >= 50 & <= 70 Items"
      },
      {
        "id": "AddItemToCartApi",
        "title": "[load-test branch]Add item to cart"
      },
      {
        "id": "AddItemToCartWithLessOrEqualFiftyItemsApi",
        "title": "[load-test branch]Add Item To Cart with <= 50 items"
      },
      {
        "id": "AddItemToCartWithFiftyToSeventyItemsApi",
        "title": "[load-test branch]Add Item To Cart with >= 50 & <= 70 items"
      }
    ]
  },
  {
    "title": "Backend API",
    "tests": [
      {
        "id": "PickingOrdersBackendApi",
        "title": "Picking orders request"
      },
      {
        "id": "UserByIdBackendApi",
        "title": "User By ID request"
      },
      {
        "id": "TokenBackendApi",
        "title": "Token request"
      },
      {
        "id": "CodeAuthorizeBackendApi",
        "title": "Code Authorize request"
      },
      {
        "id": "GetProductsBackendApi",
        "title": "Get Products request"
      }
    ]
  }
]

module.exports = scenarios;