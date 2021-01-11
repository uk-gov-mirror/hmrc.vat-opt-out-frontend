/*
 * Copyright 2021 HM Revenue & Customs
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

package assets

import models.{CustomerInformation, MTDfBMandated, MandationStatus}
import play.api.libs.json.{JsObject, Json}

object CustomerInformationConstants {


  def customerInfoJson(mandationStatus: String): JsObject = Json.obj(
    "mandationStatus" -> mandationStatus
  )

  val customerInfoJsonPending: JsObject = customerInfoJson("MTDfB Mandated") ++ Json.obj(
    "pendingChanges" -> Json.obj(
      "mandationStatus" -> "Non MTDfB"
    )
  )

  val customerInfoJsonInvalid: JsObject = Json.obj(

    "mandationStatusInvalid" -> "MTDfB Mandated"
  )

  def customerInfoModel(mandationStatus: MandationStatus): CustomerInformation =
    CustomerInformation(  mandationStatus, inflightMandationStatus = false)

  val customerInfoModelPending =
    CustomerInformation( MTDfBMandated, inflightMandationStatus = true)
}
