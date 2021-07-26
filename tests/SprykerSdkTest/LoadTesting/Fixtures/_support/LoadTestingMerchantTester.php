<?php

/**
 * Copyright © 2016-present Spryker Systems GmbH. All rights reserved.
 * Use of this software requires acceptance of the Evaluation License Agreement. See LICENSE file.
 */

namespace SprykerSdkTest\LoadTesting\Fixtures;

use Codeception\Actor;
use SprykerTest\Shared\Testify\Fixtures\FixturesExporterInterface;
use SprykerTest\Shared\Testify\Fixtures\FixturesTrait;

class LoadTestingMerchantTester extends Actor implements FixturesExporterInterface
{
    use _generated\LoadTestingMerchantTesterActions;
    use FixturesTrait;
}
