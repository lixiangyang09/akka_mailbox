package lxy;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import lombok.NonNull;

import java.util.concurrent.locks.LockSupport;

public class TestMain {
    static public void main(@NonNull String[] args) {
        var system = ActorSystem.create("TestAsyncSend");
        ActorRef worker4 = system.actorOf(Props.create(TestAsyncSend.class).withMailbox("lxy-bounded-mailbox"), "work4");
        for (var i = 0; i< 1000000; i++) {
            worker4.tell(CommandMessage.builder().commandType(CommandType.HELLO).build(), ActorRef.noSender());
        }
        LockSupport.parkNanos((long) 4e9);
        system.terminate();
    }
}
