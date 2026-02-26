package org.example.plus.domain.account.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.plus.common.entity.Account;
import org.example.plus.domain.account.repository.AccountRepository;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountCoreService coreService;

    // 비관적 락 (Pessimistic Lock)
    @Transactional
    public void withdraw(Long accountId, int amount) {

        Account account = accountRepository.findById(accountId).orElseThrow();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        account.decrease(amount);
        log.info(Thread.currentThread().getName() + " → 출금 완료 (잔액: " + account.getBalance() + ")");
    }

    @Transactional
    public void withdrawWithLock(Long accountId, int amount) {
        Account account = accountRepository.findByIdForLOCK(accountId); // 🔒 락 획득
        log.info(Thread.currentThread().getName() + " → 락 획득 완료");

        account.decrease(amount);
        log.info(Thread.currentThread().getName() + " → 출금 완료 (잔액: " + account.getBalance() + ")");
    }

    // ⚠️ 트랜잭션은 짧게 유지해야 함



    // 낙관적 락 (Optimistic Lock)
    public void withdrawWithRetry(Long id, int amount) {

        // 출금 요청 -> 낙관적 락 예외 -> 락 획득할 때까지 재요청
        // 10번까지 재시도 -> 이후에는 예외처리
        int retry = 0;
        while (retry < 10) {
            try {
                coreService.withdraw(id, amount);
                return;
            } catch (ObjectOptimisticLockingFailureException e) {
                retry++;
                log.info(Thread.currentThread().getName() + " → 충돌 발생! 락 획득 재시도 횟수: " + retry);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }
            }
        }
        throw new IllegalStateException("10회 재시도 후 실패");
    }
}
