<?php

/**
 * Copyright © 2016-present Spryker Systems GmbH. All rights reserved.
 * Use of this software requires acceptance of the Evaluation License Agreement. See LICENSE file.
 */

namespace SprykerSdkTest\LoadTesting\Fixtures\Customer;

use Generated\Shared\Transfer\CompanyBusinessUnitTransfer;
use Generated\Shared\Transfer\CompanyRoleCollectionTransfer;
use Generated\Shared\Transfer\CompanyRoleTransfer;
use Generated\Shared\Transfer\CompanyUserTransfer;
use Generated\Shared\Transfer\CustomerTransfer;
use Generated\Shared\Transfer\QuoteTransfer;
use SprykerSdkTest\LoadTesting\Fixtures\Helper\LoadTestingCsvDemoDataLoaderTrait;
use SprykerSdkTest\LoadTesting\Fixtures\LoadTestingCustomerTester;
use SprykerTest\Shared\Testify\Fixtures\FixturesBuilderInterface;
use SprykerTest\Shared\Testify\Fixtures\FixturesContainerInterface;

/**
 * Auto-generated group annotations
 *
 * @group SprykerSdkTest
 * @group LoadTesting
 * @group Fixtures
 * @group Customer
 * @group CustomerFixtures
 * Add your own group annotations below this line
 */
class CustomerFixtures implements FixturesBuilderInterface, FixturesContainerInterface
{
    use LoadTestingCsvDemoDataLoaderTrait;

    protected const DEMO_DATA_FILE_NAME = 'customer.csv';
    protected const KEY_EMAIL = 'email';
    protected const KEY_PASSWORD = 'password';
    protected const KEY_AUTH_TOKEN = 'auth_token';
    protected const FIRST_NAME = 'first_name';
    protected const LAST_NAME = 'last_name';

    protected const COMPANY_ROLE_NAME = 'admin';

    /**
     * @param \SprykerSdkTest\LoadTesting\Fixtures\LoadTestingCustomerTester $I
     *
     * @return \SprykerTest\Shared\Testify\Fixtures\FixturesContainerInterface
     */
    public function buildFixtures(LoadTestingCustomerTester $I): FixturesContainerInterface
    {
        $this->createCustomer($I);

        return $this;
    }

    /**
     * @param \SprykerSdkTest\LoadTesting\Fixtures\LoadTestingCustomerTester $I
     *
     * @return void
     */
    protected function createCustomer(LoadTestingCustomerTester $I): void
    {
        $demoData = $this->loadDemoData();
        $quoteOverride = $this->buildQuoteOverrideData($I);
        $companyOverride = $this->buildCompanyUserOverride($I);

        foreach ($demoData as $customerData) {
            $customerOverride = $this->buildCustomerOverrideData($customerData);

            $customerTransfer = $I->haveCustomerWithPersistentQuote(
                $customerOverride,
                $quoteOverride
            );

            if (empty($companyOverride)) {
                continue;
            }

            $companyOverride[CompanyUserTransfer::CUSTOMER] = $customerTransfer;

            $companyUserTransfer = $I->haveCompanyUser($companyOverride);
            $I->assignCompanyRolesToCompanyUser($companyUserTransfer);
        }
    }

    /**
     * @param string[] $customerData
     *
     * @return string[]
     */
    protected function buildCustomerOverrideData(array $customerData): array
    {
        return [
            CustomerTransfer::EMAIL => $customerData[static::KEY_EMAIL],
            CustomerTransfer::PASSWORD => $customerData[static::KEY_PASSWORD],
            CustomerTransfer::NEW_PASSWORD => $customerData[static::KEY_PASSWORD],
            CustomerTransfer::FIRST_NAME => $customerData[static::FIRST_NAME],
            CustomerTransfer::LAST_NAME => $customerData[static::LAST_NAME],
        ];
    }

    /**
     * @param \SprykerSdkTest\LoadTesting\Fixtures\LoadTestingCustomerTester $I
     *
     * @return string[]
     */
    protected function buildQuoteOverrideData(LoadTestingCustomerTester $I): array
    {
        return [
            QuoteTransfer::STORE => $I->getStoreFacade()->getCurrentStore(),
        ];
    }

    /**
     * @param \SprykerSdkTest\LoadTesting\Fixtures\LoadTestingCustomerTester $I
     *
     * @return array
     */
    protected function buildCompanyUserOverride(LoadTestingCustomerTester $I): array
    {
        if (!$I->canAssignUserToCompany()) {
            return [];
        }

        $companyTransfer = $I->haveCompany();

        $companyRoleTransfer = $I->haveCompanyRole([
            CompanyRoleTransfer::NAME => static::COMPANY_ROLE_NAME,
            CompanyRoleTransfer::IS_DEFAULT => true,
            CompanyRoleTransfer::FK_COMPANY => $companyTransfer->getIdCompany(),
            CompanyRoleTransfer::PERMISSION_COLLECTION => $I->createPermissionCollectionTransferWithPlaceOrderPermissions(),
        ]);

        $companyBusinessUnitTransfer = $I->haveCompanyBusinessUnit([
            CompanyBusinessUnitTransfer::FK_COMPANY => $companyTransfer->getIdCompany(),
        ]);

        return [
            CompanyUserTransfer::FK_COMPANY => $companyTransfer->getIdCompany(),
            CompanyUserTransfer::FK_COMPANY_BUSINESS_UNIT => $companyBusinessUnitTransfer->getIdCompanyBusinessUnit(),
            CompanyUserTransfer::COMPANY_BUSINESS_UNIT => $companyBusinessUnitTransfer->getIdCompanyBusinessUnit(),
            CompanyUserTransfer::COMPANY_ROLE_COLLECTION => (new CompanyRoleCollectionTransfer())->addRole($companyRoleTransfer),
        ];
    }

    /**
     * @return string
     */
    protected function getFileName(): string
    {
        return static::DEMO_DATA_FILE_NAME;
    }

    /**
     * @return string[]
     */
    protected function getRequiredFields(): array
    {
        return [
            static::KEY_EMAIL,
            static::KEY_PASSWORD,
            static::KEY_AUTH_TOKEN,
            static::FIRST_NAME,
            static::LAST_NAME,
        ];
    }
}
