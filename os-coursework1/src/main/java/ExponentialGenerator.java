import java.util.Random;

/**
 * @author Peter Schrammel, Ian Wakeman
 * @version 2017
 *
 * A simple exponential random generator. Uses the pseudo random generator in 
 * {@link java.util.Random} 
 */
public class ExponentialGenerator {
  private Random rand;
  private double mean;
  private double alphaBurstEstimate;
  private double initialBurstEstimate;
  /**
   * @param seed An instance of {@link java.util.Random}
   * @param mean What average to use in the distribution
   */
  public ExponentialGenerator(Random  rand, double mean, double initial, double alpha) {
    this.mean = mean;
    this.rand = rand;
    this.initialBurstEstimate = initial;
    this.alphaBurstEstimate = alpha;
  }
	
  /**
   * @return The next integer in the sequence
   */
  public int nextInt() {
    return (int)Math.ceil(-mean * Math.log(1-rand.nextDouble()));
  }

  public int estimateNextBurst(int currentBurst){
    return (int) (this.alphaBurstEstimate * currentBurst +(1 - this.alphaBurstEstimate) * this.initialBurstEstimate );
  }
}
