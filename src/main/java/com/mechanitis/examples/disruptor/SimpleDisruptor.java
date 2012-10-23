/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mechanitis.examples.disruptor;

import com.lmax.disruptor.*;
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
