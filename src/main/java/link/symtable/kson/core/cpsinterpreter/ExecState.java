package link.symtable.kson.core.cpsinterpreter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import link.symtable.kson.core.cpsinterpreter.continuation.WaitContinuation;
import link.symtable.kson.core.node.KsNode;
import lombok.Data;

@Data
public class ExecState {
    public KsNode lastValue;

    private BlockingQueue<ContRunState> callbackQueue = new LinkedBlockingQueue<>(10);
    private ContRunState storedContRunState;
    private Set<WaitContinuation> waitingResumeTasks = new HashSet<>();
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
