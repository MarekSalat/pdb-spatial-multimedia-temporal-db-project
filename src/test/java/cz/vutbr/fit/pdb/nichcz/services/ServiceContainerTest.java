package cz.vutbr.fit.pdb.nichcz.services;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * User: Marek Salát
 * Date: 7.10.13
 * Time: 16:49
 */
public class ServiceContainerTest {

    @Test
    public void showcaseTest() throws IOException {
        // Container hold all registered services
        ServiceContainer sc = new ServiceContainer();

        Assert.assertFalse("service should not be registered", sc.hasServices("mock-service"));
        Assert.assertFalse(   "service should not be created", sc.isCreated("mock-service"));

        // Service are added over factory because they will be created on first request.
        // Every service is singleton, created on demand.
        sc.addService("mock-service", new ServiceFactory() {
            @Override
            public Service create() {
                MockService mockService = new MockService();
                // ... service initialization
                return mockService;
            }
        });

        Assert.assertTrue(  "service should be registered", sc.hasServices("mock-service"));
        Assert.assertFalse("service should not be created", sc.isCreated("mock-service"));

        // this is first request to container for my service
        // service is created and returned
        MockService mockService = (MockService) sc.get("mock-service");

        Assert.assertTrue( "service should be registered", sc.hasServices("mock-service"));
        Assert.assertTrue("service should not be created", sc.isCreated("mock-service"));
        Assert.assertEquals("service should be created only once, remember it should be singleton", 1, mockService.created);

        // call service method
        Assert.assertEquals("What is the answer to life the universe and everything?", 42, mockService.doSomething());

        // another request to container for my service
        mockService = (MockService) sc.get("mock-service");
        mockService = (MockService) sc.get("mock-service");
        mockService = (MockService) sc.get("mock-service");

        Assert.assertEquals("service should be created only only once", 1, mockService.created);

        Assert.assertFalse("service should not be closed", mockService.closed);
        // try to close container, it should close all services
        sc.close();
        Assert.assertTrue("service should be closed", mockService.closed);
    }
}
