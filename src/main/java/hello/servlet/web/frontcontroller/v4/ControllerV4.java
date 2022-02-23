package hello.servlet.web.frontcontroller.v4;

import java.util.Map;

public interface ControllerV4 {

    /**
     * @param paramMap
     * @param model
     * @return viewName
     */

    // ControllerV1 => void process(HttpServletRequest request, HttpServletResponse response)
    // ControllerV2 => MyView process(HttpServletRequest request, HttpServletResponse response)
    // ControllerV3 => ModelView process(Map<String, String> paramMap)
    // ControllerV4 => String process(Map<String, String> paramMap, Map<String, Object> model)
    // ControllerV4 는 결과로 논리 이름을 반환한다 (ex. return "save-result";)
    String process(Map<String, String> paramMap, Map<String, Object> model);
}
