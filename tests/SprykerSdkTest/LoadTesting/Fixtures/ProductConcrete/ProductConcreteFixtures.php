<?php

/**
 * This file is part of the Spryker Suite.
 * For full license information, please view the LICENSE file that was distributed with this source code.
 */

namespace SprykerSdkTest\LoadTesting\Fixtures\ProductConcrete;

use Codeception\Configuration;
use Codeception\Module;
use Generated\Shared\DataBuilder\LocalizedAttributesBuilder;
use Generated\Shared\Transfer\LocaleTransfer;
use Generated\Shared\Transfer\LocalizedAttributesTransfer;
use Generated\Shared\Transfer\ProductAbstractTransfer;
use Generated\Shared\Transfer\ProductConcreteTransfer;
use Orm\Zed\Locale\Persistence\SpyLocaleQuery;
use Orm\Zed\PriceProduct\Persistence\SpyPriceProduct;
use Orm\Zed\PriceProduct\Persistence\SpyPriceProductQuery;
use Orm\Zed\PriceProduct\Persistence\SpyPriceProductStoreQuery;
use Propel\Runtime\Propel;
use Spryker\Zed\ProductSearch\Communication\Plugin\ProductConcrete\ProductSearchProductConcreteAfterCreatePlugin;
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
        $productSearchFacade = $I->getLocator()->productSearch()->facade();

        foreach ($demoData as $demoDataKey => $data) {
            try{
                $sku = $data['sku'];
                if ($productFacade->hasProductConcrete($sku)) {
                    $productConcreteTransfer = $productFacade->getProductConcrete($sku);
                    if (
                        $productSearchFacade->isProductConcreteSearchable($productConcreteTransfer->getIdProductConcrete()) &&
                        $this->isValidProductAbstractPrices($productConcreteTransfer->getFkProductAbstract(), $I)
                    ) {
                        continue;
                    }

                    (new ProductSearchProductConcreteAfterCreatePlugin())->create($productConcreteTransfer);
                    $this->debug(sprintf('Added Product concrete to Search: %s', $productConcreteTransfer->getSku()));

                    $priceProductOverride = $I->generatePriceProductData(
                        $productConcreteTransfer->getAbstractSku(),
                        $productConcreteTransfer->getAbstractSku(),
                        $productConcreteTransfer->getFkProductAbstract()
                    );
                    $I->createPriceProductAbstract($productConcreteTransfer->getFkProductAbstract(), $priceProductOverride);
                    $this->debug(sprintf('Updated default price for Product Abstract id: %s', $productConcreteTransfer->getFkProductAbstract()));


                    $this->debug(sprintf('%d row of the demo data was updated. Total amount amount of rows: %d.', $demoDataKey+1, count($demoData)));

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
     * @param string $message
     *
     * @return void
     */
    protected function debug(string $message): void
    {
        codecept_debug($message);
    }

    /**
     * @param int $idProductAbstract
     * @param \SprykerSdkTest\LoadTesting\Fixtures\LoadTestingProductTester $I
     *
     * @return bool
     */
    protected function isValidProductAbstractPrices(int $idProductAbstract, LoadTestingProductTester $I): bool
    {
        $priceProduct = $this->getPriceProductQuery()->findOneByFkProductAbstract($idProductAbstract);
        if (!$priceProduct || $priceProduct->getPriceType()->getName() !== 'DEFAULT') {
            return false;
        }

        $priceProductStoreCollection = $this->getPriceProductStoreQuery()
            ->findByFkPriceProduct($priceProduct->getIdPriceProduct()
            );

        foreach ($priceProductStoreCollection as $priceProductStore) {
            if ($priceProductStore->getGrossPrice()) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return \Orm\Zed\PriceProduct\Persistence\SpyPriceProductStoreQuery
     */
    protected function getPriceProductStoreQuery(): SpyPriceProductStoreQuery
    {
        return new SpyPriceProductStoreQuery();
    }

    /**
     * @return \Orm\Zed\PriceProduct\Persistence\SpyPriceProductQuery
     */
    protected function getPriceProductQuery(): SpyPriceProductQuery
    {
        return new SpyPriceProductQuery();
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
