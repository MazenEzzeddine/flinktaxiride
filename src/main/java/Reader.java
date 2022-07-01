import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.zip.GZIPInputStream;

public class Reader {


    private transient BufferedReader reader;
    private transient InputStream gzipStream;


    private static transient DateTimeFormatter timeFormatter =
            DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withLocale(Locale.US).withZoneUTC();
            ArrayList<TaxiRide> rides = new ArrayList<TaxiRide>();



     void read() throws IOException {
         int counter = 0;
         DateTime comp =  DateTime.parse( "2013-01-02 00:00:00", timeFormatter);
        gzipStream = new GZIPInputStream(new FileInputStream("C:\\Users\\m.ezzeddine\\Desktop\\Experiments\\gzippp\\nycTaxiRides.gz"));
        reader = new BufferedReader(new InputStreamReader(gzipStream, "UTF-8"));
         String line;
         TaxiRide ride;

         if (reader.ready() && (line = reader.readLine()) != null) {
             // read first ride
             ride = TaxiRide.fromString(line);
             //System.out.println(ride);
             counter++;
             rides.add(ride);
         }


         if (reader.ready() && (line = reader.readLine()) != null) {
             // read first ride
             ride = TaxiRide.fromString(line);
             counter++;
             rides.add(ride);
         }

         while (reader.ready() && (line = reader.readLine()) != null) {
             ride = TaxiRide.fromString(line);
             //System.out.println(ride);
             counter++;
             rides.add(ride);
         }

         System.out.println(counter);
         int cc =0;

         for (TaxiRide r  : rides) {
             if(r.startTime.isBefore(comp.toInstant())   && r.isStart)
             System.out.println(r);
             cc++;
             if(cc == 500)
                 break;
         }


         this.reader.close();
         this.reader = null;
         this.gzipStream.close();
         this.gzipStream = null;





    }


}
