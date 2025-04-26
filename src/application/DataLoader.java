package application;

import java.io.*;
import java.util.*;

public class DataLoader {

	public static List<Map<String, String>> loadData(String filePath) throws IOException {
		List<Map<String, String>> data = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(filePath));

		String headerLine = br.readLine();
		if (headerLine == null || headerLine.isEmpty() || !headerLine.contains(",")) {
			throw new IllegalArgumentException("The file is empty or has an invalid header!");
		}

		String[] headers = headerLine.split(",");
		String line;

		while ((line = br.readLine()) != null) {
			String[] values = line.split(",");

			if (values.length != headers.length) {
				continue;
			}

			Map<String, String> row = new HashMap<>();
			boolean skipRow = false;

			for (int i = 0; i < headers.length; i++) {
				String value = values[i].trim();

				if (value.isEmpty()) {
					skipRow = true;
					break;
				}

				row.put(headers[i].trim(), value);
			}

			if (!skipRow) {
				data.add(row);
			}
		}

		br.close();
		return data;
	}

	public static List<String> loadColumnsFromFile(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String headerLine = br.readLine();
		if (headerLine == null || headerLine.isEmpty() || !headerLine.contains(",")) {
			throw new IllegalArgumentException("The file is empty or has an invalid header!");
		}

		br.close();
		String[] headers = headerLine.split(",");
		List<String> columns = new ArrayList<>();
		for (String header : headers) {
			columns.add(header.trim());
		}
		return columns;
	}

	public static Map<String, List<Map<String, String>>> splitData(List<Map<String, String>> data, double trainRatio) {
		List<Map<String, String>> shuffledData = new ArrayList<>(data);
		Collections.shuffle(shuffledData);

		int trainSize = (int) (shuffledData.size() * trainRatio);
		List<Map<String, String>> trainData = new ArrayList<>();
		List<Map<String, String>> testData = new ArrayList<>();

		for (int i = 0; i < shuffledData.size(); i++) {
			if (i < trainSize) {
				trainData.add(new HashMap<>(shuffledData.get(i)));
			} else {
				testData.add(new HashMap<>(shuffledData.get(i)));
			}
		}

		Map<String, List<Map<String, String>>> splitData = new HashMap<>();
		splitData.put("train", trainData);
		splitData.put("test", testData);

		return splitData;
	}
}
