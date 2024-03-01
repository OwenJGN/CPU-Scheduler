import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Properties;
import java.util.Queue;

/**
 * Shortest Job First Scheduler
 * 
 * @version 2017
 */
public class SJFScheduler extends AbstractScheduler {

  private PriorityQueue<Process> readyQueue;
  private double burstEstimate;
  private double alphaBurstEstimate;

  public SJFScheduler(){
    readyQueue = new PriorityQueue<>(new Comparator<Process>() {
      @Override
      public int compare(Process p1, Process p2) {
        p1.setPriority((int) (burstEstimate = alphaBurstEstimate + p2.getNextBurst() +(1-alphaBurstEstimate) * burstEstimate));
        return Integer.compare(p1.getPriority(), p2.getPriority());
      }
    });
  }
  @Override
  public void initialize(Properties parameters) {
    burstEstimate = Double.parseDouble(parameters.getProperty("initialBurstEstimate"));
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
    System.out.println("Scheduler selects process "+readyQueue.poll());
    return readyQueue.poll();
  }
}
