<?php

/**
 * Copyright © 2016-present Spryker Systems GmbH. All rights reserved.
 * Use of this software requires acceptance of the Evaluation License Agreement. See LICENSE file.
 */

namespace SprykerSdkTest\LoadTesting\Fixtures\Merchant;

use Generated\Shared\DataBuilder\MerchantProfileAddressBuilder;
use Generated\Shared\DataBuilder\MerchantProfileBuilder;
use Generated\Shared\DataBuilder\StoreRelationBuilder;
use Generated\Shared\Transfer\MerchantProfileTransfer;
use Generated\Shared\Transfer\MerchantTransfer;
use Generated\Shared\Transfer\StoreRelationTransfer;
use Generated\Shared\Transfer\StoreTransfer;
use SprykerSdkTest\LoadTesting\Fixtures\Helper\LoadTestingCsvDemoDataLoaderTrait;
use SprykerSdkTest\LoadTesting\Fixtures\LoadTestingMerchantTester;
use SprykerTest\Shared\Testify\Fixtures\FixturesBuilderInterface;
use SprykerTest\Shared\Testify\Fixtures\FixturesContainerInterface;

/**
 * Auto-generated group annotations
 *
 * Add your own group annotations below this line
 */
class MerchantFixtures implements FixturesBuilderInterface, FixturesContainerInterface
{
    use LoadTestingCsvDemoDataLoaderTrait;

    /**
     * @uses \Spryker\Zed\Merchant\MerchantConfig::STATUS_APPROVED
     */
    protected const STATUS_APPROVED = 'approved';

    /**
     * @param \SprykerSdkTest\LoadTesting\Fixtures\LoadTestingMerchantTester $I
     *
     * @group SprykerSdkTest
     * @group LoadTesting
     * @group Fixtures
     * @group Merchant
     * @group MerchantFixtures
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
        $demoData = $this->loadDemoData();
        $stores = $I->getLocator()->store()->facade()->getAllStores();

        $storesIds = array_map(function (StoreTransfer $store) {
            return $store->getIdStore();
        }, $stores);

        foreach ($demoData as $data) {
            $merchantProfileAddressTransfer = (new MerchantProfileAddressBuilder())->build();

            $I->haveMerchant([
                MerchantTransfer::MERCHANT_REFERENCE => $data['reference'],
                MerchantTransfer::EMAIL => $data['email'],
                MerchantTransfer::STATUS => static::STATUS_APPROVED,
                MerchantTransfer::IS_ACTIVE => true,
                MerchantTransfer::STORE_RELATION => (new StoreRelationBuilder())->seed([
                    StoreRelationTransfer::ID_STORES => $storesIds,
                ])->build()->toArray(),
                MerchantTransfer::MERCHANT_PROFILE => (new MerchantProfileBuilder([
                    MerchantProfileTransfer::ADDRESS_COLLECTION => new \ArrayObject([$merchantProfileAddressTransfer]),
                ]))->build(),
            ]);
        }
    }

    protected function getFileName(): string
    {
        return 'merchant.csv';
    }

    protected function getRequiredFields(): array
    {
        return [
            'reference',
            'email',
        ];
    }
}
