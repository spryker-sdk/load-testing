<?php

/**
 * Copyright © 2016-present Spryker Systems GmbH. All rights reserved.
 * Use of this software requires acceptance of the Evaluation License Agreement. See LICENSE file.
 */

namespace SprykerSdkTest\LoadTesting\Fixtures;

use Codeception\Actor;
use Generated\Shared\Transfer\MoneyValueTransfer;
use Generated\Shared\Transfer\PriceProductTransfer;
use Generated\Shared\Transfer\ProductConcreteTransfer;
use Generated\Shared\Transfer\StockProductTransfer;
use Orm\Zed\Url\Persistence\SpyUrlQuery;
use Spryker\Zed\Store\Business\StoreFacadeInterface;
use SprykerTest\Shared\Testify\Fixtures\FixturesExporterInterface;
use SprykerTest\Shared\Testify\Fixtures\FixturesTrait;

/**
 * Inherited Methods
 *
 * @method void wantToTest($text)
 * @method void wantTo($text)
 * @method void execute($callable)
 * @method void expectTo($prediction)
 * @method void expect($prediction)
 * @method void amGoingTo($argumentation)
 * @method void am($role)
 * @method void lookForwardTo($achieveValue)
 * @method void comment($description)
 * @method void pause()
 *
 * @SuppressWarnings(PHPMD)
 */
class LoadTestingProductTester extends Actor implements FixturesExporterInterface
{
    use _generated\LoadTestingProductTesterActions;
    use FixturesTrait;

    /**
     * @param array $productConcreteOverride
     * @param array $productAbstractOverride
     * @param string $productUrl
     *
     * @return \Generated\Shared\Transfer\ProductConcreteTransfer
     */
    public function haveFullProductWithPrice(array $productConcreteOverride, array $productAbstractOverride, string $productUrl): ProductConcreteTransfer
    {
        $storeTransfer = $this->getStoreFacade()->getCurrentStore();

        $productConcreteTransfer = $this->haveFullProduct(
            $productConcreteOverride,
            $productAbstractOverride
        );

        $this->haveProductInStockForStore($storeTransfer, [
            StockProductTransfer::SKU => $productConcreteTransfer->getSku(),
            StockProductTransfer::IS_NEVER_OUT_OF_STOCK => 1,
        ]);

        $priceProductOverride = [
            PriceProductTransfer::SKU_PRODUCT_ABSTRACT => $productConcreteTransfer->getAbstractSku(),
            PriceProductTransfer::SKU_PRODUCT => $productConcreteTransfer->getSku(),
            PriceProductTransfer::ID_PRODUCT => $productConcreteTransfer->getIdProductConcrete(),
            PriceProductTransfer::PRICE_TYPE_NAME => 'DEFAULT',
            PriceProductTransfer::MONEY_VALUE => [
                MoneyValueTransfer::NET_AMOUNT => 770,
                MoneyValueTransfer::GROSS_AMOUNT => 880,
            ],
        ];

        $this->havePriceProduct($priceProductOverride);
        $this->replaceProductUrl($productConcreteTransfer->getFkProductAbstract(), $productUrl);
        $this->haveAvailabilityAbstract($productConcreteTransfer);

        return $productConcreteTransfer;
    }

    /**
     * @param int $idProductAbstract
     * @param string $urlReplace
     *
     * @return void
     */
    protected function replaceProductUrl(int $idProductAbstract, string $urlReplace): void
    {
        $urlEntity = SpyUrlQuery::create()
            ->filterByFkResourceProductAbstract($idProductAbstract)
            ->useSpyLocaleQuery()
                ->filterByLocaleName('en_US')
            ->endUse()
            ->findOne();

        $urlEntity->setUrl($urlReplace);
        $urlEntity->save();
    }

    /**
     * @return \Spryker\Zed\Store\Business\StoreFacadeInterface
     */
    protected function getStoreFacade(): StoreFacadeInterface
    {
        return $this->getLocator()->store()->facade();
    }
}
