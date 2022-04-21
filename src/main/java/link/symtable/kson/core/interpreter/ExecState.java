package link.symtable.kson.core.interpreter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import link.symtable.kson.core.interpreter.continuation.WaitMultiTaskContInstance;
import lombok.Data;

@Data
public class ExecState {
    private final Lock lock = new ReentrantLock();
    final Condition notEmpty = lock.newCondition(); // take thread
    private BlockingQueue<ContRunState> callbackQueue = new LinkedBlockingQueue<>(10);
    private ContRunState storedContRunState;
    private Set<WaitMultiTaskContInstance> waitingResumeTasks = new HashSet<>();
    private Map<Long, TimerTask> timerTasks = new HashMap<>();
    private Timer timer = new Timer();
    private AtomicLong timerTaskIdGen = new AtomicLong(1);

    public boolean hasStoredContRun() {
        return storedContRunState != null;
    }
    public void setStoredContRun(ContRunState contRunState) {
        this.storedContRunState = contRunState;
    }
    public ContRunState restoreContRunState() {
        ContRunState r = this.storedContRunState;
        this.storedContRunState = null;
        return r;
    }
}
