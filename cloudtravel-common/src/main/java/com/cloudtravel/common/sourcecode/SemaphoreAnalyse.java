package com.cloudtravel.common.sourcecode;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * Semaphore是一个线程同步的辅助类,可以维护控制访问当前资源的线程并发个数,并提供了同步机制 ,
 * Semaphore的用途显然不会与Lock一致，不然就重复造轮子了。Semaphore最重要的一个功能便是：可以允许多个线程访问一个临界区。
 * 使用semaphore可以控制同时访问资源的个数.较多的如可以控制接口的并发个数
 * 对比Mutex :
 *      Mutex一般用来串行化Critical section代码的访问,即最多同时允许一个线程访问 . 定义比较明显
 *      Semaphore: 控制资源允许的并发量 , 当其允许的并发量设置为1时,又称之为Binary Semaphore
 *  Binary Semaphore同Mutex对比:
 *    作用:两者在所起到的作用上并没有什么区别 , 此时都是为了保证资源可以串行化访问,即都是保证同时只有一个线程访问资源 .
 *    原理 : 两者在实现原理上有区别 .
 *      Semaphore: 为非对称模型,可以理解为,锁虽然只有一把,同时也只能有一把钥匙开着,但是不同的线程拥有的钥匙不同.
 *                 semaphore可以理解为一个原子变量 , 信号量通过内部计数器,控制线程的并发量.且可以通过变更信号量[permit]进行灵活的调整
 *      Mutex: 为对称模型,即一把锁只有一把钥匙,线程之间通过这一把钥匙控制资源的访问 .也就是通过synchronized安全锁进行控制的.
 * @author Administrator
 */
public class SemaphoreAnalyse implements java.io.Serializable {
    private static final long serialVersionUID = -3222578661600680210L;
    /**
     * 通过AbstractQueuedSynchronizer子类实现的所有机制
     */
    private final Sync sync;

    /**
     * Synchronization implementation for SemaphoreAnalyse.  Uses AQS state
     * to represent permits. Subclassed into fair and nonfair
     * versions.
     */

    /**
     * 为semaphore定义的锁抽象类,继承自AQS,采用CAS算法,
     * 借用AQS中的state控制是否允许访问 .
     * sync为抽象类 . 主要定义了获取锁和释放锁的非公平锁方法 .
     */
    abstract static class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = 1192457210091910933L;

        /** 调用AQS中的方法设置当前允许访问的并发数.permits : 创建semaphore实例的时候传进来 */
        Sync(int permits) {
            setState(permits);
        }

        /** 获取当前可使用的资源并发数 */
        final int getPermits() {
            return getState();
        }

        /** 获取一定数量的资源数.-非公平锁版本,返回当前剩余可使用的信号量 .  */
        final int nonfairTryAcquireShared(int acquires) {
            for (; ; ) {
                int available = getState();
                int remaining = available - acquires;
                if (remaining < 0 || compareAndSetState(available, remaining)) {
                    return remaining;
                }
            }
        }

        /** 释放一定量的资源 */
        @Override
        protected final boolean tryReleaseShared(int releases) {
            for (; ; ) {
                int current = getState();
                int next = current + releases;
                if (next < current)
                {
                    throw new Error("Maximum permit count exceeded");
                }
                if (compareAndSetState(current, next))
                {
                    return true;
                }
            }
        }

        /** 减小信号量,同nonfairTryAcquireShared操作类似.只是只能减小,不能传入负值 */
        final void reducePermits(int reductions) {
            for (; ; ) {
                int current = getState();
                int next = current - reductions;
                if (next > current) {
                    throw new Error("Permit count underflow");
                }
                if (compareAndSetState(current, next)) {
                    return;
                }
            }
        }

