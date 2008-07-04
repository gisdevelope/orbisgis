/*
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able
 * to manipulate and create vector and raster spatial information. OrbisGIS
 * is distributed under GPL 3 license. It is produced  by the geo-informatic team of
 * the IRSTV Institute <http://www.irstv.cnrs.fr/>, CNRS FR 2488:
 *    Erwan BOCHER, scientific researcher,
 *    Thomas LEDUC, scientific researcher,
 *    Fernando GONZALEZ CORTES, computer engineer.
 *
 * Copyright (C) 2007 Erwan BOCHER, Fernando GONZALEZ CORTES, Thomas LEDUC
 *
 * This file is part of OrbisGIS.
 *
 * OrbisGIS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OrbisGIS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OrbisGIS. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult:
 *    <http://orbisgis.cerma.archi.fr/>
 *    <http://sourcesup.cru.fr/projects/orbisgis/>
 *
 * or contact directly:
 *    erwan.bocher _at_ ec-nantes.fr
 *    fergonco _at_ gmail.com
 *    thomas.leduc _at_ cerma.archi.fr
 */
/*
 * JPanelUniqueSymbolLegend.java
 *
 * Created on 27 de febrero de 2008, 18:20
 */

package org.orbisgis.editorViews.toc.actions.cui.gui;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;

import org.gdms.data.types.Type;
import org.gdms.driver.DriverException;
import org.orbisgis.editorViews.toc.actions.cui.gui.widgets.LegendListDecorator;
import org.orbisgis.layerModel.ILayer;
import org.orbisgis.renderer.legend.Legend;
import org.orbisgis.renderer.legend.carto.LabelLegend;
import org.orbisgis.renderer.legend.carto.LegendFactory;

/**
 *
 * @author david
 */
