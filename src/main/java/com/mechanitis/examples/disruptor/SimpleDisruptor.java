package com.mechanitis.examples.disruptor;

import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventPublisher;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.concurrent.Executors;

public class SimpleDisruptor {
    private static final int RING_BUFFER_SIZE = 1024;
    private final SimpleEventTranslator translator = new SimpleEventTranslator();
    private final SimpleEventHandler simpleEventHandler = new SimpleEventHandler();


    private void wiringWithRingBuffer() {
        final RingBuffer<SimpleEvent> ringBuffer = new RingBuffer<>(SimpleEvent.EVENT_FACTORY, RING_BUFFER_SIZE);
        final SequenceBarrier ringBufferSequenceBarrier = ringBuffer.newBarrier();

        final EventPublisher<SimpleEvent> publisher = new EventPublisher<>(ringBuffer);

        BatchEventProcessor<SimpleEvent> batchEventProcessor = new BatchEventProcessor<>(ringBuffer, ringBufferSequenceBarrier, simpleEventHandler);
        ringBuffer.setGatingSequences(batchEventProcessor.getSequence());


        //while there's still things to push into the disruptor...
        //if/for/while
        publisher.publishEvent(translator);
        //end if/for/while

    }

    private void wiringUsingWizard() {
        final Disruptor<SimpleEvent> disruptor = new Disruptor<>(SimpleEvent.EVENT_FACTORY, RING_BUFFER_SIZE, Executors.newSingleThreadExecutor());
        disruptor.handleEventsWith(simpleEventHandler);

        //while there's still things to push into the disruptor...
        //if/for/while
        disruptor.publishEvent(translator);
        //end if/for/while
    }


    public class SimpleEventTranslator implements EventTranslator<SimpleEvent> {
        @Override
        public void translateTo(final SimpleEvent event, final long sequenceNo) {
            // put something into the Event
        }
    }

    public class SimpleEventHandler implements EventHandler<SimpleEvent> {

        @Override
        public void onEvent(final SimpleEvent event,
                            final long sequence,
                            final boolean endOfBatch) throws Exception {
            // do stuff
        }
    }

}
