package lxy;

/**
 * 比较Redisson的localMap和java的map的get效率
 *
 */

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.redisson.Redisson;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 0)
@Threads(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(1)
@State(Scope.Benchmark)
public class AkkaSendAsync {
    ActorSystem system;
    ActorRef work1;
    ActorRef work2;
    ActorRef work3;
    ActorRef work4;
    ActorRef work5;
    ActorRef work6;
    ActorRef work7;
    ActorRef work8;

    CommandMessage sleep;
    CommandMessage hello;

    @Setup
    public void prepare() {
        system = ActorSystem.create();
        work1 = system.actorOf(Props.create(TestAsyncSend.class), "work1");
        work2 = system.actorOf(Props.create(TestAsyncSend.class), "work2");
        work3 = system.actorOf(Props.create(TestAsyncSend.class), "work3");
        work4 = system.actorOf(Props.create(TestAsyncSend.class).withMailbox("lxy-bounded-mailbox"), "work4");
        work5 = system.actorOf(Props.create(TestAsyncSend.class).withMailbox("lxy-bounded-mailbox"), "work5");
        work6 = system.actorOf(Props.create(TestAsyncSend.class).withMailbox("lxy-bounded-mailbox"), "work6");

        sleep = CommandMessage.builder().commandType(CommandType.SLEEP).build();
        hello = CommandMessage.builder().commandType(CommandType.HELLO).build();

        work7 = system.actorOf(Props.create(TestAsyncSend.class), "work7");
        work8 = system.actorOf(Props.create(TestAsyncSend.class).withMailbox("lxy-bounded-mailbox"), "work8");


    }

    @TearDown
    public void shutdown() {
        system.terminate();
    }


    @Benchmark
    @Measurement(iterations = 1, batchSize = 100)
    public void testDefaultMailBox100() {
        work1.tell(hello, ActorRef.noSender());
    }

    @Benchmark
    @Measurement(iterations = 1, batchSize = 10000)
    public void testDefaultMailBox10000() {
        work2.tell(hello, ActorRef.noSender());
    }

    @Benchmark
    @Measurement(iterations = 1, batchSize = 50000)
    public void testDefaultMailBox50000() {
        work7.tell(hello, ActorRef.noSender());
    }

//    @Benchmark
//    @Measurement(iterations = 1, batchSize = 10000)
//    public void testDefaultMailBoxSleep() {
//        work3.tell(sleep, ActorRef.noSender());
//    }

//    @Benchmark
//    @Measurement(iterations = 1, batchSize = 100)
//    public void testFixMailBoxSmall() {
//        work4.tell(hello, ActorRef.noSender());
//    }
//
//    @Benchmark
//    @Measurement(iterations = 1, batchSize = 10000)
//    public void testFixMailBoxLarge() {
//        work5.tell(hello, ActorRef.noSender());
//    }
//
//    @Benchmark
//    @Measurement(iterations = 1, batchSize = 50000)
//    public void testFixMailBoxLargeMore() {
//        work8.tell(hello, ActorRef.noSender());
//    }

//    @Benchmark
//    @Measurement(iterations = 1, batchSize = 10000)
//    public void testFixMailBoxSleep() {
//        work6.tell(sleep, ActorRef.noSender());
//    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(AkkaSendAsync.class.getSimpleName())
                .build();
        new Runner(options).run();
    }
}