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

package utils

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import common.SessionKeys
import config.ErrorHandler
import mocks.MockAppConfig
import models.{MTDfBMandated, User}
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.i18n.{Lang, Messages, MessagesApi, MessagesImpl}
import play.api.inject.Injector
import play.api.mvc.{AnyContentAsEmpty, MessagesControllerComponents}
import play.api.test.FakeRequest
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.test.UnitSpec
import views.html.errors.ErrorTemplate
import uk.gov.hmrc.play.bootstrap.tools.Stubs.stubMessagesControllerComponents

import scala.concurrent.ExecutionContext

trait TestUtils extends UnitSpec with GuiceOneAppPerSuite {

  lazy val injector: Injector = app.injector
  implicit lazy val mcc: MessagesControllerComponents = stubMessagesControllerComponents()
  implicit lazy val messagesApi: MessagesApi = injector.instanceOf[MessagesApi]
  implicit lazy val messages: Messages = MessagesImpl(Lang("en-GB"), messagesApi)
  implicit val hc: HeaderCarrier = HeaderCarrier()
  implicit val ec: ExecutionContext = mcc.executionContext
  implicit val appConfig: MockAppConfig = new MockAppConfig(app.configuration)
  implicit val system: ActorSystem = ActorSystem("Sys")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest().withSession(SessionKeys.insolventWithoutAccessKey -> "false")
  val clientUser: User[AnyContentAsEmpty.type] = User("999999999")(request)
  val agentUser: User[AnyContentAsEmpty.type] = User("999999999", arn = Some("XARN1234567"))(request)

  lazy val requestWithClientVRN: FakeRequest[AnyContentAsEmpty.type] =
    FakeRequest().withSession(SessionKeys.clientVrn -> "123456789")

  lazy val requestPredicatedAgent: FakeRequest[AnyContentAsEmpty.type] =
    FakeRequest().withSession(SessionKeys.clientVrn -> "123456789",
      SessionKeys.mandationStatus -> MTDfBMandated.value, SessionKeys.inflightMandationStatus -> "false")

  lazy val requestPredicatedClient: FakeRequest[AnyContentAsEmpty.type] =
    FakeRequest().withSession(
      SessionKeys.mandationStatus -> MTDfBMandated.value, SessionKeys.insolventWithoutAccessKey -> "false",
      SessionKeys.inflightMandationStatus -> "false")

  lazy val requestInsolvent: FakeRequest[AnyContentAsEmpty.type] =
    FakeRequest().withSession(SessionKeys.insolventWithoutAccessKey -> "true")

  lazy val requestNoInsolventSessionKey: FakeRequest[AnyContentAsEmpty.type] = FakeRequest()

  lazy val agentUserWithClient: User[AnyContentAsEmpty.type] = User("999999999", arn = Some("XARN1234567"))(requestWithClientVRN)

  lazy val mockErrorHandler: ErrorHandler = new ErrorHandler(messagesApi, appConfig, injector.instanceOf[ErrorTemplate])

}
