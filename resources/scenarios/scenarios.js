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
            { id: 'CartApi', title: 'Add to cart request'},
            { id: 'GuestCartApi', title: 'Add to guest cart request'},
            { id: 'CatalogSearchApi', title: 'Search request'},
            { id: 'PdpApi', title: 'PDP request'},
            { id: 'GuestCheckoutApi', title: 'Guest checkout request'},
            { id: 'CheckoutApi', title: 'Checkout request'},
            { id: 'GuestCheckoutCustomerApi', title: 'Guest Checkout request with products'},
        ],
    },
];

module.exports = scenarios;
