<?php

/**
 * Copyright © 2016-present Spryker Systems GmbH. All rights reserved.
 * Use of this software requires acceptance of the Evaluation License Agreement. See LICENSE file.
 */

namespace SprykerSdkTest\LoadTesting\Fixtures\MerchantOrder;

use SprykerSdkTest\LoadTesting\Fixtures\LoadTestingMerchantOrderTester;
use SprykerTest\Shared\Testify\Fixtures\FixturesBuilderInterface;
use SprykerTest\Shared\Testify\Fixtures\FixturesContainerInterface;

/**
 * Auto-generated group annotations
 *
 * @group SprykerSdkTest
 * @group LoadTesting
 * @group Fixtures
 * @group MerchantOrder
 * @group MerchantOrderFixtures
 * Add your own group annotations below this line
 */
class MerchantOrderFixtures implements FixturesBuilderInterface, FixturesContainerInterface
{
    public function buildFixtures(LoadTestingMerchantOrderTester $I): FixturesContainerInterface
    {
        $this->createMerchantOrder($I);

        return $this;
    }

    /**
     * @param \SprykerSdkTest\LoadTesting\Fixtures\LoadTestingMerchantOrderTester $I
     *
     * @return void
     */
    protected function createMerchantOrder(LoadTestingMerchantOrderTester $I): void
    {
        // todo: make iterations parameterized?
        for ($i = 0; $i < 10000; $i++) {
            $I->haveMerchantOrder();
        }
    }
}
