// @SOURCE:C:/AndroidProjects/RedAlertGCMService/conf/routes
// @HASH:3d42735e2232bc9fe3529fc18b81999efb688056
// @DATE:Tue Apr 14 16:14:08 IDT 2015

import Routes.{prefix => _prefix, defaultPrefix => _defaultPrefix}
import play.core._
import play.core.Router._
import play.core.Router.HandlerInvokerFactory._
import play.core.j._

import play.api.mvc._
import _root_.controllers.Assets.Asset
import _root_.play.libs.F

import Router.queryString


// @LINE:13
// @LINE:10
// @LINE:8
// @LINE:7
// @LINE:6
package controllers {

// @LINE:13
// @LINE:6
class ReverseAssets {


// @LINE:13
// @LINE:6
def at(file:String): Call = {
   (file: @unchecked) match {
// @LINE:6
case (file) if file == "index.html" =>
  implicit val _rrc = new ReverseRouteContext(Map(("path", "/public"), ("file", "index.html")))
  Call("GET", _prefix)
                                         
// @LINE:13
case (file)  =>
  implicit val _rrc = new ReverseRouteContext(Map(("path", "/public")))
  Call("GET", _prefix + { _defaultPrefix } + "assets/" + implicitly[PathBindable[String]].unbind("file", file))
                                         
   }
}
                                                

}
                          

// @LINE:8
// @LINE:7
class ReversePush {


// @LINE:7
def register(regId:String): Call = {
   import ReverseRouteContext.empty
   Call("GET", _prefix + { _defaultPrefix } + "push/register/" + implicitly[PathBindable[String]].unbind("regId", dynamicString(regId)))
}
                        

// @LINE:8
def unregister(regId:String): Call = {
   import ReverseRouteContext.empty
   Call("GET", _prefix + { _defaultPrefix } + "push/unregister/" + implicitly[PathBindable[String]].unbind("regId", dynamicString(regId)))
}
                        

}
                          

// @LINE:10
class ReverseApplication {


// @LINE:10
def sendExampleMessage(): Call = {
   import ReverseRouteContext.empty
   Call("GET", _prefix + { _defaultPrefix } + "test")
}
                        

}
                          
}
                  


// @LINE:13
// @LINE:10
// @LINE:8
// @LINE:7
// @LINE:6
package controllers.javascript {
import ReverseRouteContext.empty

// @LINE:13
// @LINE:6
class ReverseAssets {


// @LINE:13
// @LINE:6
def at : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Assets.at",
   """
      function(file) {
      if (file == """ + implicitly[JavascriptLitteral[String]].to("index.html") + """) {
      return _wA({method:"GET", url:"""" + _prefix + """"})
      }
      if (true) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "assets/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("file", file)})
      }
      }
   """
)
                        

}
              

// @LINE:8
// @LINE:7
class ReversePush {


// @LINE:7
def register : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Push.register",
   """
      function(regId) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "push/register/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("regId", encodeURIComponent(regId))})
      }
   """
)
                        

// @LINE:8
def unregister : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Push.unregister",
   """
      function(regId) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "push/unregister/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("regId", encodeURIComponent(regId))})
      }
   """
)
                        

}
              

// @LINE:10
class ReverseApplication {


// @LINE:10
def sendExampleMessage : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.sendExampleMessage",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "test"})
      }
   """
)
                        

}
              
}
        


// @LINE:13
// @LINE:10
// @LINE:8
// @LINE:7
// @LINE:6
package controllers.ref {


// @LINE:13
// @LINE:6
class ReverseAssets {


// @LINE:6
def at(path:String, file:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Assets.at(path, file), HandlerDef(this.getClass.getClassLoader, "", "controllers.Assets", "at", Seq(classOf[String], classOf[String]), "GET", """ Home page""", _prefix + """""")
)
                      

}
                          

// @LINE:8
// @LINE:7
class ReversePush {


// @LINE:7
def register(regId:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Push.register(regId), HandlerDef(this.getClass.getClassLoader, "", "controllers.Push", "register", Seq(classOf[String]), "GET", """""", _prefix + """push/register/$regId<[^/]+>""")
)
                      

// @LINE:8
def unregister(regId:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Push.unregister(regId), HandlerDef(this.getClass.getClassLoader, "", "controllers.Push", "unregister", Seq(classOf[String]), "GET", """""", _prefix + """push/unregister/$regId<[^/]+>""")
)
                      

}
                          

// @LINE:10
class ReverseApplication {


// @LINE:10
def sendExampleMessage(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Application.sendExampleMessage(), HandlerDef(this.getClass.getClassLoader, "", "controllers.Application", "sendExampleMessage", Seq(), "GET", """""", _prefix + """test""")
)
                      

}
                          
}
        
    