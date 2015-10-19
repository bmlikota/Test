import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class Merge {

  public static void main(String[] args) throws Exception {
    File eng = new File(
        "C:\\Users\\bmlikota\\git\\sirius-web-project\\sirius-web\\sirius-web-common\\src\\main\\resources\\messages\\widgets\\widgets_en.properties");
    File defaultProp = new File(
        "C:\\Users\\bmlikota\\git\\sirius-web-project\\sirius-web\\sirius-web-common\\src\\main\\resources\\messages\\widgets\\widgets.properties");

    ArrayList<String> keyNotInDefaultSet = keyNotInDefault(eng, defaultProp);

    BufferedWriter writer = new BufferedWriter(new FileWriter(defaultProp, true));
    for (String line : keyNotInDefaultSet) {
      writer.write(line);
      writer.write("\n");
      writer.flush();
    }
    writer.close();

    System.out.println("done");
  }

  static ArrayList<String> keyNotInDefault(File eng, File defaultProp) throws Exception {
    ArrayList<String> lines = new ArrayList<String>();
    BufferedReader brEng = new BufferedReader(new FileReader(eng.getPath()));
    String lineEng = brEng.readLine();
    while (lineEng != null) {
      if (!lineEng.trim().startsWith("#")) {
        String[] arrEng = lineEng.split("=");
        String keyEng = arrEng[0].trim();

        boolean flag = false;
        BufferedReader brDef = new BufferedReader(new FileReader(defaultProp.getPath()));
        String lineDef = brDef.readLine();
        while (lineDef != null) {
          if (!lineDef.trim().startsWith("#")) {
            String[] arrDef = lineDef.split("=");
            String keyDef = arrDef[0].trim();

            if (keyDef.equals(keyEng)) {
              flag = true;
            }

          }
          lineDef = brDef.readLine();
        }
        if (!flag) {
          lines.add(lineEng);
        }
        brDef.close();

      }
      lineEng = brEng.readLine();
    }
    brEng.close();
    return lines;
  }

}
