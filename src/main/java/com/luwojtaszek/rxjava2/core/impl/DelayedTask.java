package com.luwojtaszek.rxjava2.core.impl;

import com.luwojtaszek.rxjava2.core.common.ITask;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

/**
 * Task that waits a given number of seconds and then finishes its work.
 * <p>
 * Created by lukasz on 04.08.2017.
 * With IntelliJ IDEA 15
 */
@Getter
@RequiredArgsConstructor
public class DelayedTask implements ITask {
    /**
     * Defines how many seconds the task will wait before it will finish.
     * Zero means that the task will finish immediately.
     */
    private final int delayInSeconds;
    /**
     * Stores information if task was started.
     */
    private boolean started;
    /**
     * Stores information if task was successfully finished.
     */
    private boolean finishedSuccessfully;
    /**
     * Stores information if the task was interrupted.
     * It can happen if the thread that is running this task was killed.
     */
    private boolean interrupted;
    /**
     * Stores the thread identifier in which the task was executed.
     */
    private long threadId;

    @Override
    public void execute() {
        try {
            this.threadId = Thread.currentThread().getId();
            this.started = true;
            TimeUnit.SECONDS.sleep(delayInSeconds);
            this.finishedSuccessfully = true;
        } catch (InterruptedException e) {
            this.interrupted = true;
        }
    }

    /**
     * Creates a task instance which finish its work immediately.
     *
     * @return task
     */
    public static DelayedTask notDelayedTask() {
        return new DelayedTask(0);
    }

    /**
     * Creates a task instance which finish its work after 5 seconds.
     *
     * @return task
     */
    public static DelayedTask fiveSecondsDelayedTask() {
        return new DelayedTask(5);
    }
}
