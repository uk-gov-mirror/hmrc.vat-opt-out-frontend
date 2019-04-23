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

package mocks

import connectors.httpParsers.ContactPreferencesHttpParser.ContactPreferencesResponse

import org.mockito.ArgumentMatchers
import org.mockito.Mockito.{reset, when}
import org.scalatest.{BeforeAndAfterEach, Suite}
import services.ContactPreferencesService
import uk.gov.hmrc.http.HeaderCarrier
import org.scalatestplus.mockito.MockitoSugar

import scala.concurrent.{ExecutionContext, Future}

trait MockContactPreferencesService extends MockitoSugar {

  val mockContactPreferencesService: ContactPreferencesService = mock[ContactPreferencesService]

  def mockGetContactPreferences(vrn: String)(response: Future[ContactPreferencesResponse]): Unit =
    when(mockContactPreferencesService.getContactPreferences(
      ArgumentMatchers.eq(vrn)
    )(ArgumentMatchers.any[HeaderCarrier],
      ArgumentMatchers.any[ExecutionContext])) thenReturn response
}