public class JPanelLabelLegend extends javax.swing.JPanel implements
		ILegendPanelUI {

	private int constraint = 0;
	private ILayer layer = null;
	private LabelLegend leg = null;

	private LegendListDecorator dec = null;

	/** Creates new form JPanelUniqueSymbolLegend */
	public JPanelLabelLegend(Legend leg, int constraint, ILayer layer) {
		this.constraint = constraint;
		this.layer = layer;
		this.leg = (LabelLegend) leg;
		initComponents();
		initCombo1();
		initCombo2();
	}

	/**
	 * initializes the second combobox (sizes)
	 */
	private void initCombo2() {
		ArrayList<String> comboValuesArray = new ArrayList<String>();
		try {
			int numFields = layer.getDataSource().getFieldCount();
			for (int i = 0; i < numFields; i++) {
				int fieldType = layer.getDataSource().getFieldType(i)
						.getTypeCode();
				if (fieldType == Type.BYTE || fieldType == Type.SHORT
						|| fieldType == Type.INT || fieldType == Type.LONG
						|| fieldType == Type.FLOAT || fieldType == Type.DOUBLE) {
					comboValuesArray.add(layer.getDataSource().getFieldName(i));
				}
			}
		} catch (DriverException e) {
			System.out.println("Driver Exception: " + e.getMessage());
		}

		String[] comboValues = new String[comboValuesArray.size()];

		comboValues = comboValuesArray.toArray(comboValues);

		DefaultComboBoxModel model = (DefaultComboBoxModel) jComboBoxFromField
				.getModel();

		for (int i = 0; i < comboValues.length; i++) {
			model.addElement(comboValues[i]);
		}

		String clasf = leg.getLabelSizeField();
		if (clasf != null) {
			jComboBoxFromField.setSelectedItem(clasf);
			// SIZE
			jRadioButtonSet.setSelected(false);
			jRadioButtonInPixels.setSelected(true);
			jComboBoxFromField.setEnabled(true);
			jSpinner1.setEnabled(false);
		} else {
			jComboBoxFromField.setSelectedIndex(-1);
		}

		jSpinner1.setValue(leg.getFontSize());

	}

	/**
	 * initializes the first combobox (choice of field).
	 */
	private void initCombo1() {

		ArrayList<String> comboValuesArray = new ArrayList<String>();
		try {
			int numFields = layer.getDataSource().getFieldCount();
			for (int i = 0; i < numFields; i++) {
				int fieldType = layer.getDataSource().getFieldType(i)
						.getTypeCode();
				if (fieldType == Type.STRING) {
					comboValuesArray.add(layer.getDataSource().getFieldName(i));
				}
			}
		} catch (DriverException e) {
			System.out.println("Driver Exception: " + e.getMessage());
		}

		String[] comboValues = new String[comboValuesArray.size()];

		comboValues = comboValuesArray.toArray(comboValues);

		DefaultComboBoxModel model = (DefaultComboBoxModel) jComboBoxLabelField
				.getModel();

		for (int i = 0; i < comboValues.length; i++) {
			model.addElement(comboValues[i]);
		}

		String field = leg.getClassificationField();
		jComboBoxLabelField.setSelectedItem(field);

		// SIZE
		jRadioButtonSet.setSelected(true);
		jComboBoxFromField.setEnabled(false);

	}

	public JPanelLabelLegend(int constraint, ILayer layer) {
		this(LegendFactory.createLabelLegend(), constraint, layer);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// <editor-fold defaultstate="collapsed" desc="Generated
	// <editor-fold defaultstate="collapsed" desc="Generated
	// <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanelFirstLine = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBoxLabelField = new javax.swing.JComboBox();
        jPanelSecondLine = new javax.swing.JPanel();
        jRadioButtonSet = new javax.swing.JRadioButton();
        jSpinner1 = new javax.swing.JSpinner();
        jPanelThirdLine = new javax.swing.JPanel();
        jRadioButtonInPixels = new javax.swing.JRadioButton();
        jComboBoxFromField = new javax.swing.JComboBox();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.PAGE_AXIS));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Label field:");
        jPanelFirstLine.add(jLabel1);

        jComboBoxLabelField.setPreferredSize(new java.awt.Dimension(225, 19));
        jComboBoxLabelField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxLabelFieldActionPerformed(evt);
            }
        });
        jPanelFirstLine.add(jComboBoxLabelField);

        add(jPanelFirstLine);

        buttonGroup1.add(jRadioButtonSet);
        jRadioButtonSet.setText("Set size:");
        jRadioButtonSet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonSetActionPerformed(evt);
            }
        });
        jPanelSecondLine.add(jRadioButtonSet);

        jSpinner1.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanelSecondLine.add(jSpinner1);

        add(jPanelSecondLine);

        buttonGroup1.add(jRadioButtonInPixels);
        jRadioButtonInPixels.setText("From field:");
        jRadioButtonInPixels.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonInPixelsActionPerformed(evt);
            }
        });
        jPanelThirdLine.add(jRadioButtonInPixels);

        jComboBoxFromField.setPreferredSize(new java.awt.Dimension(80, 23));
        jComboBoxFromField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxFromFieldActionPerformed(evt);
            }
        });
        jPanelThirdLine.add(jComboBoxFromField);

        add(jPanelThirdLine);
    }// </editor-fold>//GEN-END:initComponents

	private void jComboBoxFromFieldActionPerformed(
			java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jComboBoxFromFieldActionPerformed
		if (dec != null)
			dec.setLegend(getLegend());
	}// GEN-LAST:event_jComboBoxFromFieldActionPerformed

	private void jRadioButtonSetActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jRadioButtonSetActionPerformed
		jComboBoxFromField.setEnabled(false);
		jSpinner1.setEnabled(true);
		if (dec != null)
			dec.setLegend(getLegend());
	}// GEN-LAST:event_jRadioButtonSetActionPerformed

	private void jRadioButtonInPixelsActionPerformed(
			java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jRadioButtonInPixelsActionPerformed
		jComboBoxFromField.setEnabled(true);
		jSpinner1.setEnabled(false);
		if (dec != null)
			dec.setLegend(getLegend());
	}// GEN-LAST:event_jRadioButtonInPixelsActionPerformed

	private void jComboBoxLabelFieldActionPerformed(
			java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jComboBoxLabelFieldActionPerformed
		if (dec != null)
			dec.setLegend(getLegend());
	}// GEN-LAST:event_jComboBoxLabelFieldActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox jComboBoxFromField;
    private javax.swing.JComboBox jComboBoxLabelField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanelFirstLine;
    private javax.swing.JPanel jPanelSecondLine;
    private javax.swing.JPanel jPanelThirdLine;
    private javax.swing.JRadioButton jRadioButtonInPixels;
    private javax.swing.JRadioButton jRadioButtonSet;
    private javax.swing.JSpinner jSpinner1;
    // End of variables declaration//GEN-END:variables
	// public String toString() {
	// // return "Unique symbol";
	// return identity;
	// }

	public Component getComponent() {
		return this;
	}


	public void setDecoratorListener(LegendListDecorator dec) {
		this.dec = dec;
	}

	public Legend getLegend() {
		LabelLegend leg = (LabelLegend) LegendFactory.createLabelLegend();

		try {
			leg.setClassificationField((String) jComboBoxLabelField
					.getSelectedItem());
		} catch (DriverException e) {
			System.out.println("Exception: " + e.getMessage());
		}

		if (jRadioButtonSet.isSelected()) {
			leg.setFontSize((Integer) jSpinner1.getValue());
		} else {
			try {
				leg.setLabelSizeField((String) jComboBoxFromField
						.getSelectedItem());
			} catch (DriverException e) {
				System.out.println("Exception: " + e.getMessage());
			}
		}

		return leg;
	}

	public boolean acceptsGeometryType(int geometryType) {
		// TODO Auto-generated method stub
		return false;
	}

	public String getLegendTypeName() {
		// TODO Auto-generated method stub
		return null;
	}

	public ILegendPanelUI newInstance(LegendContext legendContext) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setLegend(Legend legend) {
		// TODO Auto-generated method stub

	}

	public void setLegendContext(LegendContext lc) {
		// TODO Auto-generated method stub

	}


}