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

package controllers

import common.SessionKeys.turnoverThreshold
import common.{Constants, SessionKeys}
import config.AppConfig
import controllers.predicates.{AuthPredicate, OptOutPredicate}
import forms.TurnoverThresholdForm._
import javax.inject.{Inject, Singleton}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import views.html.TurnoverThresholdView

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TurnoverThresholdController @Inject()(authenticate: AuthPredicate,
                                            val optOutPredicate: OptOutPredicate,
                                            turnoverThresholdView: TurnoverThresholdView)
                                           (implicit val appConfig: AppConfig,
                                            override val mcc: MessagesControllerComponents
                                           ) extends ControllerBase {
  implicit val ec: ExecutionContext = mcc.executionContext

  val show: Action[AnyContent] = (authenticate andThen optOutPredicate).async { implicit user =>
    user.session.get(turnoverThreshold) match {
      case Some(turnoverOption) =>
        Future.successful(Ok(turnoverThresholdView(turnoverThresholdForm(appConfig.thresholdAmount).fill(turnoverOption))))
      case _ =>
        Future.successful(Ok(turnoverThresholdView(turnoverThresholdForm(appConfig.thresholdAmount))))
    }
  }

  def submit: Action[AnyContent] = (authenticate andThen optOutPredicate) { implicit user =>
    turnoverThresholdForm(appConfig.thresholdAmount).bindFromRequest().fold(
      error => {
              BadRequest(turnoverThresholdView(error))
      },
      {
        case formData@Constants.optionYes => Redirect(controllers.routes.CannotOptOutController.show())
          .addingToSession(SessionKeys.turnoverThreshold -> formData)
        case formData@Constants.optionNo => Redirect(controllers.routes.ConfirmOptOutController.show())
          .addingToSession(SessionKeys.turnoverThreshold -> formData)
      }
    )
  }
}
