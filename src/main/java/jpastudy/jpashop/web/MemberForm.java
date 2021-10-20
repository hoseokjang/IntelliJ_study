package jpastudy.jpashop.web;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class MemberForm {
    @NotBlank(message = "회원 이름은 필수입니다.")
    private String name;

    private String city, street, zipcode;
}
