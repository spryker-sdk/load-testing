<?php

/**
 * This file is part of the Spryker Suite.
 * For full license information, please view the LICENSE file that was distributed with this source code.
 */

namespace SprykerSdkTest\LoadTesting\Fixtures\ProductConcrete;

use Generated\Shared\Transfer\ProductAbstractTransfer;
use Generated\Shared\Transfer\ProductConcreteTransfer;
use Propel\Runtime\Propel;
use SprykerSdkTest\LoadTesting\Fixtures\Helper\LoadTestingCsvDemoDataLoaderTrait;
use SprykerSdkTest\LoadTesting\Fixtures\LoadTestingProductTester;
use SprykerTest\Shared\Testify\Fixtures\FixturesBuilderInterface;
use SprykerTest\Shared\Testify\Fixtures\FixturesContainerInterface;

/**
 * Auto-generated group annotations
 *
 * Add your own group annotations below this line
 */
class ProductConcreteFixtures implements FixturesBuilderInterface, FixturesContainerInterface
{
    use LoadTestingCsvDemoDataLoaderTrait;

    /**
     * @param \SprykerSdkTest\LoadTesting\Fixtures\LoadTestingProductTester $I
     *
     * @group SprykerSdkTest
     * @group LoadTesting
     * @group Fixtures
     * @group ProductConcrete
     * @group ProductConcreteFixtures
     * @return \SprykerTest\Shared\Testify\Fixtures\FixturesContainerInterface
     */
    public function buildFixtures(LoadTestingProductTester $I): FixturesContainerInterface
    {
        $this->createProductConcrete($I);

        return $this;
    }

    protected function createProductConcrete(LoadTestingProductTester $I): void
    {
        $demoData = $this->loadDemoData();

        $productFacade = $I->getLocator()->product()->facade();

        foreach ($demoData as $data) {
            try {
                $sku = $data['sku'];

                if ($productFacade->hasProductConcrete($sku)) {
                    continue;
                }

                $productConcreteOverride = [
                    ProductConcreteTransfer::SKU => $sku,
                    ProductConcreteTransfer::IS_ACTIVE => 1,
                ];

                $I->haveFullProductWithPrice($productConcreteOverride, [
                    ProductAbstractTransfer::SKU => $data['abstract_sku'],
                ], $data['pdp_url']);
            } catch (\Throwable $e) {
                echo $e->getMessage() . PHP_EOL;
            }
        }
    }

    protected function getFileName(): string
    {
        return 'product_concrete_2.csv';
    }

    protected function getRequiredFields(): array
    {
        return [
            'abstract_sku',
            'sku',
            'pdp_url',
        ];
    }
}
