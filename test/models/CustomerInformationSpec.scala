/*
 * Copyright 2019 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package models

import assets.CustomerInformationConstants._
import utils.TestUtils

class CustomerInformationSpec extends TestUtils {

  "CustomerInformation" should {

    "successfully parse from JSON" when {

      "all expected fields are present" in {
        customerInfoJsonAll.as[CustomerInformation] shouldBe customerInfoModelTradeName
      }

      "there is only an organisation name present" in {
        customerInfoJsonOrg.as[CustomerInformation] shouldBe customerInfoModelOrgName
      }

      "there is only a first and last name present" in {
        customerInfoJsonInd.as[CustomerInformation] shouldBe customerInfoModelIndName
      }

      "there are no names present" when {

        "there is a mandation status of MTDfB Mandatory" in {
          customerInfoJsonNoName(MTDfBMandated.value).as[CustomerInformation] shouldBe customerInfoModelNoName(MTDfBMandated)
        }

        "there is a mandation status of MTDfB Voluntary" in {
          customerInfoJsonNoName(MTDfBVoluntary.value).as[CustomerInformation] shouldBe customerInfoModelNoName(MTDfBVoluntary)
        }

        "there is a mandation status of Non MTDfB" in {
          customerInfoJsonNoName(NonMTDfB.value).as[CustomerInformation] shouldBe customerInfoModelNoName(NonMTDfB)
        }

        "there is a mandation status of Non Digital" in {
          customerInfoJsonNoName(NonDigital.value).as[CustomerInformation] shouldBe customerInfoModelNoName(NonDigital)
        }
      }

      "there is a pending mandation status" in {
        customerInfoJsonPending.as[CustomerInformation] shouldBe customerInfoModelPending
      }
    }

    "fail to parse from JSON" when {

      "the JSON is in an invalid format" in {
        intercept[Exception](customerInfoJsonInvalid.as[CustomerInformation])
      }

      "there is a mandation status that is not recognised" in {
        intercept[Exception](customerInfoJsonNoName("VATDEC Mandatory").as[CustomerInformation])
      }
    }
  }
}
