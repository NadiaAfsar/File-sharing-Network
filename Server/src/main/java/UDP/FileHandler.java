package UDP;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileHandler {
    public ArrayList<byte[]> extractData(File file) {
        ArrayList<byte[]> data = new ArrayList<>();
        try {
            long size = getSize(file);
            int packs = getPacketsNumber(size);
            int lastPack = (int)(size % 65507);
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            for (int i = 0; i < packs-1; i++) {
                byte[] fileContent = new byte[1000];
                bufferedInputStream.read(fileContent);
                data.add(fileContent);
            }
            if (lastPack != 0) {
                byte[] fileContent = new byte[lastPack];
                bufferedInputStream.read(fileContent);
                data.add(fileContent);
            }
            bufferedInputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return data;
    }
    public static long getSize(File file) {
        try {
            return Files.size(Paths.get(file.getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static int getPacketsNumber(long size) {
        if (size % 65507 == 0) {
            return (int)(size / 65507);
        }
        else {
            return ((int)(size / 65507)+1);
        }
    }
    public void createFile(String name, String username, ArrayList<byte[]> data) {
        File folder = new File("src/main/java/SavedFiles/"+username);
        if (!folder.exists()) {
            folder.mkdir();
        }
        File file = getAvailableName(name, "src/main/java/SavedFiles/"+username);
        try {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            for (int i = 0; i < data.size(); i++) {
                bufferedOutputStream.write(data.get(i));
            }
            bufferedOutputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    private File getAvailableName(String name, String path) {
        File file = new File(path+"/"+name);
        if (file.exists()) {
            int x = 1;
            while (file.exists()) {
                file = new File(path+"/"+name+"("+x+")");
                x++;
            }
        }
        return file;
    }
}
