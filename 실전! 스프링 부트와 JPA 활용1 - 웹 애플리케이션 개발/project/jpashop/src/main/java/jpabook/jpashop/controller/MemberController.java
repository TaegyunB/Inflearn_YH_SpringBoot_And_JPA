package jpabook.jpashop.controller;

import jakarta.validation.Valid;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {
        /*
        @Valid
            - 자바 빈 검증의 핵심 기능
            - 요청 데이터를 컨트롤러 메서드에 바인딩할 때, 그 객체에 설정한 검증 규칙을 자동으로 검사해주는 기능

        BindingResult
            - BindingResult는 @Valid 바로 뒤에 선언해야 함
            - BindingResult 없으면 검증 실패 시 예외가 발생해서 400 에러로 끝남
            - BindingResult 있으면 예외 대신 에러 객체를 담아 처리 가능
         */

        // 에러 발생시 다시 form으로 돌아가면서 BindingResult 표시
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);

        return "redirect:/";
    }
}
