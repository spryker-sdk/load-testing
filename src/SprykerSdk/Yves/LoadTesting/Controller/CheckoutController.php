<?php

/**
 * Copyright © 2016-present Spryker Systems GmbH. All rights reserved.
 * Use of this software requires acceptance of the Evaluation License Agreement. See LICENSE file.
 */

namespace SprykerSdk\Yves\LoadTesting\Controller;

use Generated\Shared\Transfer\QuoteTransfer;
use Spryker\Yves\Kernel\Controller\AbstractController;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;

/**
 * @method \SprykerSdk\Yves\LoadTesting\LoadTestingFactory getFactory()
 */
class CheckoutController extends AbstractController
{
    /**
     * @param \Symfony\Component\HttpFoundation\Request $request
     *
     * @return \Symfony\Component\HttpFoundation\JsonResponse
     */
    public function placeOrderDebugAction(Request $request): JsonResponse
    {
        $encodedQuote = $request->get('_payload');
        if ($encodedQuote === null) {
            return $this->jsonResponse([], 400);
        }

        $quoteArray = $this->getFactory()
            ->getUtilEncodingService()
            ->decodeJson($encodedQuote, true);
        $quoteTransfer = (new QuoteTransfer())->fromArray($quoteArray, true);

        if (!$this->getFactory()->getCheckoutClient()->isQuoteApplicableForCheckout($quoteTransfer)) {
            return $this->jsonResponse([], 400);
        }

        $checkoutResponseTransfer = $this->getFactory()->getCheckoutClient()->placeOrder($quoteTransfer);

        if (!$checkoutResponseTransfer->getIsSuccess()) {
            return $this->jsonResponse([], 400);
        }

        return $this->jsonResponse([], 200);
    }
}
