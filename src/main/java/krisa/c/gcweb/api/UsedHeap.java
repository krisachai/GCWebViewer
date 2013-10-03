package krisa.c.gcweb.api;

import java.util.Iterator;

import com.tagtraum.perf.gcviewer.model.GCEvent;
import com.tagtraum.perf.gcviewer.model.GCModel;
/**
 * @author <a href="mailto:gcviewer@gmx.ch">Joerg Wuethrich</a>
 * <p>created on: 22.07.2012</p>
 * licensed under LGPL http://www.gnu.org/licenses/lgpl.txt
 */
public class UsedHeap {
	 public DataPoint computePolygon(GCModel model) {
		 DataPoint polygon = new DataPoint();
	        for (Iterator<GCEvent> i = model.getGCEvents(); i.hasNext();) {
	            GCEvent event = i.next();
	            // e.g. "GC remark" of G1 algorithm does not contain memory information
	            if (event.getTotal() > 0) {
	                final double timestamp = event.getTimestamp() - model.getFirstPauseTimeStamp();
	                polygon.addPoint(timestamp, event.getPreUsed());
	                polygon.addPoint(timestamp, event.getPostUsed());
	            }
	        }
	        // dummy point to make the polygon complete
	        polygon.addPoint(model.getRunningTime(), 0.0d);
	        //System.out.println("last x coord " + polygon.xpoints[polygon.npoints-1]);
	        return polygon;
	    }
}
