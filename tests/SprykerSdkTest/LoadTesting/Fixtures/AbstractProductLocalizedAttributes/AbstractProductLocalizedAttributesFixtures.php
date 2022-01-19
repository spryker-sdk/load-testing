<?php

/**
 * Copyright © 2016-present Spryker Systems GmbH. All rights reserved.
 * Use of this software requires acceptance of the Evaluation License Agreement. See LICENSE file.
 */

namespace SprykerSdkTest\LoadTesting\Fixtures\AbstractProductLocalizedAttributes;

use Generated\Shared\DataBuilder\LocalizedAttributesBuilder;
use Generated\Shared\DataBuilder\ProductAbstractBuilder;
use Generated\Shared\DataBuilder\StoreRelationBuilder;
use Generated\Shared\Transfer\LocalizedAttributesTransfer;
use Generated\Shared\Transfer\ProductConcreteTransfer;
use Generated\Shared\Transfer\StoreRelationTransfer;
use Generated\Shared\Transfer\StoreTransfer;
use SprykerSdkTest\LoadTesting\Fixtures\Helper\LoadTestingCsvDemoDataLoaderTrait;
use SprykerSdkTest\LoadTesting\Fixtures\LoadTestingAbstractProductLocalizedAttributesTester;
use SprykerTest\Shared\Testify\Fixtures\FixturesBuilderInterface;
use SprykerTest\Shared\Testify\Fixtures\FixturesContainerInterface;

/**
 * Auto-generated group annotations
 *
 * Add your own group annotations below this line
 */
class AbstractProductLocalizedAttributesFixtures implements FixturesBuilderInterface, FixturesContainerInterface
{
    use LoadTestingCsvDemoDataLoaderTrait;

    /**
     * @param \SprykerSdkTest\LoadTesting\Fixtures\LoadTestingAbstractProductLocalizedAttributesTester $I
     *
     * @group SprykerSdkTest
     * @group LoadTesting
     * @group Fixtures
     * @group AbstractProductLocalizedAttributes
     * @group AbstractProductLocalizedAttributesFixtures
     * @return \SprykerTest\Shared\Testify\Fixtures\FixturesContainerInterface
     */
    public function buildFixtures(LoadTestingAbstractProductLocalizedAttributesTester $I): FixturesContainerInterface
    {
        $this->createAbstractProduct($I);

        return $this;
    }

    /**
     * @param \SprykerSdkTest\LoadTesting\Fixtures\LoadTestingAbstractProductLocalizedAttributesTester $I
     *
     * @return void
     */
    protected function createAbstractProduct(LoadTestingAbstractProductLocalizedAttributesTester $I): void
    {
        $demoData = $this->loadDemoData();

        $stores = $I->getLocator()->store()->facade()->getAllStores();
        $idStores = array_map(function (StoreTransfer $storeTransfer) {
            return $storeTransfer->getIdStore();
        }, $stores);

        $allStoresRelation = (new StoreRelationBuilder([
            StoreRelationTransfer::ID_STORES => $idStores,
            StoreRelationTransfer::STORES => new \ArrayObject($stores),
        ]))->build();

        $localizedAttributes = (new LocalizedAttributesBuilder([
            LocalizedAttributesTransfer::NAME => uniqid('Product #', true),
            LocalizedAttributesTransfer::LOCALE => $I->getLocator()->locale()->facade()->getCurrentLocale(),
        ]))->build();

        $productFacade = $I->getLocator()->product()->facade();

        foreach ($demoData as $data) {
            $sku = $data['sku'];
            $productAbstract = $productFacade->findProductAbstractById($productFacade->findProductAbstractIdBySku($sku))
                ->setLocalizedAttributes(new \ArrayObject([$localizedAttributes]))
                ->setStoreRelation($allStoresRelation);

            $productFacade->saveProductAbstract($productAbstract);

            echo "Updated Abstract Product: {$sku}";
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
