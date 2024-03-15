import java.util.*;

/**
 * Ideal Shortest Job First Scheduler
 * 
 * @version 2017
 */
public class IdealSJFScheduler extends AbstractScheduler {

  private Queue<Process> readyQueue;

  public IdealSJFScheduler(){
      readyQueue = new PriorityQueue<>(new Comparator<Process>() {
          public int compare(Process p1, Process p2) {
              return Integer.compare(p1.getNextBurst(), p2.getNextBurst());
          }
      });
  }

  /**
   * Adds a process to the ready queue.
   * usedFullTimeQuantum is true if process is being moved to ready
   * after having fully used its time quantum.
   */
  public void ready(Process process, boolean usedFullTimeQuantum) {
      readyQueue.offer(process);
  }

  /**
   * Removes the next process to be run from the ready queue
   * and returns it.
   * Returns null if there is no process to run.
   */
  public Process schedule() {
      return readyQueue.poll();
  }

  public boolean isPreemptive(){ return false; }

}
