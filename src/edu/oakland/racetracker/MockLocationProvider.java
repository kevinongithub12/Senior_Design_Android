package edu.oakland.racetracker;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
/**
 * 
 * @author Lukas Greib
 * Mock location provider used in testing
 */
public class MockLocationProvider {
  String providerName;
  Context ctx;
 
  public MockLocationProvider(String name, Context ctx) {
    this.providerName = name;
    this.ctx = ctx;
 
    LocationManager lm = (LocationManager) ctx.getSystemService(
      Context.LOCATION_SERVICE);
    if(lm.getProvider(providerName) != null){
        lm.removeTestProvider(providerName);
    }
    lm.addTestProvider(providerName, false, false, false, false, false, true, true, 0, 5);
    lm.setTestProviderEnabled(providerName, true);
  }
 
  public void pushLocation(double lat, double lon) {
    LocationManager lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
 
    Location mockLocation = new Location(providerName);
    mockLocation.setElapsedRealtimeNanos(1);
    mockLocation.setAccuracy(0);
    mockLocation.setLatitude(lat);
    mockLocation.setLongitude(lon); 
    mockLocation.setAltitude(0); 
    mockLocation.setTime(System.currentTimeMillis()); 
    lm.setTestProviderLocation(providerName, mockLocation);
  }
 
  public void shutdown() {
    LocationManager lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
    lm.removeTestProvider(providerName);
  }
}