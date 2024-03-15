import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Properties;

/**
 * Shortest Job First Scheduler
 *
 * @version 2017
 */

public class SJFScheduler extends AbstractScheduler {

  private PriorityQueue<Process> readyQueue;

  private double alphaBurstEstimate;
  private double initialBurstEstimate;

  public void initialize(Properties parameters) {
    readyQueue = new PriorityQueue<>(new Comparator<Process>() {
      public int compare(Process p1, Process p2) {
        return Double.compare(getBurstEstimate(p1), getBurstEstimate(p2));
      }
    });

    initialBurstEstimate = Double.parseDouble(parameters.getProperty("initialBurstEstimate"));
    alphaBurstEstimate = Double.parseDouble(parameters.getProperty("alphaBurstEstimate"));
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
    return readyQueue.poll();
  }

  public Double getBurstEstimate(Process process){
    if(process.getRecentBurst() == -1){
      return initialBurstEstimate;
    }
    else {
      return (alphaBurstEstimate * process.getRecentBurst()) + ((1 - alphaBurstEstimate) * process.getRecentBurst());
    }
  }

}
