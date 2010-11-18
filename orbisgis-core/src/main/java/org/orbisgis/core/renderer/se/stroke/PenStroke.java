package org.orbisgis.core.renderer.se.stroke;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBElement;
import org.orbisgis.core.renderer.persistance.se.PenStrokeType;

import org.gdms.data.feature.Feature;
import org.orbisgis.core.map.MapTransform;
import org.orbisgis.core.renderer.persistance.se.ObjectFactory;
import org.orbisgis.core.renderer.persistance.se.ParameterValueType;
import org.orbisgis.core.renderer.se.common.ShapeHelper;
import org.orbisgis.core.renderer.se.common.Uom;
import org.orbisgis.core.renderer.se.fill.GraphicFill;
import org.orbisgis.core.renderer.se.parameter.ParameterException;
import org.orbisgis.core.renderer.se.parameter.SeParameterFactory;
import org.orbisgis.core.renderer.se.parameter.color.ColorHelper;
import org.orbisgis.core.renderer.se.parameter.color.ColorLiteral;
import org.orbisgis.core.renderer.se.parameter.color.ColorParameter;
import org.orbisgis.core.renderer.se.parameter.real.RealLiteral;
import org.orbisgis.core.renderer.se.parameter.real.RealParameter;
import org.orbisgis.core.renderer.se.parameter.real.RealParameterContext;
import org.orbisgis.core.renderer.se.parameter.string.StringLiteral;
import org.orbisgis.core.renderer.se.parameter.string.StringParameter;

/**
 * Basic stroke for linear features
 * @todo implement dasharray/dashoffset
 * @author maxence
 */
public final class PenStroke extends Stroke {

    private ColorParameter color;
    private GraphicFill stipple;
    private boolean useColor;
    private RealParameter opacity;
    private RealParameter width;

    private LineJoin lineJoin;
    private LineCap lineCap;

	private StringParameter dashArray;
    private RealParameter dashOffset;


	//private BasicStroke bStroke;


    public enum LineCap {

        BUTT, ROUND, SQUARE;

        public ParameterValueType getParameterValueType() {
            return SeParameterFactory.createParameterValueType(this.name().toLowerCase());
        }
    }

    public enum LineJoin {

        MITRE, ROUND, BEVEL;

        public ParameterValueType getParameterValueType() {
            return SeParameterFactory.createParameterValueType(this.name().toLowerCase());
        }
    }

    /**
     * Create a standard undashed 0.1mm-wide opaque black stroke
     */
    public PenStroke() {
        setColor(new ColorLiteral(Color.BLACK));
        setWidth(new RealLiteral(0.1));
        setOpacity(new RealLiteral(100.0));

        setUom(null);
		setStipple(null);

		setDashArray(null);
		setDashOffset(null);

		setLineCap(LineCap.ROUND);
		setLineJoin(LineJoin.BEVEL);

        //updateBasicStroke();
    }

	/**
	 * @todo line cap line join !
	 * @param t
	 */
    public PenStroke(PenStrokeType t) {
        this();

        if (t.getColor() != null) {
            this.setColor(SeParameterFactory.createColorParameter(t.getColor()));
        } else if (t.getStipple() != null) {
            this.setStipple(new GraphicFill(t.getStipple()));
        }

        if (t.getDashArray() != null) {
			this.setDashArray(SeParameterFactory.createStringParameter(t.getDashArray()));
		}

        if (t.getDashOffset() != null) {
            this.setDashOffset(SeParameterFactory.createRealParameter(t.getDashOffset()));
        }

        if (t.getWidth() != null) {
            this.setWidth(SeParameterFactory.createRealParameter(t.getWidth()));
        }

        if (t.getLineCap() != null) {
			try {
				StringParameter lCap = SeParameterFactory.createStringParameter(t.getLineCap());
				this.setLineCap(LineCap.valueOf(lCap.getValue(null).toUpperCase()));
			} catch (Exception ex) {
				Logger.getLogger(PenStroke.class.getName()).log(Level.SEVERE, "Could not convert line cap", ex);
			}
        }

        if (t.getLineJoin() != null) {
			try {
				StringParameter lJoin = SeParameterFactory.createStringParameter(t.getLineJoin());
				this.setLineJoin(LineJoin.valueOf(lJoin.getValue(null).toUpperCase()));
			} catch (Exception ex) {
				Logger.getLogger(PenStroke.class.getName()).log(Level.SEVERE, "Could not convert line join", ex);
			}
        }

        if (t.getOpacity() != null) {
            this.setOpacity(SeParameterFactory.createRealParameter(t.getOpacity()));
        }

        if (t.getUnitOfMeasure() != null) {
            this.setUom(Uom.fromOgcURN(t.getUnitOfMeasure()));
        }
		else{
			this.uom = null;
		}

        //this.updateBasicStroke();
    }

