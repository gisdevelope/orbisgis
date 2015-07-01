/**
 * OrbisToolBox is an OrbisGIS plugin dedicated to create and manage processing.
 *
 * OrbisToolBox is distributed under GPL 3 license. It is produced by CNRS <http://www.cnrs.fr/> as part of the
 * MApUCE project, funded by the French Agence Nationale de la Recherche (ANR) under contract ANR-13-VBDU-0004.
 *
 * OrbisToolBox is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * OrbisToolBox is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with OrbisToolBox. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/> or contact directly: info_at_orbisgis.org
 */

package org.orbisgis.orbistoolboxapi.annotations.model

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Groovy annotation that can be used in a groovy script to declare a literal data field.
 * This annotation define all the properties of the literal data represented such as its format, value and valid domain.
 *
 * @author Sylvain PALOMINOS
 */
@Retention(RetentionPolicy.RUNTIME)
@interface LiteralDataAttribute {

    /** Default value for the formats attribute */
    FormatAttribute[] defaultFormats =  []
    /** Default value for the validDomain attribute */
    LiteralDataDomainAttribute[] defaulValidDomains = []


    /** List of supported formats */
    FormatAttribute[] formats() default []
    /** The valid domain for literal data */
    LiteralDataDomainAttribute[] validDomains() default []
    /** The literal value information */
    LiteralValueAttribute valueAttribute()
}