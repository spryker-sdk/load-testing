<?php

/**
 * Copyright © 2016-present Spryker Systems GmbH. All rights reserved.
 * Use of this software requires acceptance of the Evaluation License Agreement. See LICENSE file.
 */

namespace SprykerSdkTest\LoadTesting\Fixtures\Helper;

use Codeception\Module;
use Generated\Shared\DataBuilder\CompanyBuilder;
use Generated\Shared\DataBuilder\CompanyBusinessUnitBuilder;
use Generated\Shared\DataBuilder\CompanyRoleBuilder;
use Generated\Shared\DataBuilder\CompanyUserBuilder;
use Generated\Shared\Transfer\CompanyBusinessUnitTransfer;
use Generated\Shared\Transfer\CompanyRoleTransfer;
use Generated\Shared\Transfer\CompanyTransfer;
use Generated\Shared\Transfer\CompanyUserTransfer;
use Generated\Shared\Transfer\PermissionCollectionTransfer;
use Orm\Zed\CompanyBusinessUnit\Persistence\SpyCompanyBusinessUnitQuery;
use Spryker\Zed\CompanyBusinessUnit\Business\CompanyBusinessUnitFacadeInterface;
use Spryker\Zed\CompanyRole\Business\CompanyRoleFacadeInterface;
use Spryker\Zed\CompanyUser\Business\CompanyUserFacadeInterface;
use SprykerTest\Shared\Testify\Helper\LocatorHelperTrait;

class CustomerHelper extends Module
{
    use LocatorHelperTrait;

    protected const COMPANY_USER_PERMISSIONS_KEY_LIST = [
        'AddCartItemPermissionPlugin',
        'ChangeCartItemPermissionPlugin',
        'RemoveCartItemPermissionPlugin',
        'PlaceOrderWithAmountUpToPermissionPlugin',
        'PlaceOrderPermissionPlugin',
        'SeeBusinessUnitOrdersPermissionPlugin',
    ];

    /**
     * @return bool
     */
    public function canAssignUserToCompany(): bool
    {
        return class_exists(CompanyTransfer::class);
    }

    /**
     * @return \Generated\Shared\Transfer\PermissionCollectionTransfer
     */
    public function createPermissionCollectionTransferWithPlaceOrderPermissions(): PermissionCollectionTransfer
    {
        $availablePermissions = $this->getLocator()->permission()->facade()
            ->findAll()
            ->getPermissions();

        $permissionCollectionTransfer = new PermissionCollectionTransfer();

        foreach ($availablePermissions as $permissionTransfer) {
            if (in_array($permissionTransfer->getKey(), static::COMPANY_USER_PERMISSIONS_KEY_LIST, true)) {
                $permissionCollectionTransfer->addPermission($permissionTransfer);
            }
        }

        return $permissionCollectionTransfer;
    }

    /**
     * @param array $seedData
     *
     * @return \Generated\Shared\Transfer\CompanyTransfer
     */
    public function haveCompany(array $seedData = []): CompanyTransfer
    {
        $seedData[CompanyTransfer::IS_ACTIVE] = true;

        $companyTransfer = (new CompanyBuilder($seedData))->build();
        $companyTransfer->setIdCompany(null);

        return $this->getLocator()->company()->facade()->create($companyTransfer)->getCompanyTransfer();
    }

    /**
     * @param array $companyRole
     *
     * @return \Generated\Shared\Transfer\CompanyRoleTransfer
     */
    public function haveCompanyRole(array $companyRole = []): CompanyRoleTransfer
    {
        $companyRoleTransfer = (new CompanyRoleBuilder($companyRole))->build();

        return $this->getCompanyRoleFacade()
            ->create($companyRoleTransfer)
            ->getCompanyRoleTransfer();
    }

    /**
     * @param \Generated\Shared\Transfer\CompanyUserTransfer $companyUserTransfer
     *
     * @return void
     */
    public function assignCompanyRolesToCompanyUser(CompanyUserTransfer $companyUserTransfer): void
    {
        $companyUserTransfer->requireCompanyRoleCollection();

        $this->getCompanyRoleFacade()
            ->saveCompanyUser($companyUserTransfer);
    }

    /**
     * @param array $seedData
     *
     * @return \Generated\Shared\Transfer\CompanyBusinessUnitTransfer
     */
    public function haveCompanyBusinessUnit(array $seedData = []): CompanyBusinessUnitTransfer
    {
        $companyBusinessUnitTransfer = (new CompanyBusinessUnitBuilder($seedData))->build();
        $companyBusinessUnitTransfer->setIdCompanyBusinessUnit(null);

        $this->ensureCompanyBusinessUnitWithKeyDoesNotExist($companyBusinessUnitTransfer->getKey());

        return $this->getCompanyBusinessUnitFacade()
            ->create($companyBusinessUnitTransfer)
            ->getCompanyBusinessUnitTransfer();
    }

    /**
     * @param array $seed
     *
     * @return \Generated\Shared\Transfer\CompanyUserTransfer
     */
    public function haveCompanyUser(array $seed = []): CompanyUserTransfer
    {
        $companyUserTransfer = (new CompanyUserBuilder($seed))->build();
        $companyUserTransfer->setIdCompanyUser(null);

        $companyUserTransfer->requireCustomer();

        $companyUserResponseTransfer = $this->getCompanyUserFacade()->create($companyUserTransfer);

        return $companyUserResponseTransfer->getCompanyUser();
    }

    /**
     * @return \Spryker\Zed\CompanyUser\Business\CompanyUserFacadeInterface
     */
    protected function getCompanyUserFacade(): CompanyUserFacadeInterface
    {
        return $this->getLocator()->companyUser()->facade();
    }

    /**
     * @return \Spryker\Zed\CompanyBusinessUnit\Business\CompanyBusinessUnitFacadeInterface
     */
    protected function getCompanyBusinessUnitFacade(): CompanyBusinessUnitFacadeInterface
    {
        return $this->getLocator()->companyBusinessUnit()->facade();
    }

    /**
     * @param string $key
     *
     * @return void
     */
    protected function ensureCompanyBusinessUnitWithKeyDoesNotExist(string $key): void
    {
        $companyBusinessUnitQuery = SpyCompanyBusinessUnitQuery::create();
        $companyBusinessUnitQuery->filterByKey($key)->delete();
    }

    /**
     * @return \Spryker\Zed\CompanyRole\Business\CompanyRoleFacadeInterface
     */
    protected function getCompanyRoleFacade(): CompanyRoleFacadeInterface
    {
        return $this->getLocator()
            ->companyRole()
            ->facade();
    }
}
