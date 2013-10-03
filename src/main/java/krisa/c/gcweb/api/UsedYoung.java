package krisa.c.gcweb.api;

import java.util.Iterator;

import com.tagtraum.perf.gcviewer.model.GCEvent;
import com.tagtraum.perf.gcviewer.model.GCModel;
/**
 * @author <a href="mailto:gcviewer@gmx.ch">Joerg Wuethrich</a>
 * <p>created on: 22.07.2012</p>
 * licensed under LGPL http://www.gnu.org/licenses/lgpl.txt
 * Modify by Krisa Chaijaroen
 */
public class UsedYoung {

	public DataPoint computePolygon(GCModel model) {
		DataPoint polygon = new DataPoint();
		GCEvent lastTenuredEvent = null;
		GCEvent tenuredEvent = null;
		for (Iterator<GCEvent> i = model.getGCEvents(); i.hasNext();) {
			GCEvent event = i.next();
			GCEvent youngEvent = event.getYoung();
			int lastTenuredTotal = 0;
			int tenuredTotal = 0;
			if (youngEvent != null) {
				// event contains information about generation (only with
				// -XX:+PrintGCDetails)
				// if (modelChart.isShowTenured()) {
				if (tenuredEvent != null && tenuredEvent.getTotal() > 0) {
					lastTenuredEvent = tenuredEvent;
				}
				if (lastTenuredEvent == null)
					lastTenuredEvent = event.getTenured();
				tenuredEvent = event.getTenured();

				lastTenuredTotal = lastTenuredEvent.getTotal();
				tenuredTotal = tenuredEvent.getTotal();
				// }
				// e.g. "GC remark" of G1 algorithm does not contain memory
				// information
				if (youngEvent.getTotal() > 0) {
					final double timestamp = event.getTimestamp()
							- model.getFirstPauseTimeStamp();
					polygon.addPoint(timestamp,
							lastTenuredTotal + youngEvent.getPreUsed());
					polygon.addPoint(timestamp,
							tenuredTotal + youngEvent.getPostUsed());
				}
			}
		}
		// dummy point to make the polygon complete
		polygon.addPoint(model.getRunningTime(), 0.0d);
		return polygon;

	}
}
