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

package views

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class ConfirmationViewSpec extends ViewBaseSpec {

  "The confirmation page" should {

    lazy val view = views.html.confirmation()
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have the correct title" in {
      document.title shouldBe "Request to opt out of Making Tax Digital for VAT received"
    }

    "have the correct heading" in {
      elementText("h1") shouldBe "Request to opt out of Making Tax Digital for VAT received"
    }

    "have the correct subheading" in {
      elementText("h2") shouldBe "What happens next"
    }

    "have the correct first paragraph" in {
      elementText("#content p:nth-of-type(1)") shouldBe "We will send you an email within 2 working days with an " +
        "update, followed by a letter to your principal place of business. You can also go to your HMRC secure " +
        "messages to find out if your request has been accepted."
    }

    "have the correct second paragraph" in {
      elementText("#content p:nth-of-type(2)") shouldBe "Make sure your contact details are up to date."
    }

    "have a button which" should {

      "have the correct text" in {
        elementText(".button") shouldBe "Finish"
      }

      "have the correct href" in {
        element(".button").attr("href") shouldBe appConfig.manageVatUrl
      }
    }
  }
}
