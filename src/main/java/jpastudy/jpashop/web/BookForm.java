package jpastudy.jpashop.web;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter @Setter
public class BookForm {
    private Long id;

    @NotBlank(message = "상품 이름은 필수입니다.")
    private String name;

    private String author, isbn;
    private int price;

    @Min(value = 1, message = "최소 수량은 1개 입니다.")
    private int stockQuantity;
}
