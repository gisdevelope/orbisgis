package org.orbisgis.renderer.legend;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;

public abstract class AbstractLineSymbol extends AbstractGeometrySymbol {

	public AbstractLineSymbol() {
		setName("Line symbol");
	}

	public boolean willDrawSimpleGeometry(Geometry geom) {
		return geom instanceof LineString || geom instanceof MultiLineString;
	}

}
