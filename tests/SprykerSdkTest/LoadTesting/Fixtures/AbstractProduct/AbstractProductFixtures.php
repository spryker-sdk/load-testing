<?php

/**
 * Copyright © 2016-present Spryker Systems GmbH. All rights reserved.
 * Use of this software requires acceptance of the Evaluation License Agreement. See LICENSE file.
 */

namespace SprykerSdkTest\LoadTesting\Fixtures\AbstractProduct;

use SprykerSdkTest\LoadTesting\Fixtures\LoadTestingAbstractProductTester;
use SprykerTest\Shared\Testify\Fixtures\FixturesBuilderInterface;
use SprykerTest\Shared\Testify\Fixtures\FixturesContainerInterface;

/**
 * Auto-generated group annotations
 *
 * @group SprykerSdkTest
 * @group LoadTesting
 * @group Fixtures
 * @group AbstractProduct
 * @group AbstractProductFixtures
 * Add your own group annotations below this line
 */
class AbstractProductFixtures implements FixturesBuilderInterface, FixturesContainerInterface
{
    /**
     * @param \SprykerSdkTest\LoadTesting\Fixtures\LoadTestingAbstractProductTester $I
     *
     * @return \SprykerTest\Shared\Testify\Fixtures\FixturesContainerInterface
     */
    public function buildFixtures(LoadTestingAbstractProductTester $I): FixturesContainerInterface
    {
        $this->createAbstractProduct($I);

        return $this;
    }

    /**
     * @param \SprykerSdkTest\LoadTesting\Fixtures\LoadTestingAbstractProductTester $I
     *
     * @return void
     */
    protected function createAbstractProduct(LoadTestingAbstractProductTester $I): void
    {
        // todo: make iterations parameterized?
        for ($i = 0; $i < 10000; $i++) {
            $I->haveProductAbstract();
        }
    }
}
