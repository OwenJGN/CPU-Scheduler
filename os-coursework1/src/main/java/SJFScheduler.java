import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Properties;

public class SJFScheduler extends AbstractScheduler {

  private PriorityQueue<Process> readyQueue;
  private Map<Integer, Double> burstEstimates;
  private double alpha;
  private double initialBurstEstimate;

  public SJFScheduler() {
    burstEstimates = new HashMap<>();
    readyQueue = new PriorityQueue<>(new Comparator<Process>() {
      @Override
      public int compare(Process p1, Process p2) {
        return Double.compare(getBurstEstimate(p1.getId()), getBurstEstimate(p2.getId()));
      }
    });
  }

  @Override
  public void initialize(Properties parameters) {
    initialBurstEstimate = Double.parseDouble(parameters.getProperty("initialBurstEstimate"));
    alpha = Double.parseDouble(parameters.getProperty("alphaBurstEstimate"));
  }

  private double getBurstEstimate(int processId) {
    return burstEstimates.getOrDefault(processId, initialBurstEstimate);
  }

  private void updateBurstEstimate(Process process) {
    int processId = process.getId();
    double lastBurst = process.getRecentBurst();
    double previousEstimate = getBurstEstimate(processId);
    double newEstimate = alpha * lastBurst + (1 - alpha) * previousEstimate;
    burstEstimates.put(processId, newEstimate);
  }

  public void ready(Process process, boolean usedFullTimeQuantum) {
    updateBurstEstimate(process);
    readyQueue.add(process);
  }

  public Process schedule() {
    return readyQueue.poll();
  }
}
