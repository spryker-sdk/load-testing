<?php

/**
 * This file is part of the Spryker Suite.
 * For full license information, please view the LICENSE file that was distributed with this source code.
 */

namespace SprykerSdkTest\LoadTesting\Fixtures;

use Codeception\Actor;
use SprykerTest\Shared\Testify\Fixtures\FixturesExporterInterface;
use SprykerTest\Shared\Testify\Fixtures\FixturesTrait;

class LoadTestingAbstractProductLocalizedAttributesTester extends Actor implements FixturesExporterInterface
{
    use _generated\LoadTestingAbstractProductLocalizedAttributesTesterActions;
    use FixturesTrait;
}
