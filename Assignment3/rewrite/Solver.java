public abstract class Solver {
    public abstract int[] solve();
    public abstract long getPartialExploredSize();
    public abstract long getFullyExploredSize();
    public abstract long getTotalExploredSize();
    public abstract int getSolutionProductivity();
}
