import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

public class SearchProperties {

  public static ArrayList<String> list = new ArrayList<String>();

  private static String rootPath;
  private static String rootPropertiesPath;

  public static boolean isKeyInUse = false;

  public static void main(String[] args) {
    try {

      Date startTime = new Date();

      ArrayList<PathClass> pathList = new ArrayList<PathClass>();
      PathClass alexPathClass = new PathClass();
      alexPathClass.setRootPath("C:\\Users\\bmlikota\\git\\sirius-web-project\\sirius-web\\sirius-web-alex\\src\\main");
      alexPathClass
          .setRootPropertiesPath("C:\\Users\\bmlikota\\git\\sirius-web-project\\sirius-web\\sirius-web-alex\\src\\main\\resources");
      pathList.add(alexPathClass);

      PathClass pbzPathClass = new PathClass();
      pbzPathClass.setRootPath("C:\\Users\\bmlikota\\git\\sirius-web-project\\sirius-web\\sirius-web-pbz\\src\\main");
      pbzPathClass
          .setRootPropertiesPath("C:\\Users\\bmlikota\\git\\sirius-web-project\\sirius-web\\sirius-web-pbz\\src\\main\\resources");
      pathList.add(pbzPathClass);

      PathClass commonPathClass = new PathClass();
      commonPathClass
          .setRootPath("C:\\Users\\bmlikota\\git\\sirius-web-project\\sirius-web");
      commonPathClass
          .setRootPropertiesPath("C:\\Users\\bmlikota\\git\\sirius-web-project\\sirius-web\\sirius-web-common\\src\\main\\resources");
      pathList.add(commonPathClass);

      // PathClass testPathClass = new PathClass();
      // testPathClass
      // .setRootPath("C:\\Users\\bmlikota\\git\\sirius-web-project\\sirius-web\\sirius-web-pbz\\src\\main\\java\\hr\\pbz\\sirius\\web\\controller\\currencyConversion");
      // testPathClass
      // .setRootPropertiesPath("C:\\Users\\bmlikota\\git\\sirius-web-project\\sirius-web\\sirius-web-common\\src\\main\\resources\\messages\\currencyExchange");
      // pathList.add(testPathClass);

      // Iteriram po projektima i brisem parametre iz properties datoteka.
      for (PathClass pClass : pathList) {
        rootPropertiesPath = pClass.getRootPropertiesPath();
        rootPath = pClass.getRootPath();
        goThroughProperties(rootPropertiesPath);
      }

      System.out.println("Keys deleted: " + list.size());

      Date endTime = new Date();
      GregorianCalendar gcStart = new GregorianCalendar();
      gcStart.setTime(startTime);
      GregorianCalendar gcEnd = new GregorianCalendar();
      gcEnd.setTime(endTime);
      System.out.println("Duration: " + (gcEnd.getTimeInMillis() - gcStart.getTimeInMillis()));

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Metoda ide kroz properties datoteke i brise kljuceve koji se ne koriste.
   * @param dirName
   * @throws Exception
   */
  static void goThroughProperties(String dirName) throws Exception {
    File folder = new File(dirName);
    File[] listOfFiles = folder.listFiles();
    for (File file : listOfFiles) {
      if (file.isDirectory()) {
        goThroughProperties(file.getPath());
      } else if (file.getName().endsWith(".properties") && !file.getName().startsWith("TEMP")) {
        System.out.println(file.getPath());
        createTempPropertieFile(file);
        deleteFileCopies(file);
      }
    }
  }

  /**
   * Metoda kreira temp datoteku u kojoj su samo kljucevi koji se koriste.
   * @param file
   * @throws Exception
   */
  static void createTempPropertieFile(File file) throws Exception {
    BufferedReader br = new BufferedReader(new FileReader(file.getPath()));
    try {
      String line = br.readLine();

      ArrayList<String> keysForDelete = new ArrayList<String>();
      while (line != null) {
        if (!line.trim().startsWith("#")) {

          String[] arr = line.split("=");
          String key = arr[0].trim();

          if (key != null && !key.equals("") && !key.equals("#")) {
            isKeyInUse = false;
            checkIsKeyInUse(rootPath, key);
            if (!isKeyInUse) {
              keysForDelete.add(key);
              list.add(key);
            }
          }

        }
        line = br.readLine();
      }

      createTempFile(file, keysForDelete);

    } finally {
      br.close();
    }
  }

  /**
   * Metoda provjerava da li se kljuc iz properties datoteke igdje koristi.
   * @param path
   * @param key
   * @throws Exception
   */
  static void checkIsKeyInUse(String path, String key) throws Exception {

    File folder = new File(path);
    File[] listOfFiles = folder.listFiles();
    for (File file : listOfFiles) {
      if (file.isDirectory()) {
        checkIsKeyInUse(file.getPath(), key);
      } else if (file.getName().endsWith(".java") || file.getName().endsWith(".css") || file.getName().endsWith(".js")
          || file.getName().endsWith(".html")) {
        BufferedReader br = new BufferedReader(new FileReader(file.getPath()));
        try {
          String line = br.readLine();
          while (line != null) {
            if (line.contains(key)) {
              isKeyInUse = true;
            }
            line = br.readLine();
          }
        } finally {
          br.close();
        }
      }
    }

  }

  /**
   * Metoda kreira datoteke u koje sprema samo kljuceve koji se koriste.
   * @param file
   * @param keysForDelete
   * @throws Exception
   */
  static void createTempFile(File file, ArrayList<String> keysForDelete) throws Exception {
    if (keysForDelete.isEmpty()) {
      return;
    }

    File inputFile = new File(file.getPath());
    File tempFile = new File(file.getParent() + "\\TEMP" + file.getName());

    BufferedReader reader = new BufferedReader(new FileReader(inputFile));
    BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

    String currentLine;

    while ((currentLine = reader.readLine()) != null) {

      String[] arr = currentLine.split("=");
      String currentKey = arr[0].trim();
      if (keysForDelete.contains(currentKey)) {
        continue;
      }

      writer.write(currentLine);
      writer.write("\n");
      writer.flush();
    }

    writer.close();
    reader.close();

  }

  /**
   * Metoda brise originalne properties file-ove, a iz naziva temp fileova brise
   * prefiks TEMP.
   * @param file
   * @throws Exception
   */
  static void deleteFileCopies(File file) throws Exception {
    File inputFile = new File(file.getPath());
    File tempFile = new File(file.getParent() + "\\TEMP" + file.getName());
    if (tempFile.exists()) {
      inputFile.delete();
      if (isFileEmpty(tempFile)) {
        tempFile.delete();
      } else {
        tempFile.renameTo(inputFile);
      }
    }
  }

  static boolean isFileEmpty(File file) throws Exception {
    boolean ret = true;

    BufferedReader reader = new BufferedReader(new FileReader(file));
    String currentLine;
    while ((currentLine = reader.readLine()) != null) {
      if (!currentLine.trim().equals("") && !currentLine.trim().startsWith("#")) {
        ret = false;
      }
    }
    reader.close();

    return ret;
  }
}