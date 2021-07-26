<?php

/**
 * Copyright © 2016-present Spryker Systems GmbH. All rights reserved.
 * Use of this software requires acceptance of the Evaluation License Agreement. See LICENSE file.
 */

namespace SprykerSdkTest\LoadTesting\Fixtures\ProductOffer;

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
        // todo: make iterations parameterized?
        for ($i = 0; $i < 10000; $i++) {
            $I->haveProductOffer();
        }
    }
}
