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
package org.orbisgis.view.toc.actions.cui;

import org.h2gis.utilities.GeometryTypeCodes;

/**
 * A simple representation of geometry types. We don't have as much types here
 * as in {@link org.h2gis.utilities.GeometryTypeCodes}, and that's on purpose. Indeed, values defined here are
 * intended to be used in GUI, consider in that most of the times, we will
 * process multi-points and points the same way, for instance.
 * @author Alexis Guéganno
 * @author Nicolas Fortin
 */
public class SimpleGeometryType {

        private SimpleGeometryType(){};

        public static final int POINT = 1;
        public static final int LINE = 2;
        public static final int POLYGON = 4;
        public static final int ALL = POINT | LINE | POLYGON;

    /**
     * Gets the simple representation of {@code type}.
     *
     * @param type One of {@link org.h2gis.utilities.GeometryTypeCodes}
     * @return One of the constants defined in this class
     * @throws IllegalArgumentException If {@code type} is not a geometry
     * type.
     *
     */
    public static int getSimpleType(int type){
        switch(type){
            case GeometryTypeCodes.POINT:
            case GeometryTypeCodes.POINTZ:
            case GeometryTypeCodes.POINTZM:
            case GeometryTypeCodes.MULTIPOINT:
            case GeometryTypeCodes.MULTIPOINTZ:
            case GeometryTypeCodes.MULTIPOINTM:
                return POINT;
            case GeometryTypeCodes.LINESTRING:
            case GeometryTypeCodes.LINESTRINGZ:
            case GeometryTypeCodes.LINESTRINGZM:
            case GeometryTypeCodes.MULTILINESTRING:
            case GeometryTypeCodes.MULTILINESTRINGZ:
            case GeometryTypeCodes.MULTILINESTRINGM:
                return LINE;
            case GeometryTypeCodes.POLYGON:
            case GeometryTypeCodes.POLYGONM:
            case GeometryTypeCodes.POLYGONZ:
            case GeometryTypeCodes.MULTIPOLYGON:
            case GeometryTypeCodes.MULTIPOLYGONZ:
            case GeometryTypeCodes.MULTIPOLYGONM:
                return POLYGON;
            default:
                return ALL;
        }
    }
}
