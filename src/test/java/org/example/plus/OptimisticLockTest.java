package org.example.plus;

import org.example.plus.common.entity.Account;
import org.example.plus.domain.account.repository.AccountRepository;
import org.example.plus.domain.account.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class OptimisticLockTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    @Test
    void 낙관적락_정상동작_테스트() throws InterruptedException {

        // Account 생성
        Account account = accountRepository.save(new Account(100));

        // 4명이 동시에 요청 보냄
        ExecutorService executor = Executors.newFixedThreadPool(4);
        Runnable task = () -> accountService.withdrawWithRetry(account.getId(), 10);

        executor.submit(task);
        executor.submit(task);
        executor.submit(task);
        executor.submit(task);

        executor.shutdown();

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Account result = accountRepository.findById(account.getId()).orElseThrow();
        System.out.println("최종 잔액: " + result.getBalance());
    }
}