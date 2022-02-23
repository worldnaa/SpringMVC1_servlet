package hello.servlet.web.frontcontroller.v5.adapter;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.v3.ControllerV3;
import hello.servlet.web.frontcontroller.v5.MyHandlerAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// 인터페이스 MyHandlerAdapter 를 구현한 실제 어댑터 (ControllerV3를 지원하는 어댑터 구현)
public class ControllerV3HandlerAdapter implements MyHandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        // handler(=컨트롤러) 가 ControllerV3 의 인스턴스면 true 리턴
        // ControllerV3을 처리할 수 있는 어댑터인지 확인
        return (handler instanceof ControllerV3);
    }

    @Override
    public ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {

        // handler 를 ControllerV3 으로 변환한 다음에 V3 형식에 맞도록 호출한다.
        // supports()를 통해 ControllerV3만 지원하기 때문에 타입변환은 걱정없이 실행해도된다.
        ControllerV3 controller = (ControllerV3) handler;

        // ControllerV3은 인자로 paramMap 을 받기에, 요청 정보를 Map으로 변환하는 메서드 실행
        Map<String, String> paramMap = createParamMap(request);

        // ControllerV3은 ModelView를 반환하므로 그대로 ModelView를 반환하면 된다.
        ModelView mv = controller.process(paramMap);

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
