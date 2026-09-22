package com.springbyexample.transactionmanagement;

import com.springbyexample.transactionmanagement.impl.GreetingServiceImpl;

/**
 * @author Mujuzi Moses
 */
public class TransactionManagementApplication {

    public static void main(String[] args) {
        RecordingTransactionManager transactionManager = new RecordingTransactionManager();
        TransactionalServiceContext serviceContext = new TransactionalServiceContext(transactionManager);
        serviceContext.setService(GreetingService.class, new GreetingServiceImpl());

        GreetingService service = serviceContext.getService(GreetingService.class);

        System.out.println(service.saveGreeting());

        try {
            service.fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Transactions started: " + transactionManager.getTransactionCount());
        System.out.println("Transactions committed: " + transactionManager.getCommitCount());
        System.out.println("Transactions rolled back: " + transactionManager.getRollbackCount());
    }
}
