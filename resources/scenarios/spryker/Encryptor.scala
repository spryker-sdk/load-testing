/*
 * Copyright 2011-2019 GatlingCorp (https://gatling.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package spryker

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import java.util.Base64

object Encryptor {
    val algorythm = "HmacSHA256"
    val feSalt = "MZ-4h5N~mg"

    def generateHMAC(sharedSecret: String, preHashString: String): String = {
        val secret = new SecretKeySpec(sharedSecret.getBytes, algorythm)
        val mac = Mac.getInstance(algorythm)
        mac.init(secret)

        Base64.getEncoder.encodeToString(mac.doFinal(preHashString.getBytes))
    }

    def getNotificationHash(chargetotal: String, currency: String, txndatetime: String, storename: String, approval_code:String): String = {
        val toBeHashedString = chargetotal + "|" + currency + "|" + txndatetime + "|" + storename + "|" + approval_code;
        generateHMAC(toBeHashedString, feSalt);
    }

    def getCustomPropHash(oid: String, storename: String, currency: String, chargetotal: String, customParam_SuccessFeUrl: String, customParam_FailureFeUrl: String): String = {
        val toBeHashedString2 = oid + "|" + storename + "|" + currency + "|" + chargetotal + "|" + customParam_SuccessFeUrl + "|" + customParam_FailureFeUrl;
        generateHMAC(toBeHashedString2, feSalt);
    }
}
