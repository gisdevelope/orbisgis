/**
 * OrbisGIS is a java GIS application dedicated to research in GIScience.
 * OrbisGIS is developed by the GIS group of the DECIDE team of the 
 * Lab-STICC CNRS laboratory, see <http://www.lab-sticc.fr/>.
 *
 * The GIS group of the DECIDE team is located at :
 *
 * Laboratoire Lab-STICC – CNRS UMR 6285
 * Equipe DECIDE
 * UNIVERSITÉ DE BRETAGNE-SUD
 * Institut Universitaire de Technologie de Vannes
 * 8, Rue Montaigne - BP 561 56017 Vannes Cedex
 * 
 * OrbisGIS is distributed under GPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2017 CNRS (Lab-STICC UMR CNRS 6285)
 *
 * This file is part of OrbisGIS.
 *
 * OrbisGIS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * OrbisGIS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * OrbisGIS. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly:
 * info_at_ orbisgis.org
 */
package org.orbisgis.view.toc.actions.cui.legend.ui;

import net.miginfocom.swing.MigLayout;
import org.orbisgis.coremap.renderer.se.fill.SolidFill;
import org.orbisgis.legend.structure.fill.constant.ConstantSolidFillLegend;
import org.orbisgis.legend.structure.fill.constant.NullSolidFillLegend;
import org.orbisgis.legend.structure.stroke.constant.ConstantPenStrokeLegend;
import org.orbisgis.legend.structure.stroke.constant.NullPenStrokeLegend;
import org.orbisgis.legend.thematic.PointParameters;
import org.orbisgis.legend.thematic.constant.UniqueSymbolPoint;
import org.orbisgis.legend.thematic.proportional.ProportionalPoint;
import org.orbisgis.sif.UIFactory;
import org.orbisgis.sif.UIPanel;
import org.orbisgis.view.toc.actions.cui.LegendContext;
import org.orbisgis.view.toc.actions.cui.SimpleGeometryType;
import org.orbisgis.view.toc.actions.cui.components.CanvasSE;
import org.orbisgis.view.toc.actions.cui.legend.panels.AreaPanel;
import org.orbisgis.view.toc.actions.cui.legend.panels.LinePanel;
import org.orbisgis.view.toc.actions.cui.legend.panels.PreviewPanel;
import org.orbisgis.view.toc.actions.cui.legend.panels.ProportionalPointPanel;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.EventHandler;
import java.net.URL;

/**
 * "Proportional Point" UI.
 *
 * @author Alexis Guéganno
 * @author Adam Gouge
 */
public final class PnlProportionalPointSE extends PnlProportional {

        private static final I18n I18N = I18nFactory.getI18n(PnlProportionalPointSE.class);

        private ProportionalPoint proportionalPoint;

        private int geometryType = SimpleGeometryType.ALL;
        private MouseListener l;

        /**
         * Builds a panel based on a new legend.
         *
         * @param lc LegendContext
         */
        public PnlProportionalPointSE(LegendContext lc) {
            this(lc, new ProportionalPoint());
        }

        /**
         * Builds a panel based on the given legend.
         *
         * @param lc     LegendContext
         * @param legend Legend
         */
        public PnlProportionalPointSE(LegendContext lc, ProportionalPoint legend) {
            super(lc);
            this.proportionalPoint = legend;
            this.geometryType = lc.getGeometryType();
            initPreview();
            buildUI();
        }

        @Override
        public ProportionalPoint getLegend() {
                return proportionalPoint;
        }

        @Override
        public void buildUI() {
                JPanel glob = new JPanel(new MigLayout("wrap 2"));

                glob.add(new ProportionalPointPanel(
                    proportionalPoint,
                    getPreview(),
                    I18N.tr(MARK_SETTINGS),
                    ds,table,
                    geometryType));

                // The preview is created only once while the other panels are
                // created twice, currently. Adds a locally stored listener to
                // avoid having it twice because of this double call of
                // buildUI.
                CanvasSE prev = getPreview();
                if(l == null || prev.getMouseListeners().length == 0){
                    l = EventHandler.create(MouseListener.class, this,
                            "onClickOnPreview", "", "mouseClicked");
                    prev.addMouseListener(l);
                }
                glob.add(new PreviewPanel(getPreview()));
                this.add(glob);
        }

