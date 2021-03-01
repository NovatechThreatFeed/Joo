package services

import javax.inject.{Inject, Singleton}

import scala.concurrent.{ExecutionContext, Future}

import play.api.Logger
import play.api.libs.json._

import akka.NotUsed
import akka.stream.scaladsl.Source
import models._

import org.elastic4play.controllers.Fields
import org.elastic4play.database.ModifyConfig
import org.elastic4play.services._

@Singleton
class DashboardSrv @Inject()(
    dashboardModel: DashboardModel,
    createSrv: CreateSrv,
    getSrv: GetSrv,
    updateSrv: UpdateSrv,
    deleteSrv: DeleteSrv,
    findSrv: FindSrv
) {

  private[DashboardSrv] lazy val logger = Logger(getClass)

  def create(fields: Fields)(implicit authContext: AuthContext, ec: ExecutionContext): Future[Dashboard] =
    createSrv[DashboardModel, Dashboard](dashboardModel, fields)

  def get(id: String)(implicit ec: ExecutionContext): Future[Dashboard] =
    getSrv[DashboardModel, Dashboard](dashboardModel, id)

  def update(id: String, fields: Fields)(implicit authContext: AuthContext, ec: ExecutionContext): Future[Dashboard] =
    update(id, fields, ModifyConfig.default)

  def update(id: String, fields: Fields, modifyConfig: ModifyConfig)(implicit authContext: AuthContext, ec: ExecutionContext): Future[Dashboard] =
    updateSrv[DashboardModel, Dashboard](dashboardModel, id, fields, modifyConfig)

  def update(dashboard: Dashboard, fields: Fields)(implicit authContext: AuthContext, ec: ExecutionContext): Future[Dashboard] =
    update(dashboard, fields, ModifyConfig.default)

  def update(dashboard: Dashboard, fields: Fields, modifyConfig: ModifyConfig)(
      implicit authContext: AuthContext,
      ec: ExecutionContext
  ): Future[Dashboard] =
    updateSrv(dashboard, fields, modifyConfig)

  def delete(id: String)(implicit authContext: AuthContext, ec: ExecutionContext): Future[Dashboard] =
    deleteSrv[DashboardModel, Dashboard](dashboardModel, id)

  def find(queryDef: QueryDef, range: Option[String], sortBy: Seq[String])(
      implicit ec: ExecutionContext
  ): (Source[Dashboard, NotUsed], Future[Long]) =
    findSrv[DashboardModel, Dashboard](dashboardModel, queryDef, range, sortBy)

  def stats(queryDef: QueryDef, aggs: Seq[Agg])(implicit ec: ExecutionContext): Future[JsObject] = findSrv(dashboardModel, queryDef, aggs: _*)
}
