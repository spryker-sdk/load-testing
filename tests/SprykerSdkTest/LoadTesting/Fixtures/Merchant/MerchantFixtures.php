<?php

/**
 * Copyright © 2016-present Spryker Systems GmbH. All rights reserved.
 * Use of this software requires acceptance of the Evaluation License Agreement. See LICENSE file.
 */

namespace SprykerSdkTest\LoadTesting\Fixtures\Merchant;

use Faker\Factory;
use Generated\Shared\Transfer\MerchantTransfer;
use SprykerSdkTest\LoadTesting\Fixtures\LoadTestingMerchantTester;
use SprykerTest\Shared\Testify\Fixtures\FixturesBuilderInterface;
use SprykerTest\Shared\Testify\Fixtures\FixturesContainerInterface;

/**
 * Auto-generated group annotations
 *
 * @group SprykerSdkTest
 * @group LoadTesting
 * @group Fixtures
 * @group Merchant
 * @group MerchantFixtures
 * Add your own group annotations below this line
 */
class MerchantFixtures implements FixturesBuilderInterface, FixturesContainerInterface
{
    /**
     * @param \SprykerSdkTest\LoadTesting\Fixtures\LoadTestingMerchantTester $I
     *
     * @return \SprykerTest\Shared\Testify\Fixtures\FixturesContainerInterface
     */
    public function buildFixtures(LoadTestingMerchantTester $I): FixturesContainerInterface
    {
        $this->createMerchant($I);

        return $this;
    }

    /**
     * @param \SprykerSdkTest\LoadTesting\Fixtures\LoadTestingMerchantTester $I
     *
     * @return void
     */
    protected function createMerchant(LoadTestingMerchantTester $I): void
    {
        $faker = Factory::create(); // todo: di/parameterized?

        // todo: make iterations parameterized?
        for ($i = 0; $i < 10000; $i++) {
            $I->haveMerchant([
                MerchantTransfer::EMAIL => $faker->unique()->email,
            ]);
        }
    }
}