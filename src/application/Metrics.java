package application;

public class Metrics {
	private final double accuracy;
	private final double precision;
	private final double recall;
	private final double fScore;

	public Metrics(double accuracy, double precision, double recall, double fScore) {
		this.accuracy = accuracy;
		this.precision = precision;
		this.recall = recall;
		this.fScore = fScore;
	}

	public double getAccuracy() {
		return accuracy;
	}

	public double getPrecision() {
		return precision;
	}

	public double getRecall() {
		return recall;
	}

	public double getFScore() {
		return fScore;
	}
}
