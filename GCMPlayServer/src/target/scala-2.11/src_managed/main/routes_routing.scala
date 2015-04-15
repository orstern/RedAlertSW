// @SOURCE:C:/AndroidProjects/RedAlertGCMService/conf/routes
// @HASH:3d42735e2232bc9fe3529fc18b81999efb688056
// @DATE:Tue Apr 14 16:14:08 IDT 2015


import play.core._
import play.core.Router._
import play.core.Router.HandlerInvokerFactory._
import play.core.j._

import play.api.mvc._
import _root_.controllers.Assets.Asset
import _root_.play.libs.F

import Router.queryString

object Routes extends Router.Routes {

import ReverseRouteContext.empty

private var _prefix = "/"

def setPrefix(prefix: String): Unit = {
  _prefix = prefix
  List[(String,Routes)]().foreach {
    case (p, router) => router.setPrefix(prefix + (if(prefix.endsWith("/")) "" else "/") + p)
  }
}

def prefix = _prefix

lazy val defaultPrefix = { if(Routes.prefix.endsWith("/")) "" else "/" }


// @LINE:6
private[this] lazy val controllers_Assets_at0_route = Route("GET", PathPattern(List(StaticPart(Routes.prefix))))
private[this] lazy val controllers_Assets_at0_invoker = createInvoker(
controllers.Assets.at(fakeValue[String], fakeValue[String]),
HandlerDef(this.getClass.getClassLoader, "", "controllers.Assets", "at", Seq(classOf[String], classOf[String]),"GET", """ Home page""", Routes.prefix + """"""))
        

// @LINE:7
private[this] lazy val controllers_Push_register1_route = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("push/register/"),DynamicPart("regId", """[^/]+""",true))))
private[this] lazy val controllers_Push_register1_invoker = createInvoker(
controllers.Push.register(fakeValue[String]),
HandlerDef(this.getClass.getClassLoader, "", "controllers.Push", "register", Seq(classOf[String]),"GET", """""", Routes.prefix + """push/register/$regId<[^/]+>"""))
        

// @LINE:8
private[this] lazy val controllers_Push_unregister2_route = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("push/unregister/"),DynamicPart("regId", """[^/]+""",true))))
private[this] lazy val controllers_Push_unregister2_invoker = createInvoker(
controllers.Push.unregister(fakeValue[String]),
HandlerDef(this.getClass.getClassLoader, "", "controllers.Push", "unregister", Seq(classOf[String]),"GET", """""", Routes.prefix + """push/unregister/$regId<[^/]+>"""))
        

// @LINE:10
private[this] lazy val controllers_Application_sendExampleMessage3_route = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("test"))))
private[this] lazy val controllers_Application_sendExampleMessage3_invoker = createInvoker(
controllers.Application.sendExampleMessage(),
HandlerDef(this.getClass.getClassLoader, "", "controllers.Application", "sendExampleMessage", Nil,"GET", """""", Routes.prefix + """test"""))
        

// @LINE:13
private[this] lazy val controllers_Assets_at4_route = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("assets/"),DynamicPart("file", """.+""",false))))
private[this] lazy val controllers_Assets_at4_invoker = createInvoker(
controllers.Assets.at(fakeValue[String], fakeValue[String]),
HandlerDef(this.getClass.getClassLoader, "", "controllers.Assets", "at", Seq(classOf[String], classOf[String]),"GET", """ Map static resources from the /public folder to the /assets URL path""", Routes.prefix + """assets/$file<.+>"""))
        
def documentation = List(("""GET""", prefix,"""controllers.Assets.at(path:String = "/public", file:String = "index.html")"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """push/register/$regId<[^/]+>""","""controllers.Push.register(regId:String)"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """push/unregister/$regId<[^/]+>""","""controllers.Push.unregister(regId:String)"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """test""","""controllers.Application.sendExampleMessage()"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """assets/$file<.+>""","""controllers.Assets.at(path:String = "/public", file:String)""")).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
  case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
  case l => s ++ l.asInstanceOf[List[(String,String,String)]]
}}
      

def routes:PartialFunction[RequestHeader,Handler] = {

// @LINE:6
case controllers_Assets_at0_route(params) => {
   call(Param[String]("path", Right("/public")), Param[String]("file", Right("index.html"))) { (path, file) =>
        controllers_Assets_at0_invoker.call(controllers.Assets.at(path, file))
   }
}
        

// @LINE:7
case controllers_Push_register1_route(params) => {
   call(params.fromPath[String]("regId", None)) { (regId) =>
        controllers_Push_register1_invoker.call(controllers.Push.register(regId))
   }
}
        

// @LINE:8
case controllers_Push_unregister2_route(params) => {
   call(params.fromPath[String]("regId", None)) { (regId) =>
        controllers_Push_unregister2_invoker.call(controllers.Push.unregister(regId))
   }
}
        

// @LINE:10
case controllers_Application_sendExampleMessage3_route(params) => {
   call { 
        controllers_Application_sendExampleMessage3_invoker.call(controllers.Application.sendExampleMessage())
   }
}
        

// @LINE:13
case controllers_Assets_at4_route(params) => {
   call(Param[String]("path", Right("/public")), params.fromPath[String]("file", None)) { (path, file) =>
        controllers_Assets_at4_invoker.call(controllers.Assets.at(path, file))
   }
}
        
}

}
     