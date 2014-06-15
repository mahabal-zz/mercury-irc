package com.mercuryirc.event;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;

public final class MercuryEventBus {

    private static final EventBus bus;

    public static void post(final Object object) {
        try {
            bus.post(object);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void register(final Object object) {
        bus.register(object);
    }

    public static void unregister(final Object object) {
        bus.unregister(object);
    }

    static {
        bus = new EventBus(new SubscriberExceptionHandler() {
            public void handleException(Throwable throwable, SubscriberExceptionContext subscriberExceptionContext) {
                throwable.printStackTrace();
            }
        });
    }

}
