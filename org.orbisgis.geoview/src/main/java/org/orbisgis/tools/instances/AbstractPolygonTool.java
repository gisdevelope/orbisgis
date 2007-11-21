package org.orbisgis.tools.instances;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.net.URL;
import java.util.ArrayList;

import org.orbisgis.tools.DrawingException;
import org.orbisgis.tools.FinishedAutomatonException;
import org.orbisgis.tools.ToolManager;
import org.orbisgis.tools.TransitionException;
import org.orbisgis.tools.ViewContext;
import org.orbisgis.tools.instances.generated.Polygon;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;

public abstract class AbstractPolygonTool extends Polygon {

	private GeometryFactory gf = new GeometryFactory();

	private ArrayList<Coordinate> points = new ArrayList<Coordinate>();

	@Override
	public void transitionTo_Standby(ViewContext vc, ToolManager tm)
			throws FinishedAutomatonException, TransitionException {
		points.clear();
	}

	@Override
	public void transitionTo_Point(ViewContext vc, ToolManager tm)
			throws FinishedAutomatonException, TransitionException {
		points.add(new Coordinate(tm.getValues()[0], tm.getValues()[1]));
	}

	@SuppressWarnings("unchecked")//$NON-NLS-1$
	@Override
	public void transitionTo_Done(ViewContext vc, ToolManager tm)
			throws FinishedAutomatonException, TransitionException {
		if (points.size() < 3)
			throw new TransitionException(Messages
					.getString("MultipolygonTool.0")); //$NON-NLS-1$
		ArrayList<Coordinate> tempPoints = (ArrayList<Coordinate>) points
				.clone();
		tempPoints.add(new Coordinate(points.get(0).x, points.get(0).y));
		com.vividsolutions.jts.geom.Polygon pol = gf.createPolygon(gf
				.createLinearRing(tempPoints.toArray(new Coordinate[0])),
				new LinearRing[0]);

		if (!pol.isValid()) {
			throw new TransitionException(Messages.getString("PolygonTool.1")); //$NON-NLS-1$
		}
		polygonDone(pol, vc, tm);

		transition("init"); //$NON-NLS-1$
	}

	protected abstract void polygonDone(com.vividsolutions.jts.geom.Polygon g,
			ViewContext vc, ToolManager tm) throws TransitionException;

	@Override
	public void transitionTo_Cancel(ViewContext vc, ToolManager tm)
			throws FinishedAutomatonException, TransitionException {
	}

	@Override
	public void drawIn_Standby(Graphics g, ViewContext vc, ToolManager tm)
			throws DrawingException {
	}

	@SuppressWarnings("unchecked")//$NON-NLS-1$
	@Override
	public void drawIn_Point(Graphics g, ViewContext vc, ToolManager tm)
			throws DrawingException {
		if (points.size() >= 2) {
			ArrayList<Coordinate> tempPoints = (ArrayList<Coordinate>) points
					.clone();
			Point2D current = tm.getLastRealMousePosition();
			tempPoints.add(new Coordinate(current.getX(), current.getY()));
			tempPoints.add(new Coordinate(tempPoints.get(0).x, tempPoints
					.get(0).y));
			com.vividsolutions.jts.geom.Polygon pol = gf.createPolygon(gf
					.createLinearRing(tempPoints.toArray(new Coordinate[0])),
					new LinearRing[0]);

			tm.addGeomToDraw(pol);

			if (!pol.isValid()) {
				throw new DrawingException(Messages.getString("PolygonTool.1")); //$NON-NLS-1$
			}

		}
	}

	@Override
	public void drawIn_Done(Graphics g, ViewContext vc, ToolManager tm)
			throws DrawingException {
	}

	@Override
	public void drawIn_Cancel(Graphics g, ViewContext vc, ToolManager tm)
			throws DrawingException {
	}

	public URL getMouseCursor() {
		return null;
	}

}
