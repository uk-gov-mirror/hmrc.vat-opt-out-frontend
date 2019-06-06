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

package controllers.predicates

import audit.AuditService
import audit.models.GetCustomerInfoAuditModel
import common.SessionKeys.{inflightMandationStatus, mandationStatus}
import config.{AppConfig, ErrorHandler}
import javax.inject.{Inject, Singleton}
import models.{NonMTDfB, User}
import play.api.Logger
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.Results.{Ok, Redirect}
import play.api.mvc.{ActionRefiner, Result}
import services.VatSubscriptionService
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.HeaderCarrierConverter

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OptOutPredicate @Inject()(vatSubscriptionService: VatSubscriptionService,
                                val errorHandler: ErrorHandler,
                                val messagesApi: MessagesApi,
                                auditService: AuditService,
                                implicit val appConfig: AppConfig,
                                implicit val ec: ExecutionContext)
  extends ActionRefiner[User, User] with I18nSupport {

  override def refine[A](request: User[A]): Future[Either[Result, User[A]]] = {

    implicit val hc: HeaderCarrier = HeaderCarrierConverter.fromHeadersAndSession(request.headers, Some(request.session))
    implicit val req: User[A] = request

    val getSessionAttribute: String => Option[String] = req.session.get

    (getSessionAttribute(inflightMandationStatus), getSessionAttribute(mandationStatus)) match {
      case (_, Some(NonMTDfB.value)) =>
        Future.successful(Left(Ok(views.html.alreadyOptedOut())))
      case (Some("true"), _) =>
        Future.successful(Left(errorHandler.showInternalServerError))
      case (Some("false"), _) =>
        Future.successful(Right(req))
      case _ =>
        getCustomerInfoCall(req.vrn)
    }
  }

  private def getCustomerInfoCall[A](vrn: String)(implicit hc: HeaderCarrier,
                                                  request: User[A]): Future[Either[Result, User[A]]] =
    vatSubscriptionService.getCustomerInfo(vrn).map {
      case Right(customerInfo) =>

        val auditModel = GetCustomerInfoAuditModel(
          request.vrn,
          request.arn,
          request.isAgent,
          customerInfo.mandationStatus,
          customerInfo.inflightMandationStatus
        )
        auditService.audit(auditModel, Some(controllers.routes.TurnoverThresholdController.show().url))

        (customerInfo.inflightMandationStatus, customerInfo.mandationStatus) match {
          case (_, NonMTDfB) =>
            Logger.warn("[OptOutPredicate][getCustomerInfoCall] - " +
              "Mandation status is NonMTDfB. Rendering already opted out error page.")
            Left(Ok(views.html.alreadyOptedOut()).addingToSession(mandationStatus -> NonMTDfB.value))
          case (true, _) =>
            Logger.warn("[OptOutPredicate][getCustomerInfoCall] - " +
              "Mandation status is inflight. Rendering standard error page.")
            Left(errorHandler.showInternalServerError.addingToSession(inflightMandationStatus -> "true"))
          case (false, mandStatus) =>
            Logger.debug("[OptOutPredicate][getCustomerInfoCall] -"
              + "Mandation status is not in flight and not NonMTDfB. Redirecting user to the start of the journey.")
            Left(Redirect(controllers.routes.TurnoverThresholdController.show().url)
              .addingToSession(
                mandationStatus -> mandStatus.value,
                inflightMandationStatus -> "false"
              )
            )
        }
      case Left(error) =>
        Logger.warn(s"[OptOutPredicate][getCustomerInfoCall] - The call to the GetCustomerInfo API failed. Error: ${error.body}")
        Left(errorHandler.showInternalServerError)
    }
}