        /** 销毁信号量-将当前可使用信号量改为0.即不允许访问 */
        final int drainPermits() {
            for (; ; ) {
                int current = getState();
                if (current == 0 || compareAndSetState(current, 0))
                    return current;
            }
        }
    }

    /** 非公平锁版本 */
    static final class NonfairSync extends Sync {
        private static final long serialVersionUID = -2694183684443567898L;

        /** 构造器 */
        NonfairSync(int permits) {
            super(permits);
        }

        /** 获取信号量资源 */
        @Override
        protected int tryAcquireShared(int acquires) {
            return nonfairTryAcquireShared(acquires);
        }
    }

    /**
     * 公平锁版本
     */
    static final class FairSync extends Sync {
        private static final long serialVersionUID = 2014338818796000944L;

        FairSync(int permits) {
            super(permits);
        }

        /** 获取资源信号量 当前state -1
         * 对比NonFairSync多了hasQueuedPredecessors()的判断
         * hasQueuedPredecessors()判断当前线程是否需要排队.并将当前线程加入到队列中
         * 队列遵循FIFO[先进先出]的原则,若当前队列中无等待线程,则返回false表示不需要排队,
         * 若存在等待线程,说明当前可用信号量已耗尽 , 需要排队,返回true.
         * */
        protected int tryAcquireShared(int acquires) {
            for (; ; ) {
                if (hasQueuedPredecessors())
                    return -1;
                int available = getState();
                int remaining = available - acquires;
                if (remaining < 0 ||
                        compareAndSetState(available, remaining))
                    return remaining;
            }
        }
    }

    /**
     * 构造器-默认非公平锁版本
     * @param permits
     */
    public SemaphoreAnalyse(int permits) {
        sync = new NonfairSync(permits);
    }

    /** 构造器,可自定义选择公平锁还是非公平锁 */
    public SemaphoreAnalyse(int permits, boolean fair) {
        sync = fair ? new FairSync(permits) : new NonfairSync(permits);
    }

    /**
     * 获取一个信号量, 当前state -1
     * 其核心实现为AQS的doAcquireSharedInterruptibly()方法
     * 若当前信号量资源耗尽,则会中断响应
     * */
    public void acquire() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    /**
     * 获取信号量,当前state -1
     * 若当前信号量资源耗尽,则响应等待直到获取到资源 . 采用自旋锁
     */
    public void acquireUninterruptibly() {
        sync.acquireShared(1);
    }

    /**
     * 获取一个信号量 . 当前state -1
     * 若当前还有资源可用 . 返回true . 若无,返回false
     * @return
     */
    public boolean tryAcquire() {
        return sync.nonfairTryAcquireShared(1) >= 0;
    }

    /**
     * 获取一个信号量 . 当前state -1
     * 若当前还有资源可用 . 返回true . 若无,返回false . 可自定义超时时间,超时中断响应
     * @param timeout
     * @param unit
     * @return
     * @throws InterruptedException
     */
    public boolean tryAcquire(long timeout, TimeUnit unit)
            throws InterruptedException {
        return sync.tryAcquireSharedNanos(1, unit.toNanos(timeout));
    }

    /** 释放一个资源 . 当前state + 1 */
    public void release() {
        sync.releaseShared(1);
    }

    /** 获取一定量的信号量资源 . 若当前信号量耗尽或者无法满足,则中断响应 */
    public void acquire(int permits) throws InterruptedException {
        if (permits < 0) throw new IllegalArgumentException();
        sync.acquireSharedInterruptibly(permits);
    }

    /**
     * 从该信号量分析获取给定数量的资源，直到所有许可证都可用为止。
     * 获取给定数量的许可证（如果可用），并立即返回，将可用许可证数量减少给定数量。
     * 如果没有足够的信号量可用，则当前线程出于线程调度目的将被禁用，并处于休眠状态，
     * 直到其他线程调用此信号量分析的某个释放方法，当前线程下一个将被分配许可证，并且可用许可证的数量满足此请求。
     * 如果当前线程在等待许可证时被中断，那么它将继续等待，并且它在队列中的位置不受影响。当线程确实从此方法返回时，将设置其中断状态。
     * @param permits
     */
    public void acquireUninterruptibly(int permits) {
        if (permits < 0) throw new IllegalArgumentException();
        sync.acquireShared(permits);
    }

    /**
     * 仅当调用时所有许可证都可用时，从该信号量分析获取给定数量的许可证。
     * 获取给定数量的许可证（如果可用），并立即返回，返回值为true，将可用许可证数量减少给定数量。
     * 如果没有足够的许可证可用，则此方法将立即返回值false，并且可用许可证的数量不变。
     * 即使此信号量分析已设置为使用公平排序策略，对tryAcquire的调用也将立即获得许可证（如果有），
     * 无论其他线程当前是否正在等待。这种“讨价还价”行为在某些情况下是有用的，即使它破坏了公平性。
     * 如果您想遵守公平性设置，那么使用几乎相等的tryAcquire（允许，0，TimeUnit.SECONDS）（它还检测中断）。
     * @param permits
     * @return
     */
    public boolean tryAcquire(int permits) {
        if (permits < 0) throw new IllegalArgumentException();
        return sync.nonfairTryAcquireShared(permits) >= 0;
    }

    /**
     * 以共享定时模式获取信号量,超时中断响应
     * @param permits
     * @param timeout
     * @param unit
     * @return
     * @throws InterruptedException
     */
    public boolean tryAcquire(int permits, long timeout, TimeUnit unit)
            throws InterruptedException {
        if (permits < 0) throw new IllegalArgumentException();
        return sync.tryAcquireSharedNanos(permits, unit.toNanos(timeout));
    }

    /**
     * 释放给定数量的许可证
     * @param permits
     */
    public void release(int permits) {
        if (permits < 0) throw new IllegalArgumentException();
        sync.releaseShared(permits);
    }

    /**
     * 返回当前可用的信号量
     * @return
     */
    public int availablePermits() {
        return sync.getPermits();
    }

    /**
     * 将当前可用信号量全部占用
     * @return
     */
    public int drainPermits() {
        return sync.drainPermits();
    }

    /**
     * 按指定的减少量缩小可用许可证的数量。此方法在使用信号量分析跟踪不可用资源的子类中非常有用。
     * 此方法与acquire的不同之处在于，它不会发生线程阻塞
     */
    protected void reducePermits(int reduction) {
        if (reduction < 0) throw new IllegalArgumentException();
        sync.reducePermits(reduction);
    }

    /** 当前是否为公平锁 */
    public boolean isFair() {
        return sync instanceof FairSync;
    }

    /**
     * 当前是否有线程在队列中等待
     */
    public final boolean hasQueuedThreads() {
        return sync.hasQueuedThreads();
    }

    /**
     * 获取等待线程的队列的长度.即返回有多少个等待线程
     */
    public final int getQueueLength() {
        return sync.getQueueLength();
    }

    /**
     * 获取队列中的所有等待线程 AQS内自带的方法
     * @return the collection of threads
     */
    protected Collection<Thread> getQueuedThreads() {
        return sync.getQueuedThreads();
    }

    /**
     * toString
     * @return a string identifying this SemaphoreAnalyse, as well as its state
     */
    public String toString() {
        return super.toString() + "[Permits = " + sync.getPermits() + "]";
    }
}
