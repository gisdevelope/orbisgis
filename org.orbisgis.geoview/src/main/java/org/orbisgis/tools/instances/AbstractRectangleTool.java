package org.orbisgis.tools.instances;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.net.URL;

import org.orbisgis.tools.FinishedAutomatonException;
import org.orbisgis.tools.ToolManager;
import org.orbisgis.tools.TransitionException;
import org.orbisgis.tools.ViewContext;
import org.orbisgis.tools.instances.generated.ZoomIn;

public abstract class AbstractRectangleTool extends ZoomIn {

	protected double[] firstPoint;

	/**
	 * @see org.estouro.tools.generated.ZoomIn#transitionTo_RectangleDone()
	 */
	@Override
	public void transitionTo_RectangleDone(ViewContext vc, ToolManager tm)
			throws TransitionException, FinishedAutomatonException {
		double[] v = tm.getValues();

		double minx = Math.min(firstPoint[0], v[0]);
		double miny = Math.min(firstPoint[1], v[1]);

		Rectangle2D rect = new Rectangle2D.Double(minx, miny, Math.abs(v[0]
				- firstPoint[0]), Math.abs(v[1] - firstPoint[1]));

		rectangleDone(rect, vc, tm);

		transition("init"); //$NON-NLS-1$
	}

	protected abstract void rectangleDone(Rectangle2D rect, ViewContext vc,
			ToolManager tm) throws TransitionException;

	/**
	 * @see org.estouro.tools.generated.Rectangle#transitionTo_Standby()
	 */
	@Override
	public void transitionTo_Standby(ViewContext vc, ToolManager tm)
			throws TransitionException {
	}

	/**
	 * @see org.estouro.tools.generated.ZoomIn#transitionTo_OnePointLeft()
	 */
	@Override
	public void transitionTo_OnePointLeft(ViewContext vc, ToolManager tm)
			throws TransitionException {
		firstPoint = tm.getValues();
	}

	/**
	 * @see org.estouro.tools.generated.ZoomIn#transitionTo_Cancel()
	 */
	@Override
	public void transitionTo_Cancel(ViewContext vc, ToolManager tm)
			throws TransitionException {
	}

	/**
	 * @see org.estouro.tools.generated.ZoomIn#drawIn_Standby(java.awt.Graphics)
	 */
	@Override
	public void drawIn_Standby(Graphics g, ViewContext vc, ToolManager tm) {
	}

	/**
	 * @see org.estouro.tools.generated.ZoomIn#drawIn_OnePointLeft(java.awt.Graphics)
	 */
	@Override
	public void drawIn_OnePointLeft(Graphics g, ViewContext vc, ToolManager tm) {
		Point p = vc.fromMapPoint(new Point2D.Double(firstPoint[0],
				firstPoint[1]));
		int minx = Math.min(p.x, tm.getLastMouseX());
		int miny = Math.min(p.y, tm.getLastMouseY());
		int width = Math.abs(p.x - tm.getLastMouseX());
		int height = Math.abs(p.y - tm.getLastMouseY());
		g.drawRect(minx, miny, width, height);
	}

	/**
	 * @see org.estouro.tools.generated.ZoomIn#drawIn_RectangleDone(java.awt.Graphics)
	 */
	@Override
	public void drawIn_RectangleDone(Graphics g, ViewContext vc, ToolManager tm) {
	}

	/**
	 * @see org.estouro.tools.generated.ZoomIn#drawIn_Cancel(java.awt.Graphics)
	 */
	@Override
	public void drawIn_Cancel(Graphics g, ViewContext vc, ToolManager tm) {
	}

	@Override
	public Point getHotSpotOffset() {
		return new Point(0, 0);
	}

	@Override
	public URL getMouseCursorURL() {
		return null;
	}

}
