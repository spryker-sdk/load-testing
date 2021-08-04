const scenarios = [
    {
        title: 'Storefront',
        tests: [
            { id: 'Home', title: 'Home page'},
            { id: 'Nope', title: 'Empty index'},
            { id: 'CatalogSearch', title: 'Search page'},
            { id: 'CatalogWithOffersSearch', title: 'Search offer page'},
            { id: 'Pdp', title: 'PDP page'},
            { id: 'OfferPdp', title: 'Offer PDP page'},
            { id: 'AddToGuestCart', title: 'Add to guest cart request'},
            { id: 'AddToCustomerCart', title: 'Add to customer cart request'},
            { id: 'AddOfferToCustomerCart', title: 'Add offer to customer cart request'},
            { id: 'PlaceOrder', title: 'Place order request'},
            { id: 'PlaceOrderWithOffers', title: 'Place order with offers request'},
            { id: 'PlaceOrderCustomer', title: 'Place order customer request'},
            { id: 'PlaceOrderWithOffersCustomer', title: 'Place order with offer customer request'},
            { id: 'AddOfferToGuestCart', title: 'Add offer to guest cart request'},
        ],
    },
    {
        title: 'API',
        tests: [
            { id: 'CartApi', title: 'Add to cart request'},
            { id: 'CartWithOffersApi', title: 'Add offer to cart request'},
            { id: 'GuestCartApi', title: 'Add to guest cart request'},
            { id: 'GuestWithOffersCartApi', title: 'Add offer to guest cart request'},
            { id: 'CatalogSearchApi', title: 'Search request'},
            { id: 'PdpApi', title: 'PDP request'},
            { id: 'OfferPdpApi', title: 'Offer PDP request'},
            { id: 'GuestCheckoutApi', title: 'Guest checkout request'},
            { id: 'GuestCheckoutWithOffersApi', title: 'Guest checkout with offers request'},
            { id: 'CheckoutApi', title: 'Checkout request'},
            { id: 'CheckoutWithOffersApi', title: 'Checkout with offers request'},
            { id: 'CartWithOffersApi', title: 'Add to cart with offers request'},
            { id: 'CatalogWithOffersSearchApi', title: 'Search offer request'},
        ],
    },
];

module.exports = scenarios;
