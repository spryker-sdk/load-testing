<?php

/**
 * Copyright © 2016-present Spryker Systems GmbH. All rights reserved.
 * Use of this software requires acceptance of the Evaluation License Agreement. See LICENSE file.
 */

namespace SprykerSdk\Yves\LoadTesting;

use Spryker\Yves\Kernel\AbstractFactory;
use SprykerSdk\Yves\LoadTesting\Dependency\Client\LoadTestingToCheckoutClientInterface;
use SprykerSdk\Yves\LoadTesting\Dependency\Client\LoadTestingToQuoteClientInterface;
use SprykerSdk\Yves\LoadTesting\Dependency\Service\LoadTestingToUtilEncodingServiceInterface;

class LoadTestingFactory extends AbstractFactory
{
    /**
     * @return \SprykerSdk\Yves\LoadTesting\Dependency\Client\LoadTestingToCheckoutClientInterface
     */
    public function getCheckoutClient(): LoadTestingToCheckoutClientInterface
    {
        return $this->getProvidedDependency(LoadTestingDependencyProvider::CLIENT_CHECKOUT);
    }

    /**
     * @return \SprykerSdk\Yves\LoadTesting\Dependency\Client\LoadTestingToQuoteClientInterface
     */
    public function getQuoteClient(): LoadTestingToQuoteClientInterface
    {
        return $this->getProvidedDependency(LoadTestingDependencyProvider::CLIENT_QUOTE);
    }

    /**
     * @return \SprykerSdk\Yves\LoadTesting\Dependency\Service\LoadTestingToUtilEncodingServiceInterface
     */
    public function getUtilEncodingService(): LoadTestingToUtilEncodingServiceInterface
    {
        return $this->getProvidedDependency(LoadTestingDependencyProvider::SERVICE_UTIL_ENCODING);
    }
}
