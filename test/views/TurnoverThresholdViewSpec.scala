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

package views

import common.Constants.{optionNo, optionYes}
import forms.TurnoverThresholdForm.turnoverThresholdForm
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.api.data.Form
import play.twirl.api.Html
import views.html.TurnoverThresholdView

class TurnoverThresholdViewSpec extends ViewBaseSpec {

  val injectedView: TurnoverThresholdView = injector.instanceOf[TurnoverThresholdView]

  object Selectors {
    val pageHeading = "#content h1"
    val backLink = ".govuk-back-link"
    val backLinkPreviousYears = "#content > div > a"
    val hintText = "#label-email-hint"
    val form = "form"
    val radioYesField = "#threshold-yes"
    val radioNoField = "#threshold-no"
    val continueButton = ".govuk-button"
    val errorSummary = "#error-summary-title"
    val radioYesLabel = "label[for=threshold-yes]"
    val radioNoLabel = "label[for=threshold-no]"
    val errorSummaryLink = ".govuk-list > li:nth-child(1) > a:nth-child(1)"
    val errorRadioText = "#threshold-error"
  }

  val emptyForm: Form[String] = turnoverThresholdForm(appConfig.thresholdAmount)

  "Rendering the turnover threshold page with an empty form for a client" when {

    "the form has no errors" should {

      lazy val view: Html = injectedView(emptyForm)(messages, appConfig, clientUser)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "have the correct document title" in {
        document.title shouldBe "Has the business???s taxable turnover been above ??85,000 since 1 April 2019? - Business tax account - GOV.UK"
      }

      "have a back link" which {

        "should have the correct text" in {
          elementText(Selectors.backLink) shouldBe "Back"
        }

        "should have the correct back link" in {
          element(Selectors.backLink).attr("href") shouldBe appConfig.vatSummaryServicePath
        }
      }

      "have a page heading" which {

        "has the correct text" in {
          elementText(Selectors.pageHeading) shouldBe "Has the business???s taxable turnover been above ??85,000 since 1 April 2019?"
        }
      }

      "have the turnover threshold form with the correct form action" in {
        element(Selectors.form).attr("action") shouldBe controllers.routes.TurnoverThresholdController.submit().url
      }

      "have the radio button no field that have the correct post value" in {
        element(Selectors.radioNoField).attr("value") shouldBe optionNo
      }

      "have the radio button yes field that have the correct post value" in {
        element(Selectors.radioYesField).attr("value") shouldBe optionYes
      }

      "have the correct radio button yes text" in {
        elementText(Selectors.radioYesLabel) shouldBe "Yes"
      }

      "have the correct radio button no text" in {
        elementText(Selectors.radioNoLabel) shouldBe "No"
      }

      "have a continue button" which {

        "has the correct text" in {
          elementText(Selectors.continueButton) shouldBe "Continue"
        }
      }
    }

    "the form has errors" should {

      lazy val view = injectedView(
        turnoverThresholdForm(appConfig.thresholdAmount).bind(Map[String,String]())
      )(messages, appConfig, clientUser)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "have the correct title" in {
        document.title shouldBe "Error: Has the business???s taxable turnover been above ??85,000 since 1 April 2019? - Business tax account - GOV.UK"
      }

      "display the error summary" in {
        element(Selectors.errorSummary).text() shouldBe "There is a problem"
      }

      "display the error summary link" which {

        "has the correct text" in {
          elementText(Selectors.errorSummaryLink) shouldBe "Select yes if the business???s taxable turnover is above ??85,000"
        }

        "has the correct href" in {
          element(Selectors.errorSummaryLink).attr("href") shouldBe "#threshold-yes"
        }
      }

      "display the radio button error text" in {
        elementText(Selectors.errorRadioText) shouldBe "Error: Select yes if the business???s taxable turnover is above ??85,000"
      }
    }
  }

  "Rendering the turnover threshold page for a client with a populated yes option" should {

    lazy val view = injectedView(
      turnoverThresholdForm(appConfig.thresholdAmount).bind(Map("threshold" -> optionYes))
    )(messages, appConfig, clientUser)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have a checked 'Yes' radio option" in {
      elementAttributes(Selectors.radioYesField).get("checked").isDefined
    }

    "have an unchecked 'No' radio option" in {
      elementAttributes(Selectors.radioNoField).get("checked").isEmpty
    }
  }

  "Rendering the turnover threshold page for a client with a populated no option" should {

    lazy val view = injectedView(
      turnoverThresholdForm(appConfig.thresholdAmount).bind(Map("threshold" -> optionNo))
    )(messages, appConfig, clientUser)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have a checked 'No' radio option" in {
      elementAttributes(Selectors.radioNoField).get("checked").isDefined
    }

    "have an unchecked 'Yes' radio option" in {
      elementAttributes(Selectors.radioYesField).get("checked").isEmpty
    }
  }

  "Rendering the turnover threshold page for an agent" should {

    lazy val view: Html = injectedView(emptyForm)(messages, appConfig, agentUser)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have the correct document title" in {
      document.title shouldBe "Has the business???s taxable turnover been above ??85,000 since 1 April 2019? - Your client???s VAT details - GOV.UK"
    }

    "have a back link with the correct link location" in {
      element(Selectors.backLink).attr("href") shouldBe appConfig.agentClientHubPath
    }
  }
}
