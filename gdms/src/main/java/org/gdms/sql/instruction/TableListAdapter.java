/*
 * The GDMS library (Generic Datasources Management System)
 * is a middleware dedicated to the management of various kinds of
 * data-sources such as spatial vectorial data or alphanumeric. Based
 * on the JTS library and conform to the OGC simple feature access
 * specifications, it provides a complete and robust API to manipulate
 * in a SQL way remote DBMS (PostgreSQL, H2...) or flat files (.shp,
 * .csv...). GDMS is produced  by the geomatic team of the IRSTV
 * Institute <http://www.irstv.cnrs.fr/>, CNRS FR 2488:
 *    Erwan BOCHER, scientific researcher,
 *    Thomas LEDUC, scientific researcher,
 *    Fernando GONZALES CORTES, computer engineer.
 *
 * Copyright (C) 2007 Erwan BOCHER, Fernando GONZALES CORTES, Thomas LEDUC
 *
 * This file is part of GDMS.
 *
 * GDMS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GDMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GDMS. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult:
 *    <http://orbisgis.cerma.archi.fr/>
 *    <http://sourcesup.cru.fr/projects/orbisgis/>
 *    <http://listes.cru.fr/sympa/info/orbisgis-developers/>
 *    <http://listes.cru.fr/sympa/info/orbisgis-users/>
 *
 * or contact directly:
 *    erwan.bocher _at_ ec-nantes.fr
 *    fergonco _at_ gmail.com
 *    thomas.leduc _at_ cerma.archi.fr
 */
/*
 * Created on 12-oct-2004
 */
package org.gdms.sql.instruction;

import java.util.ArrayList;
import java.util.HashMap;

import org.gdms.data.DataSource;
import org.gdms.data.DataSourceCreationException;
import org.gdms.data.NoSuchTableException;
import org.gdms.driver.DriverException;
import org.gdms.driver.driverManager.DriverLoadException;

/**
 * Adaptador
 *
 * @author Fernando Gonz�lez Cort�s
 */
public class TableListAdapter extends Adapter {
	private DataSource[] tables;
	private String[] sourceNames;
	private HashMap<String, String> aliases;

	/**
	 * Gets the involved source names
	 *
	 * @param mode
	 *
	 * @return array de datasources
	 *
	 * @throws TableNotFoundException
	 *             Si no se encontr� alguna tabla
	 * @throws CreationException
	 * @throws NoSuchTableException
	 * @throws DriverLoadException
	 * @throws DriverException
	 * @throws DataSourceCreationException
	 * @throws RuntimeException
	 */
	public String[] getTableNames() {
		if (tables == null) {
			aliases = new HashMap<String, String>();
			Adapter[] hijos = getChilds();
			ArrayList<String> ret = new ArrayList<String>();

			for (int i = 0; i < hijos.length; i++) {
				TableRefAdapter tRef = (TableRefAdapter) hijos[i];
				ret.add(tRef.getName());
				if (tRef.getAlias() != null) {
					aliases.put(tRef.getName(), tRef.getAlias());
				}
			}

			sourceNames = ret.toArray(new String[0]);
		}

		return sourceNames;
	}

	public DataSource[] getTables(int mode) throws DriverLoadException,
			NoSuchTableException, DataSourceCreationException {
		String[] names = getTableNames();
		ArrayList<DataSource> ret = new ArrayList<DataSource>();
		for (String name : names) {
			String alias = aliases.get(name);
			if (alias != null) {
				ret.add(getInstructionContext().getDSFactory().getDataSource(
						name, alias, mode));
			} else {
				ret.add(getInstructionContext().getDSFactory().getDataSource(
						name, mode));
			}
		}

		return ret.toArray(new DataSource[0]);
	}
}
