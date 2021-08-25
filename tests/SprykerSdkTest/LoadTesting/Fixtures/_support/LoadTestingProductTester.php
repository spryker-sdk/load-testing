<?php

/**
 * Copyright © 2016-present Spryker Systems GmbH. All rights reserved.
 * Use of this software requires acceptance of the Evaluation License Agreement. See LICENSE file.
 */

namespace SprykerSdkTest\LoadTesting\Fixtures;

use \ArrayObject;
use Codeception\Actor;
use Generated\Shared\DataBuilder\PriceProductBuilder;
use Generated\Shared\Transfer\CurrencyTransfer;
use Generated\Shared\Transfer\MoneyValueTransfer;
use Generated\Shared\Transfer\PriceProductDimensionTransfer;
use Generated\Shared\Transfer\PriceProductTransfer;
use Generated\Shared\Transfer\PriceTypeTransfer;
use Generated\Shared\Transfer\ProductAbstractTransfer;
use Generated\Shared\Transfer\ProductConcreteTransfer;
use Generated\Shared\Transfer\StockProductTransfer;
use Generated\Shared\Transfer\StoreTransfer;
use Orm\Zed\Url\Persistence\SpyUrlQuery;
use Spryker\Shared\PriceProduct\PriceProductConfig;
use Spryker\Zed\Currency\Business\CurrencyFacadeInterface;
use Spryker\Zed\PriceProduct\Business\PriceProductFacadeInterface;
use Spryker\Zed\PriceProduct\Persistence\PriceProductQueryContainerInterface;
use Spryker\Zed\Store\Business\StoreFacadeInterface;
use SprykerTest\Shared\Testify\Fixtures\FixturesExporterInterface;
use SprykerTest\Shared\Testify\Fixtures\FixturesTrait;
use SprykerTest\Shared\Testify\Helper\DataCleanupHelperTrait;

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

    protected const EUR_ISO_CODE = 'EUR';
    protected const NET_PRICE = 10;
    protected const GROSS_PRICE = 9;
    protected const STORE_NAME_DE = "DE";
    protected const STORE_NAME_US = "US";


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
            $productAbstractOverride,
            true
        );

        $this->haveProductInStockForStore($storeTransfer, [
            StockProductTransfer::SKU => $productConcreteTransfer->getSku(),
            StockProductTransfer::IS_NEVER_OUT_OF_STOCK => 1,
        ]);

        $priceProductConcreteOverride = $this->generatePriceProductData(
            $productConcreteTransfer->getAbstractSku(),
            $productConcreteTransfer->getSku(),
            $productConcreteTransfer->getIdProductConcrete()
        );

        $priceProductAbstractOverride = $this->generatePriceProductData(
                $productConcreteTransfer->getAbstractSku(),
                $productConcreteTransfer->getAbstractSku(),
                $productConcreteTransfer->getIdProductConcrete()
            );

        $this->havePriceProduct($priceProductConcreteOverride);
        $this->createPriceProductAbstract($productConcreteTransfer->getFkProductAbstract(), $priceProductAbstractOverride);
        $this->replaceProductUrl($productConcreteTransfer->getFkProductAbstract(), $productUrl);
        $this->haveAvailabilityAbstract($productConcreteTransfer);

        return $productConcreteTransfer;
    }

    /**
     * @param int $idProductAbstract
     * @param array $priceProductOverride
     *
     * @return \Generated\Shared\Transfer\PriceProductTransfer
     */
    public function createPriceProductAbstract(int $idProductAbstract, array $priceProductOverride = []): ProductAbstractTransfer
    {
        $storeTransfers = $this->getStoreFacade()->getAllStores();
        $priceProductTransfers = new ArrayObject();

        foreach ($storeTransfers as $storeTransfer) {
            $priceProductTransfers->append(
                $this->createPriceProductTransfer(
                    $priceProductOverride,
                    static::EUR_ISO_CODE,
                    $storeTransfer
                )
            );
        }

        $productAbstractTransfer = $this->getPriceProductFacade()
            ->persistProductAbstractPriceCollection(
            (new ProductAbstractTransfer())
                ->setIdProductAbstract($idProductAbstract)
                ->setPrices($priceProductTransfers)
            );

        return $productAbstractTransfer;
    }

    /**
     * @param string $skuProductAbstract
     * @param string $skuProduct
     * @param int $idProduct
     *
     * @return array
     */
    public function generatePriceProductData(string $skuProductAbstract, string $skuProduct, int $idProduct): array
    {
        $priceProductData = [
            PriceProductTransfer::SKU_PRODUCT_ABSTRACT =>$skuProductAbstract,
            PriceProductTransfer::SKU_PRODUCT => $skuProduct,
            PriceProductTransfer::ID_PRODUCT => $idProduct,
            PriceProductTransfer::PRICE_TYPE_NAME => 'DEFAULT',
            PriceProductTransfer::MONEY_VALUE => [
                MoneyValueTransfer::NET_AMOUNT => static::NET_PRICE,
                MoneyValueTransfer::GROSS_AMOUNT => static::GROSS_PRICE,
            ],
        ];

        return $priceProductData;
    }

    /**
     * @param array $priceProductOverride
     * @param string $currencyIsoCode
     * @param \Generated\Shared\Transfer\StoreTransfer $storeTransfer
     *
     * @return \Generated\Shared\Transfer\PriceProductTransfer
     */
    protected function createPriceProductTransfer(
        array $priceProductOverride,
        string $currencyIsoCode,
        StoreTransfer $storeTransfer
    ): PriceProductTransfer {
        $currencyTransfer = $this->getCurrencyFacade()->fromIsoCode($currencyIsoCode);

        $config = $this->getSharedPriceProductConfig();

        $priceDimensionTransfer = (new PriceProductDimensionTransfer())
            ->setType($config->getPriceDimensionDefault());

        $defaultPriceTypeName = $this->getPriceProductFacade()->getDefaultPriceTypeName();
        $idPriceType = $this->getPriceTypeId($defaultPriceTypeName);

        $priceTypeTransfer = (new PriceTypeTransfer())
            ->setName($defaultPriceTypeName)
            ->setIdPriceType($idPriceType);

        if (isset($priceProductOverride[PriceProductTransfer::PRICE_TYPE])) {
            $priceTypeTransfer = $priceProductOverride[PriceProductTransfer::PRICE_TYPE];
        }

        if (isset($priceProductOverride[PriceProductTransfer::MONEY_VALUE][MoneyValueTransfer::CURRENCY])) {
            $currencyTransfer = $priceProductOverride[PriceProductTransfer::MONEY_VALUE][MoneyValueTransfer::CURRENCY];
        }

        $priceProductDefaultData = [
            PriceProductTransfer::PRICE_TYPE_NAME => $priceTypeTransfer->getName(),
            PriceProductTransfer::PRICE_TYPE => $priceTypeTransfer,
            PriceProductTransfer::PRICE_DIMENSION => $priceDimensionTransfer,
            PriceProductTransfer::FK_PRICE_TYPE => $priceTypeTransfer->getIdPriceType(),
        ];

        $priceProductTransfer = (new PriceProductBuilder($priceProductDefaultData))
            ->seed($priceProductOverride)
            ->build();

        $grossPrice = $priceProductOverride[PriceProductTransfer::MONEY_VALUE][MoneyValueTransfer::GROSS_AMOUNT];
        $netPrice = $priceProductOverride[PriceProductTransfer::MONEY_VALUE][MoneyValueTransfer::NET_AMOUNT];

        $moneyValueTransfer = $this->createMoneyValueTransfer(
            $grossPrice,
            $netPrice,
            $storeTransfer,
            $currencyTransfer,
            $priceProductOverride[PriceProductTransfer::MONEY_VALUE] ?? []
        );

        $priceProductTransfer->setMoneyValue($moneyValueTransfer);

        return $priceProductTransfer;
    }

    /**
     * @param int $grossAmount
     * @param int $netAmount
     * @param \Generated\Shared\Transfer\StoreTransfer $storeTransfer
     * @param \Generated\Shared\Transfer\CurrencyTransfer $currencyTransfer
     * @param array $moneyValueOverride
     *
     * @return \Generated\Shared\Transfer\MoneyValueTransfer
     */
    protected function createMoneyValueTransfer(
        int $grossAmount,
        int $netAmount,
        StoreTransfer $storeTransfer,
        CurrencyTransfer $currencyTransfer,
        array $moneyValueOverride = []
    ): MoneyValueTransfer {
        $moneyValueTransfer = (new MoneyValueTransfer())->fromArray($moneyValueOverride, true);

        return $moneyValueTransfer
            ->setNetAmount($netAmount)
            ->setGrossAmount($grossAmount)
            ->setFkStore($storeTransfer->getIdStore())
            ->setFkCurrency($currencyTransfer->getIdCurrency())
            ->setCurrency($currencyTransfer)
            ->setStore($storeTransfer);
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

    /**
     * @return \Spryker\Zed\PriceProduct\Business\PriceProductFacadeInterface
     */
    protected function getPriceProductFacade(): PriceProductFacadeInterface
    {
        return $this->getLocator()->priceProduct()->facade();
    }

    /**
     * @return \Spryker\Zed\Currency\Business\CurrencyFacadeInterface
     */
    protected function getCurrencyFacade(): CurrencyFacadeInterface
    {
        return $this->getLocator()->currency()->facade();
    }

    /**
     * @param string $name
     *
     * @return int|null
     */
    protected function getPriceTypeId(string $name): ?int
    {
        $spyPriceTypeEntity = $this->getPriceProductQueryContainer()->queryPriceType($name)->findOne();

        if (!$spyPriceTypeEntity) {
            return null;
        }

        return $spyPriceTypeEntity->getIdPriceType();
    }

    /**
     * @return \Spryker\Zed\PriceProduct\Persistence\PriceProductQueryContainerInterface
     */
    protected function getPriceProductQueryContainer(): PriceProductQueryContainerInterface
    {
        return $this->getLocator()->priceProduct()->queryContainer();
    }

    /**
     * @return \Spryker\Shared\PriceProduct\PriceProductConfig
     */
    protected function getSharedPriceProductConfig(): PriceProductConfig
    {
        return new PriceProductConfig();
    }
}
