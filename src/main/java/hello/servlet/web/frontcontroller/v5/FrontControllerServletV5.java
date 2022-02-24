package hello.servlet.web.frontcontroller.v5;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import hello.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import hello.servlet.web.frontcontroller.v5.adapter.ControllerV3HandlerAdapter;
import hello.servlet.web.frontcontroller.v5.adapter.ControllerV4HandlerAdapter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 컨트롤러(Controller) ==> 핸들러(Handler)
// 이전에는 컨트롤러를 직접 매핑해서 사용. 이제는 어댑터를 사용하기에, 어댑터가 지원하기만 하면, 어떤 것이라도 매핑해서 사용가능.
// 그래서 이름을 컨트롤러에서 더 넒은 범위의 핸들러로 변경.
@WebServlet(name = "frontControllerServletV5", urlPatterns = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet {

    // 매핑 정보의 값이 ControllerV3, ControllerV4 같은 인터페이스에서 아무값이나 받을 수 있는 Object로 변경 됨
    private final Map<String, Object> handlerMappingMap = new HashMap<>();
    private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();

    // 생성자는 핸들러 매핑과 어댑터를 초기화(등록) 한다
    public FrontControllerServletV5() {
        initHandlerMappingMap();
        initHandlerAdapters();
    }

    // 생성과 동시에 실행 (핸들러 매핑)
    private void initHandlerMappingMap() {
        handlerMappingMap.put("/front-controller/v5/v3/members/new-form", new MemberFormControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members/save", new MemberSaveControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members", new MemberListControllerV3());

        handlerMappingMap.put("/front-controller/v5/v4/members/new-form", new MemberFormControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members/save", new MemberSaveControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members", new MemberListControllerV4());
    }

    // 생성과 동시에 실행 (어댑터 초기화)
    private void initHandlerAdapters() {
        handlerAdapters.add(new ControllerV3HandlerAdapter());
        handlerAdapters.add(new ControllerV4HandlerAdapter());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 1. 핸들러 조회 : request URI 를 통해 해당하는 핸들러(컨트롤러)를 찾아온다
        // 결과 ex) new MemberFormControllerV3()
        Object handler = getHandler(request);

        // 예외처리
        if (handler == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 2. 핸들러 어댑터 조회 : 핸들러(컨트롤러)를 통해 해당하는 어댑터를 찾아온다
        // MyHandlerAdapter 는 각각 핸들러어댑터들의 인터페이스
        // 어떤 핸들러어댑터가 와도 사용할 수 있도록 인터페이스를 타입으로 지정 (다형성)
        // 결과 ex) new ControllerV3HandlerAdapter()
        MyHandlerAdapter adapter = getHandleradapter(handler);

        // 3. 핸들러 어댑터 실행 : 찾아온 실제 어댑터를 호출한다
        // 4. 핸들러 실행 : 호출된 어댑터가 핸들러(컨트롤러)를 실행한다
        // 예를들어 handler 로 new MemberSaveControllerV3() 를 보냈을 경우
        // MemberSaveControllerV3 의 Model(응답데이터)과 ViewName(응답경로)을 가진 ModelView 객체 반환
        ModelView mv = adapter.handle(request, response, handler);

        String viewName = mv.getViewName();   // ModelView 에서 논리이름 가져와 저장
        MyView view = viewResolver(viewName); // 논리이름으로 MyView 객체의 인스턴스 생성

        view.render(mv.getModel(), request, response); // MyView 객체의 render()를 실행 => 뷰 렌더링
    }

    // 핸들러 매핑 메서드
    // 핸들러 매핑 정보인 handlerMappingMap 에서 URL에 매핑된 핸들러(컨트롤러) 객체를 찾아서 반환
    private Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return handlerMappingMap.get(requestURI);
    }

    // 핸들러(컨트롤러)를 처리할 수 있는 실제 어댑터 생성 메서드
    private MyHandlerAdapter getHandleradapter(Object handler) {

        // handlerAdapters 리스트에서 반복문을 통해 값을 꺼내온다
        // 리스트에 담긴 값 : ControllerV3HandlerAdapter, ControllerV4HandlerAdapter
        for (MyHandlerAdapter adapter : handlerAdapters) {

            // ControllerV3HandlerAdapter.supports(), ControllerV4HandlerAdapter.supports() 실행
            // supports()를 실행해서 true가 넘어왔을 경우 아래 if문 실행
            if (adapter.supports(handler)) {
                return adapter; // 결과 ex) new ControllerV3HandlerAdapter()
            }
        }
        throw new IllegalArgumentException("handler adapter를 찾을 수 없습니다. handler=" + handler);
    }

    // 논리이름을 받아 물리경로로 바꾼 후 MyView 객체를 생성하여 리턴
    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }
}
