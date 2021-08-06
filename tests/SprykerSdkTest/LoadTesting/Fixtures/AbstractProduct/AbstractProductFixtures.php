<?php

/**
 * Copyright © 2016-present Spryker Systems GmbH. All rights reserved.
 * Use of this software requires acceptance of the Evaluation License Agreement. See LICENSE file.
 */

namespace SprykerSdkTest\LoadTesting\Fixtures\AbstractProduct;

use Generated\Shared\Transfer\ProductAbstractTransfer;
use SprykerSdkTest\LoadTesting\Fixtures\Helper\LoadTestingCsvDemoDataLoaderTrait;
use SprykerSdkTest\LoadTesting\Fixtures\LoadTestingAbstractProductTester;
use SprykerTest\Shared\Testify\Fixtures\FixturesBuilderInterface;
use SprykerTest\Shared\Testify\Fixtures\FixturesContainerInterface;

/**
 * Auto-generated group annotations
 *
 * Add your own group annotations below this line
 */
class AbstractProductFixtures implements FixturesBuilderInterface, FixturesContainerInterface
{
    use LoadTestingCsvDemoDataLoaderTrait;

    /**
     * @param \SprykerSdkTest\LoadTesting\Fixtures\LoadTestingAbstractProductTester $I
     *
     * @group SprykerSdkTest
     * @group LoadTesting
     * @group Fixtures
     * @group AbstractProduct
     * @group AbstractProductFixtures
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
        $demoData = $this->loadDemoData();

        foreach ($demoData as $data) {
            $I->haveProductAbstract([
                ProductAbstractTransfer::SKU => $data['sku'],
                ProductAbstractTransfer::ID_TAX_SET => 3,
            ]);
        }
    }

    protected function getFileName(): string
    {
        return 'product_abstract.csv';
    }

    protected function getRequiredFields(): array
    {
        return [
            'sku',
        ];
    }
}
