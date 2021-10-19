package jpastudy.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {
    private String city, street, zipcode;

//    protected Address(){} // -> 위에 @NoArgsConstructor와 동일한 역할
    // protected를 한 이유는 밑에 함수처럼 생성자를 만들어서 하라고 유도하기 위해서 제약을 걸어둠

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
