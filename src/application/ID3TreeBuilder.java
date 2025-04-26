package application;

import java.util.*;

public class ID3TreeBuilder {

	private int maxDepth;
	private int minSamplesSplit;

	public ID3TreeBuilder(int maxDepth, int minSamplesSplit) {
		this.maxDepth = maxDepth;
		this.minSamplesSplit = minSamplesSplit;
	}

	public Node buildTree(List<Map<String, String>> data, List<String> attributes, String labelColumn) {
		return buildTree(data, attributes, labelColumn, 0, maxDepth, minSamplesSplit);
	}

	public Node buildTree(List<Map<String, String>> data, List<String> attributes, String labelColumn, int depth,
			int maxDepth, int minSamplesSplit) {

		if (checkRows(data, labelColumn)) {
			return new Node(data.get(0).get(labelColumn), true);
		}

		if (attributes.isEmpty()) {
			return new Node(getMajorityClass(data, labelColumn), true);
		}

		if (depth >= maxDepth || data.size() < minSamplesSplit) {
			return new Node(getMajorityClass(data, labelColumn), true);
		}

		String bestAttribute = chooseBestAttribute(data, attributes, labelColumn);
		Node root = new Node(bestAttribute);

		Map<String, List<Map<String, String>>> subsets = splitData(data, bestAttribute);

		List<String> remainingAttributes = new ArrayList<>(attributes);
		remainingAttributes.remove(bestAttribute);

		for (Map.Entry<String, List<Map<String, String>>> entry : subsets.entrySet()) {
			String value = entry.getKey();
			List<Map<String, String>> subset = entry.getValue();

			if (subset.isEmpty()) {
				root.addChild(value, new Node(getMajorityClass(data, labelColumn), true));
			} else {
				root.addChild(value,
						buildTree(subset, remainingAttributes, labelColumn, depth + 1, maxDepth, minSamplesSplit));
			}
		}

		return root;
	}
	
	private String getMajorityClass(List<Map<String, String>> data, String labelColumn) {
		Map<String, Integer> labelCounts = new HashMap<>();
		for (Map<String, String> row : data) {
			String label = row.get(labelColumn);
			labelCounts.put(label, labelCounts.getOrDefault(label, 0) + 1);
		}

		return labelCounts.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
	}

	private boolean checkRows(List<Map<String, String>> data, String labelColumn) {
		String firstLabel = data.get(0).get(labelColumn);
		for (Map<String, String> row : data) {
			if (!row.get(labelColumn).equals(firstLabel)) {
				return false;
			}
		}
		return true;
	}

	private String chooseBestAttribute(List<Map<String, String>> data, List<String> attributes, String labelColumn) {
		double baseEntropy = calculateEntropy(data, labelColumn);
		String bestAttribute = null;
		double maxGainRatio = Double.NEGATIVE_INFINITY;

		for (String attribute : attributes) {
			double informationGain = calculateInformationGain(data, attribute, labelColumn, baseEntropy);
			double splitInfoValue = calculatesplitInfoValue(data, attribute);
			double gainRatio = informationGain ;

			if (gainRatio > maxGainRatio) {
				maxGainRatio = gainRatio;
				bestAttribute = attribute;
			}
		}

		return bestAttribute;
	}

	private double calculatesplitInfoValue(List<Map<String, String>> data, String attribute) {
		Map<String, Integer> attributeValueCounts = new HashMap<>();
		for (Map<String, String> row : data) {
			String value = row.get(attribute);
			attributeValueCounts.put(value, attributeValueCounts.getOrDefault(value, 0) + 1);
		}

		double splitInfoValue = 0.0;
		int total = data.size();
		for (int count : attributeValueCounts.values()) {
			double probability = (double) count / total;
			if (probability > 0) {
				splitInfoValue -= probability * (Math.log(probability) / Math.log(2));
			}
		}

		return splitInfoValue;
	}

	private double calculateEntropy(List<Map<String, String>> data, String labelColumn) {
		if (data.isEmpty())
			return 0.0;

		Map<String, Integer> labelCounts = new HashMap<>();
		for (Map<String, String> row : data) {
			String label = row.get(labelColumn);
			labelCounts.put(label, labelCounts.getOrDefault(label, 0) + 1);
		}

		double entropy = 0.0;
		int total = data.size();
		for (int count : labelCounts.values()) {
			double probability = (double) count / total;
			entropy -= probability * (Math.log(probability) / Math.log(2));
		}

		return entropy;
	}

	private double calculateInformationGain(List<Map<String, String>> data, String attribute, String labelColumn,
			double baseEntropy) {
		Map<String, List<Map<String, String>>> subsets = splitData(data, attribute);
		double weightedEntropy = 0.0;

		for (List<Map<String, String>> subset : subsets.values()) {
			if (subset.isEmpty())
				continue;
			double subsetEntropy = calculateEntropy(subset, labelColumn);
			weightedEntropy += ((double) subset.size() / data.size()) * subsetEntropy;
		}

		return baseEntropy - weightedEntropy;
	}

	
	private Map<String, List<Map<String, String>>> splitData(List<Map<String, String>> data, String attribute) {
		Map<String, List<Map<String, String>>> subsets = new HashMap<>();
		for (Map<String, String> row : data) {
			String value = row.get(attribute);
			if (value == null)
				continue;
			subsets.putIfAbsent(value, new ArrayList<>());
			subsets.get(value).add(row);
		}
		return subsets;
	}

}
