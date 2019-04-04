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

package config

object ConfigKeys {

  val whitelistEnabled: String = "whitelist.enabled"
  val whitelistedIps: String = "whitelist.allowedIps"
  val whitelistExcludedPaths: String = "whitelist.excludedPaths"
  val whitelistShutterPage: String = "whitelist.shutter-page-url"
  val vatOptOutServiceUrl: String = "vat-opt-out-frontend.url"
  val vatOptOutServicePath: String = "vat-opt-out-frontend.path"
  val signInBaseUrl: String = "signIn.url"
  val manageVatServiceUrl: String = "manage-vat-subscription-frontend.url"
  val thresholdPreviousYearsUrl: String = "thresholdPreviousYearsUrl"
  val vatSubscription: String = "vat-subscription"
}
