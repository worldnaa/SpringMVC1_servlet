package hello.servlet.basic.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.servlet.basic.HelloData;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * http://localhost:8080/request-body-json
 * JSON 형식 전송
 * content-type: application/json
 * message body: {"username": "hello", "age": 20}
 */
@WebServlet(name = "requestBodyJsonServlet", urlPatterns = "/request-body-json")
public class RequestBodyJsonServlet extends HttpServlet {

    // JSON 결과를 파싱해서 사용할 수 있는 자바 객체로 변환 하려면
    // Jackson, Gson 같은 JSON 변환 라이브러리를 추가해서 사용해야한다.
    // 스프링부트로 Spring MVC 를 선택하면 기본으로 Jackson 라이브러리(ObjectMapper)를 함께 제공한다.
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1. 메시지 바디의 내용을 바이트 코드로 받을 수 있다
        ServletInputStream inputStream = request.getInputStream();

        //2. 바이트 코드를 스프링으로 바꾸기 (스프링이 제공하는 유틸리티 클래스 사용)
        //StandardCharsets.UTF_8 : 문자를 바이트로, 바이트를 문자로 바꿀 때는 인코딩 정보를 기입해야 함!
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        System.out.println("messageBody = " + messageBody);

        //3. readValue 를 통해 값을 읽을 수 있다 (바디에 담긴 메시지를 읽어와, HelloData.class 타입으로 변환)
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        System.out.println("helloData.username = " + helloData.getUsername());
        System.out.println("helloData.age = " + helloData.getAge());

        response.getWriter().write("ok");
    }
}
