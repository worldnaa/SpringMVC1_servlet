package hello.servlet.web.frontcontroller.v3;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "frontControllerServletV3", urlPatterns = "/front-controller/v3/*")
public class FrontControllerServletV3 extends HttpServlet {

    private Map<String, ControllerV3> controllerMap = new HashMap<>();

    public FrontControllerServletV3() {
        controllerMap.put("/front-controller/v3/members/new-form", new MemberFormControllerV3());
        controllerMap.put("/front-controller/v3/members/save", new MemberSaveControllerV3());
        controllerMap.put("/front-controller/v3/members", new MemberListControllerV3());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        ControllerV3 controller = controllerMap.get(requestURI);
        if (controller == null) { // 예외처리
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        /* FrontControllerServletV2 의 코드
        MyView view = controller.process(request, response);
        view.render(request, response); */
        
        Map<String, String> paramMap = createParamMap(request); // Ctrl + Alt + M : 메서드추출

        ModelView mv = controller.process(paramMap); // ControllerV3.process 는 ModelView 를 리턴
        System.out.println("mv = " + mv); //ex) mv = ModelView{viewName='save-result', model={member=hello.servlet.domain.member.Member@78924dad}}

        String viewName = mv.getViewName(); //논리이름이 담겨있다 (ex. new-form)

        MyView view = viewResolver(viewName); // Ctrl + Alt + M : 메서드추출
        System.out.println("view = " + view); // ex) view = MyView{viewPath='/WEB-INF/views/save-result.jsp'}

        view.render(mv.getModel(), request, response);
    }

    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    private Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        
        // getParameterNames() => 파라미터의 모든 이름 꺼내기
        // .asIterator().forEachRemaining() => 반복문과 비슷
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
                // 1. getParameterNames() 에서 꺼낸 이름을 paramName 에 담는다
                // 2. key를 paramName , value를 request.getParameter(paramName) 으로 해서 paramMap 에 담는다

        System.out.println("paramMap = " + paramMap); //ex. paramMap = {age=20, username=kim}
        
        return paramMap;
    }
}
