package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")  // 테이블 명이 member_id로 되어있기 때문
    private Long id;

    private String name;

    @Embedded  // 엔티티에서 내장 타입을 사용할 때 붙임
    private Address address;

    @OneToMany(mappedBy = "member")  // 연관관계 거울: mappedBy
    private List<Order> orders = new ArrayList<>();
}
