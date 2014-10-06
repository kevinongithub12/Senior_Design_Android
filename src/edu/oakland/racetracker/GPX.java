package edu.oakland.racetracker;

import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class GPX {
	public Point startPoint;
	public Point endPoint;
	public List<Point> wayPoints;
	
	public GPX(String xml){
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	        factory.setNamespaceAware(true);
	        XmlPullParser xpp = factory.newPullParser();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString(){
		StringBuilder stringBuilder = new StringBuilder("");
		if(startPoint != null){
			stringBuilder.append(
					"<spt"+
			        " lat="+startPoint.latitude+
			        " lon="+startPoint.longitude+
			        " alt="+startPoint.altitude+
			        " tme="+startPoint.time+
			        " />"
					);
		}
		return stringBuilder.toString();
	}
}
