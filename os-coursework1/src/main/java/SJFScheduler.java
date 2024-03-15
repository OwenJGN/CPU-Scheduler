import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Properties;

public class SJFScheduler extends AbstractScheduler {

  private PriorityQueue<Process> readyQueue;

  private double alphaBurstEstimate;
  private double initialBurstEstimate;

  public SJFScheduler() {
    readyQueue = new PriorityQueue<>(new Comparator<Process>() {
      public int compare(Process p1, Process p2) {
        return Double.compare(getBurstEstimate(p1), getBurstEstimate(p2));
      }
    });
  }

  @Override
  public void initialize(Properties parameters) {
    initialBurstEstimate = Double.parseDouble(parameters.getProperty("initialBurstEstimate"));
    alphaBurstEstimate = Double.parseDouble(parameters.getProperty("alphaBurstEstimate"));
  }

  public Double getBurstEstimate(Process process){
    if(process.getRecentBurst() == -1){
      return initialBurstEstimate;
    }
    else {
      return (alphaBurstEstimate * process.getRecentBurst()) + ((1 - alphaBurstEstimate) * process.getRecentBurst());
    }
  }

  public void ready(Process process, boolean usedFullTimeQuantum) {
    readyQueue.add(process);
  }

  public Process schedule() {
    return readyQueue.poll();
  }
}
