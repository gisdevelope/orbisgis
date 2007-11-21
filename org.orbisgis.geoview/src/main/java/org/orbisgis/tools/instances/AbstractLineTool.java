package org.orbisgis.tools.instances;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.orbisgis.tools.DrawingException;
import org.orbisgis.tools.FinishedAutomatonException;
import org.orbisgis.tools.ToolManager;
import org.orbisgis.tools.TransitionException;
import org.orbisgis.tools.ViewContext;
import org.orbisgis.tools.instances.generated.Line;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

public abstract class AbstractLineTool extends Line {

	protected ArrayList<Coordinate> points = new ArrayList<Coordinate>();

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

	@Override
	public void transitionTo_Cancel(ViewContext vc, ToolManager tm)
			throws FinishedAutomatonException, TransitionException {

	}

	@Override
	public void drawIn_Standby(Graphics g, ViewContext vc, ToolManager tm)
			throws DrawingException {

	}

	@Override
	public void transitionTo_Done(ViewContext vc, ToolManager tm)
			throws FinishedAutomatonException, TransitionException {
		if (points.size() < 2)
			throw new TransitionException(Messages.getString("MultilineTool.0")); //$NON-NLS-1$
		LineString ls = new GeometryFactory().createLineString(points
				.toArray(new Coordinate[0]));
		com.vividsolutions.jts.geom.Geometry g = ls;
		if (!g.isValid()) {
			throw new TransitionException(Messages.getString("LineTool.0")); //$NON-NLS-1$
		}
		lineDone(ls, vc, tm);

		transition("init"); //$NON-NLS-1$
	}

	protected abstract void lineDone(LineString ls, ViewContext vc,
			ToolManager tm) throws TransitionException;

	@SuppressWarnings("unchecked")//$NON-NLS-1$
	@Override
	public void drawIn_Point(Graphics g, ViewContext vc, ToolManager tm)
			throws DrawingException {
		Point2D current = tm.getLastRealMousePosition();

		ArrayList<Coordinate> tempPoints = (ArrayList<Coordinate>) points
				.clone();
		tempPoints.add(new Coordinate(current.getX(), current.getY()));
		LineString ls = new GeometryFactory().createLineString(tempPoints
				.toArray(new Coordinate[0]));

		tm.addGeomToDraw(ls);

		if (!ls.isValid()) {
			throw new DrawingException(Messages.getString("LineTool.0")); //$NON-NLS-1$
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
}
