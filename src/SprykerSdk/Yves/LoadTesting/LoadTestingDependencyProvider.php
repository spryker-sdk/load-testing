<?php

/**
 * Copyright © 2016-present Spryker Systems GmbH. All rights reserved.
 * Use of this software requires acceptance of the Evaluation License Agreement. See LICENSE file.
 */

namespace SprykerSdk\Yves\LoadTesting;

use Spryker\Yves\Kernel\AbstractBundleDependencyProvider;
use Spryker\Yves\Kernel\Container;
use SprykerSdk\Yves\LoadTesting\Dependency\Client\LoadTestingToCheckoutClientBridge;
use SprykerSdk\Yves\LoadTesting\Dependency\Client\LoadTestingToCheckoutClientInterface;
use SprykerSdk\Yves\LoadTesting\Dependency\Client\LoadTestingToQuoteClientBridge;
use SprykerSdk\Yves\LoadTesting\Dependency\Client\LoadTestingToQuoteClientInterface;
use SprykerSdk\Yves\LoadTesting\Dependency\Service\LoadTestingToUtilEncodingServiceBridge;
use SprykerSdk\Yves\LoadTesting\Dependency\Service\LoadTestingToUtilEncodingServiceInterface;

class LoadTestingDependencyProvider extends AbstractBundleDependencyProvider
{
    public const CLIENT_CHECKOUT = 'CLIENT_CHECKOUT';
    public const CLIENT_QUOTE = 'CLIENT_QUOTE';

    public const SERVICE_UTIL_ENCODING = 'SERVICE_UTIL_ENCODING';

    /**
     * @param \Spryker\Yves\Kernel\Container $container
     *
     * @return \Spryker\Yves\Kernel\Container
     */
    public function provideDependencies(Container $container): Container
    {
        $container = parent::provideDependencies($container);

        $container = $this->addCheckoutClient($container);
        $container = $this->addQuoteClient($container);
        $container = $this->addUtilEncodingService($container);

        return $container;
    }

    /**
     * @param \Spryker\Yves\Kernel\Container $container
     *
     * @return \Spryker\Yves\Kernel\Container
     */
    protected function addCheckoutClient(Container $container): Container
    {
        $container->set(static::CLIENT_CHECKOUT, function (Container $container): LoadTestingToCheckoutClientInterface {
            return new LoadTestingToCheckoutClientBridge(
                $container->getLocator()->checkout()->client()
            );
        });

        return $container;
    }

    /**
     * @param \Spryker\Yves\Kernel\Container $container
     *
     * @return \Spryker\Yves\Kernel\Container
     */
    protected function addQuoteClient(Container $container): Container
    {
        $container->set(static::CLIENT_QUOTE, function (Container $container): LoadTestingToQuoteClientInterface {
            return new LoadTestingToQuoteClientBridge($container->getLocator()->quote()->client());
        });

        return $container;
    }

    /**
     * @param \Spryker\Yves\Kernel\Container $container
     *
     * @return \Spryker\Yves\Kernel\Container
     */
    protected function addUtilEncodingService(Container $container): Container
    {
        $container->set(static::SERVICE_UTIL_ENCODING, function (Container $container): LoadTestingToUtilEncodingServiceInterface {
            return new LoadTestingToUtilEncodingServiceBridge($container->getLocator()->utilEncoding()->service());
        });

        return $container;
    }
}
