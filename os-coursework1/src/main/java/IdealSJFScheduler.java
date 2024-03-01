import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;

/**
 * Ideal Shortest Job First Scheduler
 * 
 * @version 2017
 */
public class IdealSJFScheduler extends AbstractScheduler {

  private Queue<Process> readyQueue;

  public IdealSJFScheduler(){
    readyQueue = new LinkedList<>();
  }

  /**
   * Adds a process to the ready queue.
   * usedFullTimeQuantum is true if process is being moved to ready
   * after having fully used its time quantum.
   */
  public void ready(Process process, boolean usedFullTimeQuantum) {
    readyQueue.add(process);
  }

  /**
   * Removes the next process to be run from the ready queue 
   * and returns it. 
   * Returns null if there is no process to run.
   */
  public Process schedule() {
    if (readyQueue.isEmpty()) {
      return null;
    }

    Process shortestProcess = readyQueue.peek();
    int shortestBurst = shortestProcess.getNextBurst();

    for (Process process : readyQueue) {
      int nextBurst = process.getNextBurst();
      if (nextBurst < shortestBurst) {
        shortestBurst = nextBurst;
        shortestProcess = process;
      }
    }
    System.out.println("Scheduler selects process "+readyQueue.remove(shortestProcess));
    return shortestProcess;
  }
}
