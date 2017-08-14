package com.luwojtaszek.rxjava2.core.impl;

import com.google.common.collect.Sets;
import com.luwojtaszek.rxjava2.core.common.ITask;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by lukasz on 10.08.2017.
 * With IntelliJ IDEA 15
 */
@Slf4j
@RunWith(SpringRunner.class)
public class ConcurrentTasksExecutorTests {
    private final ITask exceptionTask = () -> {
        throw new IllegalStateException("Wrong state exception");
    };

    @Test
    public void shouldKillAllThreadsAndStopWorkOnException() {
        final int numberOfConcurrentThreads = 2;
        DelayedTask firstTask = DelayedTask.fiveSecondsDelayedTask(), secondTask = DelayedTask.fiveSecondsDelayedTask();

        try {
            new ConcurrentTasksExecutor(numberOfConcurrentThreads, firstTask, exceptionTask, secondTask).execute();
            fail("Execute method should throw IllegalStateException.");
        } catch (IllegalStateException ignored) {
        }

        // checking if first task was interrupted by exception fired in other thread
        assertTrue("First task should started.", firstTask.isStarted());
        assertTrue("First task should be interrupted because of thread death.", firstTask.isInterrupted());
        assertFalse("First task should not be successfully finished.", firstTask.isFinishedSuccessfully());

        // checking if second task was not even started because of exception occurrence
        assertFalse("Second task should not started.", secondTask.isStarted());
    }

    @Test
    public void shouldThrowExceptionWhenNumberOfConcurrentThreadsIsLessThanOne() throws Exception {
        try {
            final int numberOfConcurrentThreads = 0;
            ITask task = DelayedTask.notDelayedTask();

            new ConcurrentTasksExecutor(numberOfConcurrentThreads, task, exceptionTask).execute();
            fail("Execute method should throw IllegalArgument exception.");
        } catch (IllegalArgumentException e) {
            // checking if the exception is related to wrong number of concurrent threads
            assertEquals("Wrong exception message.", "Amount of threads must be higher than zero.", e.getMessage());
        }
    }

    @Test
    public void shouldDoNothingWhenNoTasksSpecified() {
        int numberOfConcurrentThreads = 2;
        new ConcurrentTasksExecutor(numberOfConcurrentThreads).execute();
        // when there is no tasks specified method should finish its work quietly
    }

    @Test
    public void shouldCompleteSuccessfullyWhenNullTasksPassed() {
        final int numberOfConcurrentThreads = 1;
        DelayedTask task = DelayedTask.notDelayedTask();

        new ConcurrentTasksExecutor(numberOfConcurrentThreads, task, null, null).execute();

        //checking if not null task was successfully finished
        assertTrue("Task should started.", task.isStarted());
        assertFalse("Task should not be interrupted.", task.isInterrupted());
        assertTrue("Task should be successfully finished.", task.isFinishedSuccessfully());
    }

    @Test
    public void shouldCompleteSuccessfullyInThreeConcurrentThreads() {
        final int numberOfConcurrentThreads = 3;
        DelayedTask firstTask = DelayedTask.notDelayedTask(), secondTask = DelayedTask.notDelayedTask(), thirdTask = DelayedTask.notDelayedTask(), fourthTask = DelayedTask.notDelayedTask();

        new ConcurrentTasksExecutor(numberOfConcurrentThreads, firstTask, secondTask, thirdTask, fourthTask).execute();

        // all tasks should be finished successfully
        assertTrue("First task should be successfully finished.", firstTask.isFinishedSuccessfully());
        assertTrue("Second task should be successfully finished.", secondTask.isFinishedSuccessfully());
        assertTrue("Third task should be successfully finished.", thirdTask.isFinishedSuccessfully());
        assertTrue("Fourth task should be successfully finished.", fourthTask.isFinishedSuccessfully());

        // checking if proper number of threads was used
        Set<Long> threadIds = Sets.newHashSet(firstTask.getThreadId(), secondTask.getThreadId(), thirdTask.getThreadId(), fourthTask.getThreadId());
        assertEquals("Number of used threads should be the same as specified in the function parameter.", numberOfConcurrentThreads, threadIds.size());
    }

}