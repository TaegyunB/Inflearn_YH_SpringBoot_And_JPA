package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable  // 내장 타입 클래스를 정의할 때 사용
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;
}
