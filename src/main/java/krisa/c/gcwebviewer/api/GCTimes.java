package krisa.c.gcwebviewer.api;

import java.util.Iterator;

import com.tagtraum.perf.gcviewer.model.GCEvent;
import com.tagtraum.perf.gcviewer.model.GCModel;
/**
 * @author <a href="mailto:gcviewer@gmx.ch">Joerg Wuethrich</a>
 * <p>created on: 22.07.2012</p>
 * licensed under LGPL http://www.gnu.org/licenses/lgpl.txt
 */
public class GCTimes {
	public DataPoint computePolygon(GCModel model) {
		DataPoint polygon = new DataPoint();
        for (Iterator<GCEvent> i = model.getGCEvents(); i.hasNext();) {
            GCEvent event = i.next();
            polygon.addPoint(event.getTimestamp() - model.getFirstPauseTimeStamp(), event.getPause());
           
        }
        // dummy point to make the polygon complete
        polygon.addPoint(model.getRunningTime(), 0.0d);
        return polygon;
    }
}
