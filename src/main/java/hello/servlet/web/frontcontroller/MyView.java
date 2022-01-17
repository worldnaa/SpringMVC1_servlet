package hello.servlet.web.frontcontroller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class MyView {
    private String viewPath;

    public MyView(String viewPath) {
        this.viewPath = viewPath; //MyView 인스턴스가 생성될 때 viewPath(물리주소)는 바로 셋팅된다
    }

    public void render(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }

    public void render(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        modelToRequestAttribute(model, request);
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }

    private void modelToRequestAttribute(Map<String, Object> model, HttpServletRequest request) {
        // render가 실행되면, model에 request.setAttribute 해서 다 넣는다
        model.forEach((key, value) -> request.setAttribute(key, value));
        System.out.println("model = " + model); //ex) model = {member=Member{id=1, username='kim', age=20}}
    }

    @Override
    public String toString() {
        return "MyView{" +
                "viewPath='" + viewPath + '\'' +
                '}';
    }
}
