import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.zip.GZIPInputStream;

public class Reader {


    private transient BufferedReader reader;
    private transient InputStream gzipStream;

    public int servingSpeed = 1;


    private static transient DateTimeFormatter timeFormatter =
            DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withLocale(Locale.US).withZoneUTC();
            ArrayList<TaxiRide> rides = new ArrayList<TaxiRide>();



     void read() throws IOException, InterruptedException {
         int counter = 0;
         long servingStartTime = Calendar.getInstance().getTimeInMillis();
         long dataStartTime=0;

         DateTime comp =  DateTime.parse( "2013-01-02 00:00:00", timeFormatter);
        gzipStream = new GZIPInputStream(new FileInputStream("C:\\Users\\m.ezzeddine\\Desktop\\Experiments\\gzippp\\nycTaxiRides.gz"));
        reader = new BufferedReader(new InputStreamReader(gzipStream, "UTF-8"));
         String line;
         TaxiRide ride;

         if ( (line = reader.readLine()) != null) {
             // read first ride
             ride = TaxiRide.fromString(line);
             //System.out.println(ride);

             dataStartTime = getEventTime(ride);

             counter++;
             rides.add(ride);
         }

         while ((line = reader.readLine()) != null) {

             long rideEventTime;
             ride = TaxiRide.fromString(line);
             //System.out.println(ride);
             rides.add(ride);
             counter++;

             rideEventTime = getEventTime(ride);

             long now = Calendar.getInstance().getTimeInMillis();
             long servingTime = toServingTime(servingStartTime, dataStartTime, rideEventTime);
             long waitTime = servingTime - now;


             //1 hour = 24344


             if(waitTime> 0){
                 System.out.println("sleeping for " + waitTime);
             }

             Thread.sleep( (waitTime > 0) ? waitTime : 0);

             System.out.println("serving ride " +  ride.toString());

             if(counter==24344/*1000*/)
                 break;

         }

      /*   System.out.println(counter);

         for (TaxiRide r  : rides) {
            // if(r.startTime.isBefore(comp.toInstant())   && r.isStart)
             System.out.println(r);
         }
*/

         this.reader.close();
         this.reader = null;
         this.gzipStream.close();
         this.gzipStream = null;





    }


    public long toServingTime(long servingStartTime, long dataStartTime, long eventTime) {
        long dataDiff = eventTime - dataStartTime;
        return servingStartTime + (dataDiff / this.servingSpeed);
    }

    public long getEventTime(TaxiRide ride) {
        if (ride.isStart) {
            return ride.startTime.getMillis();
        }
        else {
            return ride.endTime.getMillis();
        }
    }


}
