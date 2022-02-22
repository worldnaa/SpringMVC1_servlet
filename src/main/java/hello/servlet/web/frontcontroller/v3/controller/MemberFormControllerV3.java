package hello.servlet.web.frontcontroller.v3.controller;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.v3.ControllerV3;

import java.util.Map;

public class MemberFormControllerV3 implements ControllerV3 {
    // 변경사항1. 리턴타입이 MyView 가 아닌, ModelView 이다
    // 변경사항2. 매개변수로 HttpServletRequest, Response 를 받지 않고, Map<String, String> 을 받는다
    // 변경사항3. public MyView(String viewPath)   => jsp로 이동할 경로를 물리이름으로 적는다 ("/WEB-INF/views/new-form.jsp")
    //          public ModelView(String viewName) => jsp로 이동할 경로를 논리이름으로 적는다 ("new-form")
    @Override
    public ModelView process(Map<String, String> paramMap) {
        return new ModelView("new-form");
    }
    
    /* MemberFormControllerV2 의 코드 
    public MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return new MyView("/WEB-INF/views/new-form.jsp");
    }*/
}
