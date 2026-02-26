package org.example.plus.common.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int balance;

    @Version
    private Long version; // 버전 필드 : 낙관적락 사용하기 위한 필드

    public Account(int balance) {
        this.balance = balance;
    }

    public void decrease(int amount) {
        if (balance - amount < 0) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }
        this.balance -= amount;
    }
}