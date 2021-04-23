package lxy;

import akka.actor.AbstractActor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

@Slf4j
public class TestAsyncSend extends AbstractActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CommandMessage.class, this::handleCommandMessage)
                .build();
    }

    void handleCommandMessage(CommandMessage msg) {
        if (msg.getCommandType() == CommandType.SLEEP) {
            LockSupport.parkNanos((long) 5e9);
        }
    }

}