    public PenStroke(JAXBElement<PenStrokeType> s) {
        this(s.getValue());
    }


    @Override
    public boolean dependsOnFeature() {
        if (useColor) {
            if (color != null && color.dependsOnFeature()) {
                return true;
            }
        } else {
            if (stipple != null && stipple.dependsOnFeature()) {
                return true;
            }
        }

        return (this.dashOffset != null && dashOffset.dependsOnFeature())
                || (this.opacity != null && opacity.dependsOnFeature())
                || (this.width != null && width.dependsOnFeature());
    }



    /**
     * default painter is either a solid color or a stipple
     * @return true when default is color, false when it's the stipple
     */
    public boolean useColor() {
        return useColor;
    }

    /**
     * Indicates which painter to use
     * If default painter is undefined, a random solid color is used
     * @param useColor true => use color; false => use stipple
     */
    public void setUseColor(boolean useColor) {
        this.useColor = useColor;
        //updateBasicStroke();
    }

    public void setColor(ColorParameter color) {
        this.color = color;
        useColor = true;
        //updateBasicStroke();
    }

    public ColorParameter getColor() {
        return color;
    }

    public void setStipple(GraphicFill stipple) {
        this.stipple = stipple;
		if (stipple != null){
    	    stipple.setParent(this);
	        useColor = false;
        	//updateBasicStroke();
		}
    }

    public GraphicFill getStipple() {
        return stipple;
    }

    public void setLineCap(LineCap cap) {
        lineCap = cap;
        //updateBasicStroke();
    }

    public LineCap getLineCap() {
        return lineCap;
    }

    public void setLineJoin(LineJoin join) {
        lineJoin = join;
        //updateBasicStroke();
    }

    public LineJoin getLineJoin() {
        return lineJoin;
    }

    public void setOpacity(RealParameter opacity) {
        this.opacity = opacity;

		if (opacity != null) {
			this.opacity.setContext(RealParameterContext.percentageContext);
		}
        //updateBasicStroke();
    }

    public RealParameter getOpacity() {
        return this.opacity;
    }

    public void setWidth(RealParameter width) {
        this.width = width;

		if (width != null){
			width.setContext(RealParameterContext.nonNegativeContext);
		}
        //updateBasicStroke();
    }

    public RealParameter getWidth() {
        return this.width;
    }

    public RealParameter getDashOffset() {
        return dashOffset;
    }

    public void setDashOffset(RealParameter dashOffset) {
        this.dashOffset = dashOffset;
		if (dashOffset != null){
			dashOffset.setContext(RealParameterContext.realContext);
		}
        //updateBasicStroke();
    }

	public StringParameter getDashArray() {
		return dashArray;
	}

	public void setDashArray(StringParameter dashArray) {
		this.dashArray = dashArray;
	}

	/*
    private void updateBasicStroke() {
        try {
            bStroke = createBasicStroke(null, null);
        } catch (Exception e) {
			// thrown if the stroke depends on the feature
            this.bStroke = null;
        }
    }*/

    private BasicStroke createBasicStroke(Feature feat, MapTransform mt, Double v100p) throws ParameterException {


		Double scale = null;
		Double dpi = null;

		if (mt != null){
			scale = mt.getScaleDenominator();
			dpi = mt.getDpi();
		}

        int cap;
        if (this.lineCap == null) {
            cap = BasicStroke.CAP_BUTT;
        } else {
            switch (this.lineCap) {
                case BUTT:
                default:
                    cap = BasicStroke.CAP_BUTT;
                    break;
                case ROUND:
                    cap = BasicStroke.CAP_ROUND;
                    break;
                case SQUARE:
                    cap = BasicStroke.CAP_SQUARE;
                    break;
            }
        }

        int join;
        if (this.lineJoin == null) {
            join = BasicStroke.JOIN_ROUND;
        } else {
            switch (this.lineJoin) {
                case MITRE:
                    join = BasicStroke.JOIN_MITER;
                    break;
                case ROUND:
                default:
                    join = BasicStroke.JOIN_ROUND;
                    break;
                case BEVEL:
                    join = BasicStroke.JOIN_BEVEL;
                    break;
            }
        }

        double w = 1.0;

        if (width != null) {
            w = width.getValue(feat);
            // TODO add scale and dpi
            w = Uom.toPixel(w, getUom(), mt.getDpi(), mt.getScaleDenominator(), null); // 100% based on view box height or width ?
        }


		if (this.dashArray != null && ! this.dashArray.getValue(feat).isEmpty()){

			float dashO = 0.0f;
			float[] dashA;

			String sDash = this.dashArray.getValue(feat);
			String[] splitedDash = sDash.split(" ");

			dashA = new float[splitedDash.length];
			for (int i = 0;i<splitedDash.length;i++){
            	dashA[i] = (float) Uom.toPixel(Double.parseDouble(splitedDash[i]), getUom(), mt.getDpi(), mt.getScaleDenominator(), v100p);
			}

			if (this.dashOffset != null){
            	dashO = (float) Uom.toPixel(this.dashOffset.getValue(feat), getUom(), mt.getDpi(), mt.getScaleDenominator(), v100p);
			}
        	return new BasicStroke((float) w, cap, join, 10.0f, dashA, dashO);
		}
		else{
			return new BasicStroke((float) w, cap, join);
		}
    }

