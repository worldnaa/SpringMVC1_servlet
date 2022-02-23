package hello.servlet.web.frontcontroller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class MyView {
    private String viewPath;

    // MyView 인스턴스가 생성될 때 viewPath(물리주소)는 바로 셋팅된다
    public MyView(String viewPath) {
        this.viewPath = viewPath;
    }

    public void render(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }

    public void render(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // jsp에서 사용할 수 있도록 request.setAttribute()를 하는 메서드
        modelToRequestAttribute(model, request);

        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }

    // model(요청에 대한 응답 데이터가 담긴)을 request 객체에 담아 jsp에서 사용할 수 있도록!
    private void modelToRequestAttribute(Map<String, Object> model, HttpServletRequest request) {

        model.forEach( (key, value) -> request.setAttribute(key, value) );

        System.out.println("model = " + model);
        // 결과 ex) model = {member=Member{id=1, username='kim', age=20}}
    }

    @Override
    public String toString() {
        return "MyView{" +
                "viewPath='" + viewPath + '\'' +
                '}';
    }
}
