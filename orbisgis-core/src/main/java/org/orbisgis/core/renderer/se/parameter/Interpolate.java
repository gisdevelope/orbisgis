package org.orbisgis.core.renderer.se.parameter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.JAXBElement;
import net.opengis.fes._2.ExpressionType;
import net.opengis.se._2_0.core.InterpolateType;
import net.opengis.se._2_0.core.InterpolationPointType;
import net.opengis.se._2_0.core.ModeType;
import net.opengis.se._2_0.core.ObjectFactory;
import net.opengis.se._2_0.core.ParameterValueType;

import org.orbisgis.core.renderer.se.parameter.real.RealParameter;
import org.orbisgis.core.renderer.se.parameter.real.RealParameterContext;

/**
 *
 * @author maxence
 * @param <ToType> One of RealParameter or ColorParameter
 * @param <FallbackType> extends ToType (the LirealOne, please)...
 * @todo find a nice way to compute interpolation for RealParameter and ColorParameter
 *
 */
public abstract class Interpolate<ToType extends SeParameter, FallbackType extends ToType> implements SeParameter {

	private  InterpolationMode mode;
	private  RealParameter lookupValue;
	private  FallbackType fallbackValue;
	private  List<InterpolationPoint<ToType>> iPoints;

	public enum InterpolationMode {
		LINEAR, COSINE, CUBIC
	}

    protected Interpolate(){
		this.iPoints = new ArrayList<InterpolationPoint<ToType>>();
	}

	public Interpolate(FallbackType fallbackValue) {
		this.fallbackValue = fallbackValue;
		this.iPoints = new ArrayList<InterpolationPoint<ToType>>();
	}

	@Override
	public final String dependsOnFeature() {
        String result = "";
        String lookup = this.getLookupValue().dependsOnFeature();
        if (lookup != null && !lookup.isEmpty()){
            result = lookup;
        }

		int i;
		for (i = 0; i < this.getNumInterpolationPoint(); i++) {
            String r = this.getInterpolationPoint(i).getValue().dependsOnFeature();
            if (r!= null && !r.isEmpty()){
                result += r;
			}
		}

		return result;
	}

    public InterpolationMode getMode() {
        return mode;
    }

    public void setMode(InterpolationMode mode) {
        this.mode = mode;
    }

    protected  List<InterpolationPoint<ToType>> getInterpolationPoints() {
        return iPoints;
    }


	public void setFallbackValue(FallbackType fallbackValue) {
		this.fallbackValue = fallbackValue;
	}

	public FallbackType getFallbackValue() {
		return fallbackValue;
	}

	public void setLookupValue(RealParameter lookupValue) {
		this.lookupValue = lookupValue;
		if (this.lookupValue != null){
			this.lookupValue.setContext(RealParameterContext.realContext);
		}
	}

	public RealParameter getLookupValue() {
		return lookupValue;
	}

	/**
	 * Return the number of classes defined within the classification. According to this number (n),
	 *  available class value ID are [0;n] and ID for threshold are [0;n-1
	 *
	 *  @return number of defined class
	 */
	public int getNumInterpolationPoint() {
		return iPoints.size();
	}

	/**
	 * Add a new interpolation point.
	 * The new point is inserted at the right place in the interpolation point list, according to its data
	 *
	 */
	public void addInterpolationPoint(InterpolationPoint<ToType> point) {
		iPoints.add(point);
		sortInterpolationPoint();
	}

	public InterpolationPoint<ToType> getInterpolationPoint(int i) {
		return iPoints.get(i);
	}

	public void setInterpolationMode(InterpolationMode mode) {
		this.mode = mode;
	}

	public InterpolationMode getInterpolationMode() {
		return mode;
	}

	private void sortInterpolationPoint() {
		Collections.sort(iPoints);
	}

	@Override
	public ParameterValueType getJAXBParameterValueType() {
		ParameterValueType p = new ParameterValueType();
		p.getContent().add(this.getJAXBExpressionType());
		return p;
	}

	@Override
	public JAXBElement<? extends ExpressionType> getJAXBExpressionType() {
		InterpolateType i = new InterpolateType();

		if (fallbackValue != null) {
			i.setFallbackValue(fallbackValue.toString());
		}
		if (lookupValue != null) {
			i.setLookupValue(lookupValue.getJAXBParameterValueType());
		}

		if (mode != null) {
			i.setMode(ModeType.fromValue(mode.toString()));
		}

		List<InterpolationPointType> ips = i.getInterpolationPoint();


		for (InterpolationPoint<ToType> ip : iPoints) {
			InterpolationPointType ipt = new InterpolationPointType();

			ipt.setValue(ip.getValue().getJAXBParameterValueType());
			ipt.setData(ip.getData());
			ips.add(ipt);
		}

		ObjectFactory of = new ObjectFactory();
		return of.createInterpolate(i);
	}

	protected int getFirstIP(double data) {
		int i = -1;
		for (InterpolationPoint ip : iPoints) {
			if (ip.getData() > data) {
				return i;
			}
			i++;
		}
		return -1;
	}


	protected double cubicInterpolation(double d1, double d2, double x,
			double v1, double v2, double v3, double v4){
		//double mu = (x - d1) / (d2 - d1);

		return 0.0;
	}

	protected double cosineInterpolation(double d1, double d2, double x, double v1, double v2) {
		double mu = (x - d1) / (d2 - d1);
		double mu2 = (1 - Math.cos(mu * Math.PI)) * 0.5;
		return v1 + mu2*(v2-v1);
	}

	protected double linearInterpolation(double d1, double d2, double x, double v1, double v2) {
		return v1 + (v2 - v1) * (x - d1) / (d2 - d1);
	}
}
