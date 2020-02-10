package org.bitbucket.fermenter.stout.messages;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

@Aspect
@Component
public class MessagesAspect {

    private static Logger logger = LoggerFactory.getLogger(MessagesAspect.class);

    @Pointcut("execution(public * *(..))")
    public void publicMethod() {
        // This is just used to try to simplify the annotations we're using to
        // trigger our aspect
    }

    @Around("publicMethod() && execution(* org.bitbucket.fermenter.stout.messages.MessageManagerAwareService.initializeMessageManager(*)) && args(messages)")
    public void publicMethodCallingInitializeMessageManager(ProceedingJoinPoint proceedingJoinPoint,
            Messages messages) {

        // See if we're running in an async thread and have set up the local
        // messages already
        if (!isRequestScopeAvailable() && MessageManager.isCreatedLocally()) {
            // Don't reinitialize the messages if we're in an async thread and using local messages
        } else {
            try {
                proceedingJoinPoint.proceed();
            } catch (Throwable t) {
                logger.error("Could not initialize messages", t);
            }
        }
    }

    @Before("publicMethod() && execution(* org.bitbucket.fermenter.stout.messages.MessageManagerAwareService.addAllMessagesToResponse(*))")
    public void interceptAddMessagesToResponse(JoinPoint joinPoint) {

        if (!isRequestScopeAvailable() && !MessageManager.isCreatedLocally()) {
            MessageManager.initialize(new NotSpringManagedDefaultMessages());
            MessageManager.setIsCreatedLocally();
        }
    }

    private boolean isRequestScopeAvailable() {
        return RequestContextHolder.getRequestAttributes() != null;
    }

}
