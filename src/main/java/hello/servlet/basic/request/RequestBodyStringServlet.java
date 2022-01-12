package hello.servlet.basic.request;

import org.springframework.util.StreamUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "requestBodyStringServlet", urlPatterns = "/request-body-string")
public class RequestBodyStringServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1. 메시지 바디의 내용을 바이트 코드로 받을 수 있다
        ServletInputStream inputStream = request.getInputStream();
        //2. 바이트 코드를 스프링으로 바꾸기 (스프링이 제공하는 유틸리티 클래스 사용)
        //StandardCharsets.UTF_8 : 문자를 바이트로, 바이트를 문자로 바꿀 때는 인코딩 정보를 기입해야 함!
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        System.out.println("messageBody = " + messageBody);

        response.getWriter().write("ok");
    }
}
