

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import de.micromata.opengis.kml.v_2_2_0.*;

//import de.micromata.jak.*;

public class KmlReader extends Object {

	static public void procPolygon(Polygon pg) {
		LinearRing linearRing = pg.getOuterBoundaryIs().getLinearRing();
		List<Coordinate> coordinates = linearRing.getCoordinates();
		for (Coordinate coordinate : coordinates) {
		    System.out.printf("    lon: %f \t lat: %f \t alt: %f\n", 
		    		coordinate.getLongitude(), 
		    		coordinate.getLatitude(), 
		    		coordinate.getAltitude());
		}
	}
	
	static public void procMultiGeometry(MultiGeometry mg) {
		List<Geometry> listGeometry = mg.getGeometry();
		for (Geometry geom : listGeometry) {
			procGeometry(geom);
		}
	}
	
	static public void procGeometry(Geometry geom) {
		if (geom instanceof Polygon) {
			procPolygon((Polygon)geom);
		} else if (geom instanceof MultiGeometry) {
			procMultiGeometry((MultiGeometry)geom);
		}
		
	}
	
	static public void procSimpleData(SimpleData simple) {
		System.out.printf("  name: %s \t value: %s\n", 
				simple.getName(), 
				simple.getValue());
	}
	
	static public void procSchemaData(SchemaData schema) {
		for (SimpleData simple: schema.getSimpleData()) {
			procSimpleData(simple);
		}
	}
	
	static public void procPlacemark(Placemark placemark) {
		for (SchemaData schema : placemark.getExtendedData().getSchemaData()) {
			procSchemaData(schema);
		}
		procGeometry(placemark.getGeometry());
	}
	
	static public void procFolder(Folder folder)	{
		System.out.printf("Folder name: %s\n", folder.getName());
		List<Feature> listPlacemark = folder.getFeature();
		int i = 0;
		for (Feature feature2 : listPlacemark) {
			System.out.printf("Placemark %d\n", i);
			procPlacemark((Placemark)feature2);
		}
		
	}
	static public void procDocument(Document doc) {
		List<Feature> listFolder = doc.getFeature();
		for (Feature feature1 : listFolder) {
			procFolder((Folder)feature1);
		}
		
	}
	static public void read(String fileName) {
		JAXBContext jc = null;
		Kml kml = null;
		try {
			jc = JAXBContext.newInstance(Kml.class);

			// create KML reader to parse arbitrary KML into Java Object structure
			Unmarshaller u = jc.createUnmarshaller();
			kml = (Kml) u.unmarshal(new File(fileName));
		} catch (JAXBException e) {
			
		}
		
		procDocument((Document) kml.getFeature());
	}
}
