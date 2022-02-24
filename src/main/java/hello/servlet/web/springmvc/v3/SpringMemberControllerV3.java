package hello.servlet.web.springmvc.v3;

import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * Model 도입 (Model 을 파라미터로 받아서 사용)
 * ViewName 직접 반환 (뷰의 논리이름 반환)
 * @RequestParam 사용
 * @RequestMapping -> @GetMapping, @PostMapping
 */
@Controller
@RequestMapping("/springmvc/v3/members")
public class SpringMemberControllerV3 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

//  @RequestMapping(value = "/new-form", method = RequestMethod.GET)
    @GetMapping("/new-form")
    public String newForm() {
        return "new-form";
    }

//  @RequestMapping(value = "/save", method = RequestMethod.POST)
    @PostMapping("/save")
    public String save( @RequestParam("username") String username,
                        @RequestParam("age") int age,
                        Model model) {

        // request.getParameter 대신 @RequestParam 사용
        // String username = request.getParameter("username");
        // int age = Integer.parseInt(request.getParameter("age"));

        Member member = new Member(username, age);
        memberRepository.save(member);

        model.addAttribute("member", member);
        return "save-result";
    }

//  @RequestMapping(method = RequestMethod.GET)
    @GetMapping
    public String members(Model model) {

        List<Member> members = memberRepository.findAll();

        model.addAttribute("members", members);
        return "members";
    }
}