        /**
         * Opens a OK - Cancel window used to edit the symbol configuration, size excepted.
         */
        public void onClickOnPreview(MouseEvent mouseEvent){
            //We create a copy of the constant part of the symbol
            PointParameters pp = new PointParameters(proportionalPoint.getPenStroke().getLineColor(),
                        proportionalPoint.getPenStroke().getLineOpacity(),
                        proportionalPoint.getPenStroke().getLineWidth(),
                        proportionalPoint.getPenStroke().getDashArray(),
                        proportionalPoint.getFillLegend().getColor(),
                        proportionalPoint.getFillLegend().getOpacity(),
                        3.0,
                        3.0,
                        proportionalPoint.getWellKnownName());
            UniqueSymbolPoint usp = new UniqueSymbolPoint(pp);
            if(proportionalPoint.getPenStroke() instanceof NullPenStrokeLegend){
                usp.setPenStroke(new NullPenStrokeLegend());
            }
            if(proportionalPoint.getFillLegend() instanceof NullSolidFillLegend){
                usp.setFillLegend(new NullSolidFillLegend());
            }
            usp.setStrokeUom(proportionalPoint.getStrokeUom());
            usp.setSymbolUom(proportionalPoint.getSymbolUom());
            ConfigPanel cp = new ConfigPanel(usp);
            if(UIFactory.showDialog(cp)){
                affectValues(usp);
                getPreview().imageChanged();
            }
        }

    /**
     * Uses the configuration found in the given {@link UniqueSymbolPoint} to configure the
     * Stroke, Fill and WKN of the attached ProportionalPoint.
     * @param usp The input {@link UniqueSymbolPoint}.
     */
    private void affectValues(UniqueSymbolPoint usp){
        boolean strokeNotNull = !(usp.getPenStroke() instanceof NullPenStrokeLegend);
        boolean fillNotNull = !(usp.getFillLegend() instanceof NullSolidFillLegend);
        if(strokeNotNull){
            if(proportionalPoint.getPenStroke() instanceof NullPenStrokeLegend){
                proportionalPoint.setPenStroke(new ConstantPenStrokeLegend());
            }
            proportionalPoint.getPenStroke().setLineColor(usp.getPenStroke().getLineColor());
            proportionalPoint.getPenStroke().setLineOpacity(usp.getPenStroke().getLineOpacity());
            proportionalPoint.getPenStroke().setLineWidth(usp.getPenStroke().getLineWidth());
            proportionalPoint.getPenStroke().setDashArray(usp.getPenStroke().getDashArray());
        } else {
            proportionalPoint.setPenStroke(new NullPenStrokeLegend());
        }
        if(fillNotNull){
            if(proportionalPoint.getFillLegend() instanceof NullSolidFillLegend){
                proportionalPoint.setFillLegend(new ConstantSolidFillLegend(new SolidFill()));
            }
            proportionalPoint.getFillLegend().setColor(usp.getFillLegend().getColor());
            proportionalPoint.getFillLegend().setOpacity(usp.getFillLegend().getOpacity());
        } else {
            proportionalPoint.setFillLegend(new NullSolidFillLegend());
        }
        proportionalPoint.setWellKnownName(usp.getWellKnownName());
    }

    private class ConfigPanel extends JPanel implements UIPanel {

        private UniqueSymbolPoint usp;

        public ConfigPanel(UniqueSymbolPoint point){
            usp = point;
        }

        // ************************* UIPanel ***************************
        @Override
        public URL getIconURL() {
            return UIFactory.getDefaultIcon();
        }

        @Override
        public String getTitle() {
            return I18N.tr("Stroke and fill settings");
        }

        @Override
        public String validateInput() {
            return null;
        }

        @Override
        public Component getComponent() {
            JPanel glob = new JPanel(new MigLayout("wrap 1"));

            // Update only a local preview until the changes are applied.
            // About #497
            CanvasSE localPreview = new CanvasSE(usp.getSymbolizer());

            glob.add(new LinePanel(usp,
                    localPreview,
                    I18N.tr(BORDER_SETTINGS),
                    true,
                    true));

            glob.add(new AreaPanel(usp,
                    localPreview,
                    I18N.tr(FILL_SETTINGS),
                    true));

            glob.add(new PreviewPanel(localPreview));

            return glob;
        }
    }
}
