import java.io.File;

public class TestKmlReader {

	public static void main(String[] args) {
		System.out.println("testing kml reader");
        File directory = new File("./");
        System.out.println(directory.getAbsolutePath());
        
		KmlReader.read("./src/test/resources/sample.kml");

	}

}
