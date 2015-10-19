import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedHashMap;
import java.util.Set;

public class Merge2 {
  public static void main(String[] args) throws Exception {
    File eng = new File(
        "C:\\Users\\bmlikota\\git\\sirius-web-project\\sirius-web\\sirius-web-common\\src\\main\\resources\\messages\\webMessages\\webMessagesSpecific_en.properties");
    File defaultProp = new File(
        "C:\\Users\\bmlikota\\git\\sirius-web-project\\sirius-web\\sirius-web-common\\src\\main\\resources\\messages\\webMessages\\webMessagesSpecific.properties");

    LinkedHashMap<String, String> engData = getData(eng);
    LinkedHashMap<String, String> defaultData = getData(defaultProp);

    BufferedWriter writer = new BufferedWriter(new FileWriter(defaultProp));
    Set<String> keySet = defaultData.keySet();
    for (String key : keySet) {
      if (!key.trim().startsWith("#")) {
        writer.write(engData.get(key));
      } else {
        writer.write(defaultData.get(key));
      }
      writer.write("\n");
      writer.flush();
    }
    writer.close();

    System.out.println("done");
  }

  static LinkedHashMap<String, String> getData(File file) throws Exception {
    LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
    BufferedReader br = new BufferedReader(new FileReader(file));

    String line = null;
    int mapKey = 0;
    while ((line = br.readLine()) != null) {
      if (!line.trim().startsWith("#")) {
        String[] arr = line.split("=");
        String key = arr[0].trim();
        map.put(key, line);
      } else {
        map.put("" + (mapKey++), line);
      }

    }
    br.close();
    return map;
  }
}
