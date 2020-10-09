package utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.logging.Logger;

public class SamplePersonData {


    private static List<Person> data = new ArrayList<>();

    public static List<Person> get() {

        if (data.isEmpty()) {
            initializeData();
        }

        return data;
    }

    private static void initializeData() {



        try {
            InputStream inputStream = SamplePersonData.class.getResourceAsStream("/utils/details.csv");

            CSVParser parser = CSVParser.parse(inputStream, Charset.defaultCharset(), CSVFormat.DEFAULT);
            Iterator<CSVRecord> iterator = parser.iterator();

            if (!iterator.hasNext()) return;

            iterator.next();

            iterator.forEachRemaining((record) ->
            {
                data.add(new Person(record.get(0), record.get(1), record.get(2), record.get(3), record.get(4)));
            });

            Collections.sort(data);


        } catch (IOException e) {
            Logger.getGlobal().warning("Sample data failed to initialize. Empty list returned.");
        }
    }
}
