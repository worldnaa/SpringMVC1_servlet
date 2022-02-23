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

        // URL에서 8080 뒤 '/'부터 가져온다 (ex. /front-controller/v3/members)
        String requestURI = request.getRequestURI();

        // 각 컨트롤러의 인스턴스 리턴 (ex. new MemberListControllerV3())
        ControllerV3 controller = controllerMap.get(requestURI);

        // 예외처리
        if (controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        // 1. 컨트롤러에 전달할 파라미터 정보(ex.age=20)를 Map에 담는다
        // 컨트롤러 인터페이스 : ModelView process( Map<String, String> paramMap );
        Map<String, String> paramMap = createParamMap(request);

        // 2. Map에 담긴 파라미터 정보를 인자로 넘기며, process() 실행
        // 2. 컨트롤러 process() 실행 시, ModelView("논리이름") 가 리턴 됨
        ModelView mv = controller.process(paramMap);

        // 3. ModelView 객체에서 viewName(논리이름) 을 꺼내와 변수 'viewName'에 담는다
        String viewName = mv.getViewName();

        // 4. 논리이름을 물리경로로 만들어, MyView 객체를 반환
        MyView view = viewResolver(viewName);
        System.out.println("view = " + view);

        // 5. MyView 의 render() 실행 => dispatcher.forward 실행
        view.render(mv.getModel(), request, response);
    }

    // 컨트롤러가 반환한 논리뷰 이름(viewName)을 실제 물리뷰 경로로 변경한다.
    // 그리고 실제 물리경로가 있는 MyView 객체를 반환한다.
    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    // HttpServletRequest 에서 파라미터 정보를 꺼내서 Map으로 변환한다.
    // 그리고 해당 Map(paramMap)을 컨트롤러에 전달하면서 호출한다.
    private Map<String, String> createParamMap(HttpServletRequest request) {

        Map<String, String> paramMap = new HashMap<>();
        
        // 1. getParameterNames() 에서 모든 파라미터 이름을 꺼내 paramName 에 반복해 담는다
        // 2. key를 paramName , value를 request.getParameter(paramName) 으로 해서 paramMap 에 담는다
        request.getParameterNames().asIterator()
                .forEachRemaining( paramName -> paramMap.put( paramName, request.getParameter(paramName) ) );

        System.out.println("paramMap = " + paramMap); //결과 ex) paramMap = {age=20, username=kim}
        
        return paramMap;
    }
}
// Ctrl + Alt + M : 메서드추출