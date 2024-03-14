import java.util.*;
import java.util.Properties;

/**
 * Feedback Round Robin Scheduler
 * 
 * @version 2017
 */
public class FeedbackRRScheduler extends AbstractScheduler {

  private PriorityQueue<Process> readyQueue;
  private int timeQuantum;

  public FeedbackRRScheduler() {
    readyQueue = new PriorityQueue<>(new Comparator<Process>() {
      public int compare(Process p1, Process p2) {
        return Integer.compare(p1.getPriority(), p2.getPriority());
      }
    });
  }

  public void initialize(Properties parameters) {
    timeQuantum = Integer.parseInt(parameters.getProperty("timeQuantum"));
  }

  /**
   * Adds a process to the ready queue.
   * usedFullTimeQuantum is true if process is being moved to ready
   * after having fully used its time quantum.
   */
  public void ready(Process process, boolean usedFullTimeQuantum) {
    if (usedFullTimeQuantum) {
      process.setPriority(process.getPriority()+1);
    }
    readyQueue.add(process);
  }

  /**
   * Removes the next process to be run from the ready queue 
   * and returns it. 
   * Returns null if there is no process to run.
   */
  public Process schedule() {
    System.out.println("Scheduler selects process "+readyQueue.peek());
    return readyQueue.poll();
  }

  public boolean isPreemptive() {
    return true;
  }

  public int getTimeQuantum() {
    return timeQuantum;
  }
}
