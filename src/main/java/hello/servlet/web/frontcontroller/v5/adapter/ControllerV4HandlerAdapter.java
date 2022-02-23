package hello.servlet.web.frontcontroller.v5.adapter;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.v4.ControllerV4;
import hello.servlet.web.frontcontroller.v5.MyHandlerAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// 인터페이스 MyHandlerAdapter 를 구현한 실제 어댑터 (ControllerV4를 지원하는 어댑터 구현)
public class ControllerV4HandlerAdapter implements MyHandlerAdapter {
    
    @Override
    public boolean supports(Object handler) {
        // handler(=컨트롤러) 가 ControllerV4 의 인스턴스면 true 리턴
        // ControllerV4를 처리할 수 있는 어댑터인지 확인
        return (handler instanceof ControllerV4);
    }

    @Override
    public ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {

        // handler 를 ControllerV4 로 변환한 다음에 V4 형식에 맞도록 호출한다.
        // supports()를 통해 ControllerV4만 지원하기 때문에 타입변환은 걱정없이 실행해도된다.
        ControllerV4 controller = (ControllerV4) handler;

        // ControllerV4는 인자로 paramMap 과 model 을 받기에, 요청 정보를 Map으로 변환하는 메서드 실행
        Map<String, String> paramMap = createParamMap(request);

        // ControllerV4는 인자로 paramMap 과 model 을 받기에, 인자로 보낼 model 객체 생성
        HashMap<String, Object> model = new HashMap<>();

        // ControllerV4는 논리이름을 String 형태로 반환
        String viewName = controller.process(paramMap, model);

        // handle() 은 ModelView 를 반환하므로, ModelView 를 생성하고, 응답 정보를 set 한다
        ModelView mv = new ModelView(viewName);
        mv.setModel(model);

        return mv;
    }

    // 요청 정보(파라미터 정보)를 Map으로 변환
    private Map<String, String> createParamMap(HttpServletRequest request) {

        Map<String, String> paramMap = new HashMap<>();

        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));

        return paramMap;
    }
}
