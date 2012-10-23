package com.mechanitis.examples.disruptor;

import com.lmax.disruptor.EventFactory;

public class SimpleEvent {
    public static final EventFactory<SimpleEvent> EVENT_FACTORY = new SimpleEventFactory();

    private String value;


    private static class SimpleEventFactory implements EventFactory<SimpleEvent> {
        @Override
        public SimpleEvent newInstance() {
            return new SimpleEvent();
        }
    }
}

