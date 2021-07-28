<?php

/**
 * Copyright © 2016-present Spryker Systems GmbH. All rights reserved.
 * Use of this software requires acceptance of the Evaluation License Agreement. See LICENSE file.
 */

namespace SprykerSdkTest\LoadTesting\Fixtures\ProductOffer;

use Generated\Shared\Transfer\ProductOfferTransfer;
use SprykerSdkTest\LoadTesting\Fixtures\Helper\LoadTestingCsvDemoDataLoaderTrait;
use SprykerSdkTest\LoadTesting\Fixtures\LoadTestingProductOfferTester;
use SprykerTest\Shared\Testify\Fixtures\FixturesBuilderInterface;
use SprykerTest\Shared\Testify\Fixtures\FixturesContainerInterface;

/**
 * Auto-generated group annotations
 *
 * @group SprykerSdkTest
 * @group LoadTesting
 * @group Fixtures
 * @group ProductOffer
 * @group ProductOfferFixtures
 * Add your own group annotations below this line
 */
class ProductOfferFixtures implements FixturesBuilderInterface, FixturesContainerInterface
{
    use LoadTestingCsvDemoDataLoaderTrait;

    /**
     * @param \SprykerSdkTest\LoadTesting\Fixtures\LoadTestingProductOfferTester $I
     *
     * @return \SprykerTest\Shared\Testify\Fixtures\FixturesContainerInterface
     */
    public function buildFixtures(LoadTestingProductOfferTester $I): FixturesContainerInterface
    {
        $this->createProductOffers($I);

        return $this;
    }

    /**
     * @param \SprykerSdkTest\LoadTesting\Fixtures\LoadTestingProductOfferTester $I
     *
     * @return void
     */
    protected function createProductOffers(LoadTestingProductOfferTester $I): void
    {
        $demoData = $this->loadDemoData();

        foreach ($demoData as $data) {
            $I->haveProductOffer([
                ProductOfferTransfer::CONCRETE_SKU => $data['product_sku'],
                ProductOfferTransfer::MERCHANT_REFERENCE => $data['merchant_reference'],
                ProductOfferTransfer::PRODUCT_OFFER_REFERENCE => $data['product_offer_reference'],
            ]);
        }
    }

    protected function getFileName(): string
    {
        return 'merchant_product_offer.csv';
    }

    protected function getRequiredFields(): array
    {
        return [
            'product_sku',
            'merchant_reference',
            'product_offer_reference',
        ];
    }
}
