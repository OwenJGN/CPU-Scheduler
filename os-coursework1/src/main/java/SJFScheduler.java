import java.util.Properties;
import java.util.Queue;

/**
 * Shortest Job First Scheduler
 * 
 * @version 2017
 */
public class SJFScheduler extends AbstractScheduler {

  private Queue<Process> readyQueue;
  private int initialBurstEstimate;
  private int alphaBurstEstimate;

  @Override
  public void initialize(Properties parameters) {
    initialBurstEstimate = Integer.parseInt(parameters.getProperty("initialBurstEstimate"));
    alphaBurstEstimate = Integer.parseInt(parameters.getProperty("alphaBurstEstimate"));

  }

  /**
   * Adds a process to the ready queue.
   * usedFullTimeQuantum is true if process is being moved to ready
   * after having fully used its time quantum.
   */
  public void ready(Process process, boolean usedFullTimeQuantum) {

    // TODO

  }

  /**
   * Removes the next process to be run from the ready queue
   * and returns it.
   * Returns null if there is no process to run.
   */
  public Process schedule() {

    // TODO

    return null;
  }
}
