<?php

/**
 * Copyright © 2016-present Spryker Systems GmbH. All rights reserved.
 * Use of this software requires acceptance of the Evaluation License Agreement. See LICENSE file.
 */

namespace SprykerSdk\Yves\LoadTesting\Plugin\Router;

use Spryker\Yves\Router\Plugin\RouteProvider\AbstractRouteProviderPlugin;
use Spryker\Yves\Router\Route\RouteCollection;

class LoadTestingRouterProviderPlugin extends AbstractRouteProviderPlugin
{
    public const ROUTE_NAME_PLACE_ORDER_DEBUG = 'place-order-debug';

    /**
     * @param \Spryker\Yves\Router\Route\RouteCollection $routeCollection
     *
     * @return \Spryker\Yves\Router\Route\RouteCollection
     */
    public function addRoutes(RouteCollection $routeCollection): RouteCollection
    {
        $routeCollection = $this->addPlaceOrderDebugRoute($routeCollection);

        return $routeCollection;
    }

    /**
     * @param \Spryker\Yves\Router\Route\RouteCollection $routeCollection
     *
     * @return \Spryker\Yves\Router\Route\RouteCollection
     */
    protected function addPlaceOrderDebugRoute(RouteCollection $routeCollection): RouteCollection
    {
        $route = $this->buildRoute('/place-order-debug', 'LoadTesting', 'Checkout', 'placeOrderDebugAction');
        $route = $route->setMethods(['POST']);
        $routeCollection->add(static::ROUTE_NAME_PLACE_ORDER_DEBUG, $route);

        return $routeCollection;
    }
}
