package com.springbyexample.transactionpropagation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Mujuzi Moses
 */
@Service
public class GreetingService {

    private final GreetingRepository greetingRepository;

    private final AuditService auditService;

    public GreetingService(GreetingRepository greetingRepository, AuditService auditService) {
        this.greetingRepository = greetingRepository;
        this.auditService = auditService;
    }

    @Transactional
    public void saveWithRequiredAudit(String greeting) {
        greetingRepository.save(greeting);
        auditService.saveWithRequired("Saved greeting: " + greeting);

        throw new RuntimeException("Simulated failure");
    }

    @Transactional
    public void saveWithRequiresNewAudit(String greeting) {
        greetingRepository.save(greeting);
        auditService.saveWithRequiresNew("Saved greeting: " + greeting);

        throw new RuntimeException("Simulated failure");
    }

    public int countGreetings() {
        return greetingRepository.count();
    }

    public int countAuditEntries() {
        return auditService.count();
    }
}
