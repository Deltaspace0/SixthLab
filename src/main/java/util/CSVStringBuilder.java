package util;

import java.util.ArrayList;

public class CSVStringBuilder {
    private String line = "";

    public CSVStringBuilder append(String field) {
        if (!line.equals(""))
            line += ",";
        line += "\"" + field.replace("\"", "\"\"") + "\"";
        return this;
    }

    public String build() {
        return line;
    }

    public static ArrayList<String> parse(String line) {
        boolean parsingField = false;
        boolean quoteDuringParsing = false;
        ArrayList<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        for (char c : line.toCharArray()) {
            if (c == '"' && !parsingField) {
                parsingField = true;
                continue;
            }
            if (c == '"' && parsingField && !quoteDuringParsing) {
                quoteDuringParsing = true;
                continue;
            }
            if (c == '"' && parsingField && quoteDuringParsing) {
                field.append('"');
                quoteDuringParsing = false;
                continue;
            }
            if (c == ',' && parsingField && quoteDuringParsing) {
                parsingField = false;
                fields.add(field.toString());
                field.setLength(0);
                quoteDuringParsing = false;
                continue;
            }
            if (parsingField) {
                field.append(c);
                quoteDuringParsing = false;
            }
        }
        fields.add(field.toString());
        return fields;
    }
}
