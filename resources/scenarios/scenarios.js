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
        "id": "CreateCartsItemsFrontendApi",
        "title": "CreateCartsItemsFrontendApi"
      },
      {
        "id": "GetStoresFrontendApi",
        "title": "GetStoresFrontendApi"
      },
      {
        "id": "CreateCartsFrontendApi",
        "title": "CreateCartsFrontendApi"
      },
      {
        "id": "GetCollectionOfCartsFrontendApi",
        "title": "GetCollectionOfCartsFrontendApi"
      },
      {
        "id": "DeleteCartsFrontendApi",
        "title": "DeleteCartsFrontendApi"
      },
      {
        "id": "UpdateCartsFrontendApi",
        "title": "UpdateCartsFrontendApi"
      },
      {
        "id": "GetCartsFrontendApi",
        "title": "GetCartsFrontendApi"
      },
      {
        "id": "GetCollectionOfConcreteProductsConcreteProductPricesFrontendApi",
        "title": "GetCollectionOfConcreteProductsConcreteProductPricesFrontendApi"
      },
      {
        "id": "GetConcreteProductsFrontendApi",
        "title": "GetConcreteProductsFrontendApi"
      },
      {
        "id": "UpdateCustomerRestorePasswordFrontendApi",
        "title": "UpdateCustomerRestorePasswordFrontendApi"
      },
      {
        "id": "CreateCustomerForgottenPasswordFrontendApi",
        "title": "CreateCustomerForgottenPasswordFrontendApi"
      },
      {
        "id": "GetCollectionOfCustomersFrontendApi",
        "title": "GetCollectionOfCustomersFrontendApi"
      },
      {
        "id": "DeleteCustomersFrontendApi",
        "title": "DeleteCustomersFrontendApi"
      },
      {
        "id": "UpdateCustomersFrontendApi",
        "title": "UpdateCustomersFrontendApi"
      },
      {
        "id": "GetCustomersFrontendApi",
        "title": "GetCustomersFrontendApi"
      },
      {
        "id": "GetCategoryNodesFrontendApi",
        "title": "GetCategoryNodesFrontendApi"
      },
      {
        "id": "GetCollectionOfCategoryTreesFrontendApi",
        "title": "GetCollectionOfCategoryTreesFrontendApi"
      },
      {
        "id": "GetCollectionOfConcreteProductsConcreteProductAvailabilitiesFrontendApi",
        "title": "GetCollectionOfConcreteProductsConcreteProductAvailabilitiesFrontendApi"
      },
      {
        "id": "GetCollectionOfStoresFrontendApi",
        "title": "GetCollectionOfStoresFrontendApi"
      },
      {
        "id": "CreateCheckoutTestingFrontendApi",
        "title": "CreateCheckoutTestingFrontendApi"
      },
      {
        "id": "CreateCartsFixturesFrontendApi",
        "title": "CreateCartsFixturesFrontendApi"
      },
      {
        "id": "CreateCustomerFixtureFrontendApi",
        "title": "CreateCustomerFixtureFrontendApi"
      },
      {
        "id": "GetCollectionOfCustomersPaymentMethodsFrontendApi",
        "title": "GetCollectionOfCustomersPaymentMethodsFrontendApi"
      },
      {
        "id": "GetCustomersPaymentMethodsFrontendApi",
        "title": "GetCustomersPaymentMethodsFrontendApi"
      },
      {
        "id": "CreateImportMerchantPosFrontendApi",
        "title": "CreateImportMerchantPosFrontendApi"
      },
      {
        "id": "CreateImportCategoriesFrontendApi",
        "title": "CreateImportCategoriesFrontendApi"
      },
      {
        "id": "CreateImportProductCategoriesFrontendApi",
        "title": "CreateImportProductCategoriesFrontendApi"
      },
      {
        "id": "CreateImportProductOffersFrontendApi",
        "title": "CreateImportProductOffersFrontendApi"
      },
      {
        "id": "CreateImportProductImagesFrontendApi",
        "title": "CreateImportProductImagesFrontendApi"
      },
      {
        "id": "CreateImportProductPricesFrontendApi",
        "title": "CreateImportProductPricesFrontendApi"
      },
      {
        "id": "CreateImportProductsFrontendApi",
        "title": "CreateImportProductsFrontendApi"
      },
      {
        "id": "GetCollectionOfConcreteProductsSalesUnitsFrontendApi",
        "title": "GetCollectionOfConcreteProductsSalesUnitsFrontendApi"
      },
      {
        "id": "GetProductMeasurementUnitsFrontendApi",
        "title": "GetProductMeasurementUnitsFrontendApi"
      },
      {
        "id": "GetCollectionOfCustomersOrdersFrontendApi",
        "title": "GetCollectionOfCustomersOrdersFrontendApi"
      },
      {
        "id": "GetCollectionOfCustomersCartsFrontendApi",
        "title": "GetCollectionOfCustomersCartsFrontendApi"
      },
      {
        "id": "CreateCodeAuthorizeFrontendApi",
        "title": "CreateCodeAuthorizeFrontendApi"
      },
      {
        "id": "CreateFirstDataNotificationsFrontendApi",
        "title": "CreateFirstDataNotificationsFrontendApi"
      },
      {
        "id": "CreateTokenFrontendApi",
        "title": "CreateTokenFrontendApi"
      },
      {
        "id": "GetCollectionOfPreviewDeliveryTimeslotsFrontendApi",
        "title": "GetCollectionOfPreviewDeliveryTimeslotsFrontendApi"
      },
      {
        "id": "GetCollectionOfProductOffersProductOfferPricesFrontendApi",
        "title": "GetCollectionOfProductOffersProductOfferPricesFrontendApi"
      },
      {
        "id": "GetCollectionOfMerchantCategoryTreesFrontendApi",
        "title": "GetCollectionOfMerchantCategoryTreesFrontendApi"
      },
      {
        "id": "CreateInstacartCallbacksFrontendApi",
        "title": "CreateInstacartCallbacksFrontendApi"
      },
      {
        "id": "GetCollectionOfMerchantsMerchantOpeningHoursFrontendApi",
        "title": "GetCollectionOfMerchantsMerchantOpeningHoursFrontendApi"
      },
      {
        "id": "GetCollectionOfMerchantSearchFrontendApi",
        "title": "GetCollectionOfMerchantSearchFrontendApi"
      },
      {
        "id": "GetCollectionOfProductOffersAlternativeProductOffersFrontendApi",
        "title": "GetCollectionOfProductOffersAlternativeProductOffersFrontendApi"
      },
      {
        "id": "GetCollectionOfCatalogSearchSuggestionsProductOffersFrontendApi",
        "title": "GetCollectionOfCatalogSearchSuggestionsProductOffersFrontendApi"
      },
      {
        "id": "GetCollectionOfCatalogSearchProductOffersFrontendApi",
        "title": "GetCollectionOfCatalogSearchProductOffersFrontendApi"
      },
      {
        "id": "GetCollectionOfCartsCartMerchantTimeslotsFrontendApi",
        "title": "GetCollectionOfCartsCartMerchantTimeslotsFrontendApi"
      },
      {
        "id": "CreateCartsMerchantTimeslotReservationsFrontendApi",
        "title": "CreateCartsMerchantTimeslotReservationsFrontendApi"
      },
      {
        "id": "GetCollectionOfPreviewPickupTimeslotsFrontendApi",
        "title": "GetCollectionOfPreviewPickupTimeslotsFrontendApi"
      },
      {
        "id": "GetCollectionOfConcreteProductsProductOffersFrontendApi",
        "title": "GetCollectionOfConcreteProductsProductOffersFrontendApi"
      },
      {
        "id": "GetProductOffersFrontendApi",
        "title": "GetProductOffersFrontendApi"
      },
      {
        "id": "GetCollectionOfMerchantsMerchantAddressesFrontendApi",
        "title": "GetCollectionOfMerchantsMerchantAddressesFrontendApi"
      },
      {
        "id": "GetCollectionOfMerchantsFrontendApi",
        "title": "GetCollectionOfMerchantsFrontendApi"
      },
      {
        "id": "GetMerchantsFrontendApi",
        "title": "GetMerchantsFrontendApi"
      },
      {
        "id": "DeleteCartsCartCodesFrontendApi",
        "title": "DeleteCartsCartCodesFrontendApi"
      },
      {
        "id": "CreateCartsCartCodesFrontendApi",
        "title": "CreateCartsCartCodesFrontendApi"
      },
      {
        "id": "CreateReturnsFrontendApi",
        "title": "CreateReturnsFrontendApi"
      },
      {
        "id": "GetCollectionOfReturnsFrontendApi",
        "title": "GetCollectionOfReturnsFrontendApi"
      },
      {
        "id": "GetReturnsFrontendApi",
        "title": "GetReturnsFrontendApi"
      },
      {
        "id": "GetCollectionOfReturnReasonsFrontendApi",
        "title": "GetCollectionOfReturnReasonsFrontendApi"
      },
      {
        "id": "GetHealthCheckFrontendApi",
        "title": "GetHealthCheckFrontendApi"
      },
      {
        "id": "CreateCheckoutFrontendApi",
        "title": "CreateCheckoutFrontendApi"
      },
      {
        "id": "CreateCheckoutDataFrontendApi",
        "title": "CreateCheckoutDataFrontendApi"
      },
      {
        "id": "CreateCustomersAddressesFrontendApi",
        "title": "CreateCustomersAddressesFrontendApi"
      },
      {
        "id": "GetCollectionOfCustomersAddressesFrontendApi",
        "title": "GetCollectionOfCustomersAddressesFrontendApi"
      },
      {
        "id": "UpdateCustomerPasswordFrontendApi",
        "title": "UpdateCustomerPasswordFrontendApi"
      },
      {
        "id": "GetCollectionOfOrdersFrontendApi",
        "title": "GetCollectionOfOrdersFrontendApi"
      },
      {
        "id": "GetOrdersFrontendApi",
        "title": "GetOrdersFrontendApi"
      },
      {
        "id": "GetCollectionOfConcreteProductsConcreteProductImageSetsFrontendApi",
        "title": "GetCollectionOfConcreteProductsConcreteProductImageSetsFrontendApi"
      },
      {
        "id": "DeleteCartsItemsFrontendApi",
        "title": "DeleteCartsItemsFrontendApi"
      },
      {
        "id": "UpdateCartsItemsFrontendApi",
        "title": "UpdateCartsItemsFrontendApi"
      },
      {
        "id": "CreateCustomersFrontendApi",
        "title": "CreateCustomersFrontendApi"
      },
      {
        "id": "DeleteRefreshTokensFrontendApi",
        "title": "DeleteRefreshTokensFrontendApi"
      },
      {
        "id": "CreateRefreshTokensFrontendApi",
        "title": "CreateRefreshTokensFrontendApi"
      },
      {
        "id": "CreateAccessTokensFrontendApi",
        "title": "CreateAccessTokensFrontendApi"
      },
      {
        "id": "GetUsersBackendApi",
        "title": "GetUsersBackendApi"
      },
      {
        "id": "CreateDeviceTokensBackendApi",
        "title": "CreateDeviceTokensBackendApi"
      },
      {
        "id": "GetCollectionOfOrdersBackendApi",
        "title": "GetCollectionOfOrdersBackendApi"
      },
      {
        "id": "CreateCodeAuthorizeBackendApi",
        "title": "CreateCodeAuthorizeBackendApi"
      },
      {
        "id": "CreateAccessTokensBackendApi",
        "title": "CreateAccessTokensBackendApi"
      },
      {
        "id": "CreateRabbitmqImportProductOfferDataImportApi",
        "title": "CreateRabbitmqImportProductOfferDataImportApi"
      },
      {
        "id": "CreateImportProductCategoriesDataImportApi",
        "title": "CreateImportProductCategoriesDataImportApi"
      },
      {
        "id": "CreateImportCategoriesDataImportApi",
        "title": "CreateImportCategoriesDataImportApi"
      },
      {
        "id": "GetEmailExportRefundFinanceDataImportApi",
        "title": "GetEmailExportRefundFinanceDataImportApi"
      },
      {
        "id": "GetEmailReconciliationOrderItemDataImportApi",
        "title": "GetEmailReconciliationOrderItemDataImportApi"
      },
      {
        "id": "GetEmailReconciliationOrderDataImportApi",
        "title": "GetEmailReconciliationOrderDataImportApi"
      },
      {
        "id": "GetRabbitmqExportOrderDataImportApi",
        "title": "GetRabbitmqExportOrderDataImportApi"
      },
      {
        "id": "CreateImportMerchantPosDataImportApi",
        "title": "CreateImportMerchantPosDataImportApi"
      },
      {
        "id": "CreateImportProductPricesDataImportApi",
        "title": "CreateImportProductPricesDataImportApi"
      },
      {
        "id": "CreateImportProductImagesDataImportApi",
        "title": "CreateImportProductImagesDataImportApi"
      },
      {
        "id": "CreateImportProductsDataImportApi",
        "title": "CreateImportProductsDataImportApi"
      },
      {
        "id": "CreateRabbitmqImportProductOfferApi",
        "title": "CreateRabbitmqImportProductOfferApi"
      },
      {
        "id": "CreateImportProductCategoriesApi",
        "title": "CreateImportProductCategoriesApi"
      },
      {
        "id": "GetEmailExportRefundFinanceApi",
        "title": "GetEmailExportRefundFinanceApi"
      },
      {
        "id": "GetEmailReconciliationOrderItemApi",
        "title": "GetEmailReconciliationOrderItemApi"
      },
      {
        "id": "GetEmailReconciliationOrderApi",
        "title": "GetEmailReconciliationOrderApi"
      },
      {
        "id": "GetRabbitmqExportOrderApi",
        "title": "GetRabbitmqExportOrderApi"
      },
      {
        "id": "CreateImportMerchantPosApi",
        "title": "CreateImportMerchantPosApi"
      },
      {
        "id": "CreateImportProductPricesApi",
        "title": "CreateImportProductPricesApi"
      },
      {
        "id": "CreateImportProductImagesApi",
        "title": "CreateImportProductImagesApi"
      },
      {
        "id": "CreaterabbitmqimportProductOfferApi",
        "title": "CreaterabbitmqimportProductOfferApi"
      },
      {
        "id": "GetemailexportRefundFinanceApi",
        "title": "GetemailexportRefundFinanceApi"
      },
      {
        "id": "GetemailreconciliationOrderItemApi",
        "title": "GetemailreconciliationOrderItemApi"
      },
      {
        "id": "GetemailreconciliationOrderApi",
        "title": "GetemailreconciliationOrderApi"
      },
      {
        "id": "GetrabbitmqexportOrderApi",
        "title": "GetrabbitmqexportOrderApi"
      },
      {
        "id": "CreateIimportMerchantPosApi",
        "title": "CreateIimportMerchantPosApi"
      },
      {
        "id": "CreateIimportProductPricesApi",
        "title": "CreateIimportProductPricesApi"
      },
      {
        "id": "CreateIimportProductImagesApi",
        "title": "CreateIimportProductImagesApi"
      },
      {
        "id": "CreateIimportProductsApi",
        "title": "CreateIimportProductsApi"
      },
      {
        "id": "CreaterabbitmqimportproductOfferApi",
        "title": "CreaterabbitmqimportproductOfferApi"
      },
      {
        "id": "CreateimportProductCategoriesApi",
        "title": "CreateimportProductCategoriesApi"
      },
      {
        "id": "GetemailreconciliationorderItemApi",
        "title": "GetemailreconciliationorderItemApi"
      },
      {
        "id": "CreateimportMerchantPosApi",
        "title": "CreateimportMerchantPosApi"
      },
      {
        "id": "CreateimportProductPricesApi",
        "title": "CreateimportProductPricesApi"
      },
      {
        "id": "CreateimportProductImagesApi",
        "title": "CreateimportProductImagesApi"
      },
      {
        "id": "Createimport-productundefinedategoriesApi",
        "title": "Createimport-productundefinedategoriesApi"
      },
      {
        "id": "Createimport-merchantundefinedosApi",
        "title": "Createimport-merchantundefinedosApi"
      },
      {
        "id": "Createimport-productundefinedricesApi",
        "title": "Createimport-productundefinedricesApi"
      },
      {
        "id": "Createimport-productundefinedmagesApi",
        "title": "Createimport-productundefinedmagesApi"
      },
      {
        "id": "CreaterabbitmqimportproductundefinedfferApi",
        "title": "CreaterabbitmqimportproductundefinedfferApi"
      },
      {
        "id": "CreateimportundefinedroductundefinedategoriesApi",
        "title": "CreateimportundefinedroductundefinedategoriesApi"
      },
      {
        "id": "CreateimportundefinedategoriesApi",
        "title": "CreateimportundefinedategoriesApi"
      },
      {
        "id": "GetemailexportrefundundefinedinanceApi",
        "title": "GetemailexportrefundundefinedinanceApi"
      },
      {
        "id": "GetemailreconciliationorderundefinedtemApi",
        "title": "GetemailreconciliationorderundefinedtemApi"
      },
      {
        "id": "CreateimportundefinederchantundefinedosApi",
        "title": "CreateimportundefinederchantundefinedosApi"
      },
      {
        "id": "CreateimportundefinedroductundefinedricesApi",
        "title": "CreateimportundefinedroductundefinedricesApi"
      },
      {
        "id": "CreateimportundefinedroductundefinedmagesApi",
        "title": "CreateimportundefinedroductundefinedmagesApi"
      },
      {
        "id": "CreateimportundefinedroductsApi",
        "title": "CreateimportundefinedroductsApi"
      },
      {
        "id": "Createimport-categoriesApi",
        "title": "Createimport-categoriesApi"
      },
      {
        "id": "Getemailexportrefund-financeApi",
        "title": "Getemailexportrefund-financeApi"
      },
      {
        "id": "Createimport-productsApi",
        "title": "Createimport-productsApi"
      },
      {
        "id": "Createimport-ategoriesApi",
        "title": "Createimport-ategoriesApi"
      },
      {
        "id": "Getemailexportrefund-inanceApi",
        "title": "Getemailexportrefund-inanceApi"
      },
      {
        "id": "Createimport-roductsApi",
        "title": "Createimport-roductsApi"
      },
      {
        "id": "Createimport-ProductcategoriesApi",
        "title": "Createimport-ProductcategoriesApi"
      },
      {
        "id": "Createimport-CategoriesApi",
        "title": "Createimport-CategoriesApi"
      },
      {
        "id": "Getemailexportrefund-FinanceApi",
        "title": "Getemailexportrefund-FinanceApi"
      },
      {
        "id": "Createimport-MerchantposApi",
        "title": "Createimport-MerchantposApi"
      },
      {
        "id": "Createimport-ProductpricesApi",
        "title": "Createimport-ProductpricesApi"
      },
      {
        "id": "Createimport-ProductimagesApi",
        "title": "Createimport-ProductimagesApi"
      },
      {
        "id": "Createimport-ProductsApi",
        "title": "Createimport-ProductsApi"
      },
      {
        "id": "CreateimportP-productcategoriesApi",
        "title": "CreateimportP-productcategoriesApi"
      },
      {
        "id": "CreateimportC-categoriesApi",
        "title": "CreateimportC-categoriesApi"
      },
      {
        "id": "GetemailexportrefundF-financeApi",
        "title": "GetemailexportrefundF-financeApi"
      },
      {
        "id": "CreateimportM-merchantposApi",
        "title": "CreateimportM-merchantposApi"
      },
      {
        "id": "CreateimportP-productpricesApi",
        "title": "CreateimportP-productpricesApi"
      },
      {
        "id": "CreateimportP-productimagesApi",
        "title": "CreateimportP-productimagesApi"
      },
      {
        "id": "CreateimportP-productsApi",
        "title": "CreateimportP-productsApi"
      },
      {
        "id": "CreaterabbitmqimportproductofferApi",
        "title": "CreaterabbitmqimportproductofferApi"
      },
      {
        "id": "CreateimportProductcategoriesApi",
        "title": "CreateimportProductcategoriesApi"
      },
      {
        "id": "CreateimportCategoriesApi",
        "title": "CreateimportCategoriesApi"
      },
      {
        "id": "GetemailexportrefundFinanceApi",
        "title": "GetemailexportrefundFinanceApi"
      },
      {
        "id": "GetemailreconciliationorderitemApi",
        "title": "GetemailreconciliationorderitemApi"
      },
      {
        "id": "GetemailreconciliationorderApi",
        "title": "GetemailreconciliationorderApi"
      },
      {
        "id": "GetrabbitmqexportorderApi",
        "title": "GetrabbitmqexportorderApi"
      },
      {
        "id": "CreateimportMerchantposApi",
        "title": "CreateimportMerchantposApi"
      },
      {
        "id": "CreateimportProductpricesApi",
        "title": "CreateimportProductpricesApi"
      },
      {
        "id": "CreateimportProductimagesApi",
        "title": "CreateimportProductimagesApi"
      },
      {
        "id": "CreateimportProductsApi",
        "title": "CreateimportProductsApi"
      },
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
        "id": "CreateImportCategoriesApi",
        "title": "CreateImportCategoriesApi"
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
