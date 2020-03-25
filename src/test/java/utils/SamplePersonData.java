package utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class SamplePersonData {


    private static List<Person> data;

    public static List<Person> get() {

        if (Objects.isNull(data)) {
            initializeData();
        }

        return data;
    }

    private static void initializeData() {


        data = new ArrayList<>();


        URL aussies = SamplePersonData.class.getResource("/details.csv");

        try {
            FileReader reader = new FileReader(aussies.getFile());

            CSVParser parser = CSVParser.parse(reader, CSVFormat.DEFAULT);
            Iterator<CSVRecord> iterator = parser.iterator();

            if (!iterator.hasNext()) return;

            iterator.next();

            iterator.forEachRemaining( (record) ->
            {
                data.add(new Person(record.get(0), record.get(1)));
            });

            Collections.sort(data);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
