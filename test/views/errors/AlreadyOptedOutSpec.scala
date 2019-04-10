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

package views.errors

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import views.ViewBaseSpec

class AlreadyOptedOutSpec extends ViewBaseSpec{

  val title = "You have already opted out of Making Tax Digital for VAT"
  val heading = "You have already opted out of Making Tax Digital for VAT"
  val content = "You do not need to use compatible software to submit your VAT Returns to HMRC."

  "Rendering the error template page" should {

    lazy val view = views.html.errors.alreadyOptedOut()
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have the correct document title" in {
      document.title shouldBe title
    }

    "have the correct page heading" in {
      elementText("#content h1") shouldBe heading
    }

    "have the correct explanation on the page" in {
      elementText("#content p") shouldBe content
    }

    "have a back link" which {

      "has the correct text" in {
        elementText(".link-back") shouldBe "Back"
      }

      "has the correct href" in {
        element(".link-back").attr("href") shouldBe "/change-business-details"
      }
    }

  }
}