    public BasicStroke getBasicStroke(Feature feat, MapTransform mt, Double v100p) throws ParameterException {
        //if (bStroke != null) {
        //    return bStroke;
        //} else {
    		return this.createBasicStroke(feat, mt, v100p);
        //}

    }

    @Override
    public void draw(Graphics2D g2, Shape shp, Feature feat, boolean selected, MapTransform mt) throws ParameterException, IOException {

        Paint paint = null;
        // remove preGap, postGap from the line
        Shape shape = this.getPreparedShape(shp);

        BasicStroke stroke = null;

        //if (this.bStroke == null) {
        stroke = this.createBasicStroke(feat, mt, ShapeHelper.getAreaPerimeterLength(shp));
        //} else {
        //    stroke = this.bStroke;
        //}

        g2.setStroke(stroke);

        if (this.useColor == false) {
            if (stipple != null) {
                paint = stipple.getStipplePainter(feat, selected, mt);
            } else {
                // TOOD Warn Stiple has to be used, but is undefined
            }
        } else {
            Color c;

            if (this.color != null) {
                c = color.getColor(feat);
            } else {
                c = Color.BLACK;
            }

			if (selected) {
                c = ColorHelper.invert(c);
            }


            Color ac = c;
            if (this.opacity != null) {
                paint = ColorHelper.getColorWithAlpha(c, this.opacity.getValue(feat));
            }

        }

        if (paint != null) {
            g2.setPaint(paint);
            g2.draw(shape);
        }
    }

    @Override
    public double getMaxWidth(Feature feat, MapTransform mt) throws ParameterException {
        if (this.width != null) {
            return Uom.toPixel(width.getValue(feat), this.getUom(), mt.getDpi(), mt.getScaleDenominator(), null);
        } else {
            return 0.0;
        }
    }

    @Override
    public JAXBElement<PenStrokeType> getJAXBElement() {
        ObjectFactory of = new ObjectFactory();
        return of.createPenStroke(this.getJAXBType());
    }

    public PenStrokeType getJAXBType() {
        PenStrokeType s = new PenStrokeType();

        this.setJAXBProperties(s);

        if (useColor) {
            if (color != null) {
                s.setColor(color.getJAXBParameterValueType());
            }
        } else if (stipple != null) {
            s.setStipple(stipple.getJAXBType());
        }

        if (this.uom != null) {
            s.setUnitOfMeasure(this.uom.toURN());
        }

        if (this.dashArray != null) {
            //s.setDashArray(null);
			s.setDashArray(dashArray.getJAXBParameterValueType());
        }

        if (this.dashOffset != null) {
            s.setDashOffset(this.dashOffset.getJAXBParameterValueType());
        }

        if (this.lineCap != null) {
            s.setLineCap(this.lineCap.getParameterValueType());
        }

        if (this.lineJoin != null) {
            s.setLineJoin(this.lineJoin.getParameterValueType());
        }

        if (this.opacity != null) {
			try {
				if (this.opacity.getValue(null) != 100.0) {
					s.setOpacity(this.opacity.getJAXBParameterValueType());
				}
			} catch (ParameterException ex) {
				s.setOpacity(this.opacity.getJAXBParameterValueType());
			}
        }

        if (this.preGap != null) {
            s.setPreGap(this.preGap.getJAXBParameterValueType());
        }

        if (this.postGap != null) {
            s.setPostGap(this.postGap.getJAXBParameterValueType());
        }

        if (this.width != null) {
            s.setWidth(this.width.getJAXBParameterValueType());
        }

        return s;
    }

}