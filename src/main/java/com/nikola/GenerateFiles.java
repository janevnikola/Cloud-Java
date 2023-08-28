package com.nikola;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class GenerateFiles {
    private String fileInfo;

    //English:
    //We need to create a file generator, where the file has 500 000 records
    //this is the whole class for doing that
    public void generateFile() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < 500000; i++) {
            LocalDate randomDate = createRandomDate(1900, 2000);
            stringBuilder.append(randomDate);
            stringBuilder.append(System.getProperty("line.separator"));
        }

        fileInfo = stringBuilder.toString();
    }

    public int createRandomIntBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }

    public LocalDate createRandomDate(int startYear, int endYear) {
        int day = createRandomIntBetween(1, 28);
        int month = createRandomIntBetween(1, 12);
        int year = createRandomIntBetween(startYear, endYear);
        return LocalDate.of(year, month, day);
    }

    public String getFileInfo() {
        return fileInfo;
    }
}