package hello.servlet.web.frontcontroller.v3;

import hello.servlet.web.frontcontroller.ModelView;

import java.util.Map;

public interface ControllerV3 {
    // ControllerV1 => void process(HttpServletRequest request, HttpServletResponse response)
    // ControllerV2 => MyView process(HttpServletRequest request, HttpServletResponse response)
    // ControllerV3 => ModelView process(Map<String, String> paramMap)
    // ControllerV3 는 사용하지 않는 Request,Response 를 없애고, Map 으로 변경
    ModelView process(Map<String, String> paramMap);
}
