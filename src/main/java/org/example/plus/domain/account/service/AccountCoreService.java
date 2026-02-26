package org.example.plus.domain.account.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.plus.common.entity.Account;
import org.example.plus.domain.account.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountCoreService {

    private final AccountRepository accountRepository;

    @Transactional
    public void withdraw(Long id, int amount) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("계좌 없음"));

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        account.decrease(amount);
    }
}