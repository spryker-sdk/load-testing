<?php

/**
 * Copyright © 2016-present Spryker Systems GmbH. All rights reserved.
 * Use of this software requires acceptance of the Evaluation License Agreement. See LICENSE file.
 */

namespace SprykerSdkTest\LoadTesting\Fixtures\Helper;

trait LoadTestingCsvDemoDataLoaderTrait
{
    /**
     * @return string
     */
    abstract protected function getFileName(): string;

    /**
     * @return string[]
     */
    abstract protected function getRequiredFields(): array;

    /**
     * @return array
     */
    protected function loadDemoData(): array
    {
        $filePath = codecept_data_dir() . $this->getFileName();
        $file = fopen($filePath, 'rb');

        $requiredFields = $this->getRequiredFields();
        if (!$this->validateDemoDataHeader(fgetcsv($file, 1000), $requiredFields)) {
            return [];
        }

        $demoData = [];
        while ($data = fgetcsv($file, 1000)) {
            $demoData[] = array_combine($requiredFields, $data);
        }

        $firstLine = getenv('FIXTURE_FIRST_LINE');
        $lastLine = getenv('FIXTURE_LAST_LINE');

        if ($firstLine !== false && $lastLine !== false) {
            if (!is_numeric($firstLine) && !is_numeric($lastLine)) {
                throw new \RuntimeException('[FIXTURE_FIRST_LINE] and [FIXTURE_LAST_LINE] must be numeric');
            }

            $demoData = array_slice($demoData, (int) $firstLine - 1, (int) $lastLine - (int) $firstLine + 1);
        }

        return $demoData;
    }

    /**
     * @param array $csvHeader
     * @param array $requiredFields
     *
     * @return bool
     */
    protected function validateDemoDataHeader(array $csvHeader, array $requiredFields): bool
    {
        return array_diff($requiredFields, $csvHeader) === [];
    }
}
