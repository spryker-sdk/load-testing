<?php

/**
 * This file is part of the Spryker Suite.
 * For full license information, please view the LICENSE file that was distributed with this source code.
 */

namespace SprykerSdkTest\LoadTesting\Fixtures\ProductConcrete;

use Codeception\Configuration;
use Generated\Shared\DataBuilder\LocalizedAttributesBuilder;
use Generated\Shared\Transfer\LocaleTransfer;
use Generated\Shared\Transfer\LocalizedAttributesTransfer;
use Generated\Shared\Transfer\ProductAbstractTransfer;
use Generated\Shared\Transfer\ProductConcreteTransfer;
use Orm\Zed\Locale\Persistence\SpyLocaleQuery;
use Propel\Runtime\Propel;
use Spryker\Zed\PriceProduct\Communication\Plugin\ProductConcrete\PriceProductConcreteAfterCreatePlugin;
use Spryker\Zed\ProductBundle\Communication\Plugin\Product\ProductBundleProductConcreteAfterCreatePlugin;
use Spryker\Zed\ProductDiscontinuedProductBundleConnector\Communication\Plugin\Product\DiscontinuedProductConcreteAfterCreatePlugin;
use Spryker\Zed\ProductImage\Communication\Plugin\ProductConcreteAfterCreatePlugin as ImageSetProductConcreteAfterCreatePlugin;
use Spryker\Zed\ProductSearch\Communication\Plugin\ProductConcrete\ProductSearchProductConcreteAfterCreatePlugin;
use Spryker\Zed\ProductSearch\Communication\Plugin\ProductConcrete\ProductSearchProductConcreteAfterUpdatePlugin;
use Spryker\Zed\ProductSearch\Communication\Plugin\ProductConcrete\ProductSearchProductConcreteReadPlugin;
use Spryker\Zed\ProductSearch\Communication\Plugin\ProductSearchConfigExtensionCollectorPlugin;
use Spryker\Zed\ProductValidity\Communication\Plugin\ProductValidityCreatePlugin;
use Spryker\Zed\Stock\Communication\Plugin\ProductConcreteAfterCreatePlugin as StockProductConcreteAfterCreatePlugin;
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

    protected const IS_SUPER_ATTRIBUTE_KEY = 'is_super';
    protected const ATTRIBUTE_KEY = 'key';

    protected const FORMAT_SUPER_ATTRIBUTE = 'attribute_key_%s';
    protected const FORMAT_SUPER_ATTRIBUTE_VALUE = 'attribute_value_%s';

    protected const ATTRIBUTE_MAP_KEY = 'attribute_map';
    protected const ATTRIBUTE_VARIANTS_KEY = 'attribute_variants';

    protected const ATTRIBUTE_KEY_COLOR = 'color';

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

    /**
     * @param \SprykerSdkTest\LoadTesting\Fixtures\LoadTestingProductTester $I
     *
     * @return void
     */
    protected function createProductConcrete(LoadTestingProductTester $I): void
    {
        $demoData = $this->loadDemoData();
        $productFacade = $I->getLocator()->product()->facade();

        foreach ($demoData as $data) {
            try{
                $sku = $data['sku'];
                if ($productFacade->hasProductConcrete($sku)) {
                    continue;
                }

                $productAttributes[static::ATTRIBUTE_KEY_COLOR] = sprintf(static::FORMAT_SUPER_ATTRIBUTE_VALUE, $sku);

                $productConcreteOverride = [
                    ProductConcreteTransfer::SKU => $sku,
                    ProductConcreteTransfer::IS_ACTIVE => 1,
                    ProductConcreteTransfer::ATTRIBUTES => $productAttributes
                ];
                $productAbstractOverride = [
                    ProductAbstractTransfer::SKU => $data['abstract_sku'],
                    ProductAbstractTransfer::ATTRIBUTES => $productAttributes
                ];

                $productConcreteTransfer = $I->haveFullProductWithPrice(
                    $productConcreteOverride,
                    $productAbstractOverride,
                    $data['pdp_url']
                );
            } catch (\Throwable $e) {
                echo $e->getMessage() . PHP_EOL;
            }
        }
    }

    /**
     * @return string
     */
    protected function getFileName(): string
    {
        return 'product_concrete_2.csv';
    }

    /**
     * @return string[]
     */
    protected function getRequiredFields(): array
    {
        return [
            'abstract_sku',
            'sku',
            'pdp_url',
        ];
    }
}
