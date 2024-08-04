package ro.tuc.ds2023.configuration;

import java.io.FileWriter;
import java.io.IOException;

public class ConfigFileWriter {

    public static void writeToFile(String id, String content) {
        String fileName = "config-" + id + ".txt";
        String filePath = "C:\\Facultate\\DS - Energy management\\Device-Simulator\\src\\main\\resources\\" + fileName;

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(content);
            System.out.println("File created successfully at: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
