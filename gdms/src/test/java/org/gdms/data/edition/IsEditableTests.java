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
package org.gdms.data.edition;

import org.gdms.BaseTest;
import org.gdms.data.DataSource;
import org.gdms.data.DataSourceFactory;
import org.gdms.data.object.ObjectSourceDefinition;
import org.gdms.driver.driverManager.DriverManager;
import org.gdms.source.SourceManager;

public class IsEditableTests extends BaseTest {

	private DataSourceFactory dsf;

	public void testObject() throws Exception {
		DataSource ds = dsf.getDataSource("readObject");
		assertFalse(ds.isEditable());
		ds = dsf.getDataSource("readWriteObject");
		assertFalse(ds.isEditable());
		ReadDriver.isEditable = true;
		assertTrue(ds.isEditable());
	}

	public void testFile() throws Exception {
		DataSource ds = dsf.getDataSource("readFile");
		assertFalse(ds.isEditable());
		ds = dsf.getDataSource("readWriteFile");
		assertFalse(ds.isEditable());
		ReadDriver.isEditable = true;
		assertTrue(ds.isEditable());
	}

	public void testDB() throws Exception {
		DataSource ds = dsf.getDataSource("readDB");
		assertFalse(ds.isEditable());
		ds = dsf.getDataSource("readWriteDB");
		assertFalse(ds.isEditable());
		ReadDriver.isEditable = true;
		assertTrue(ds.isEditable());
	}

	@Override
	protected void setUp() throws Exception {
		ReadDriver.initialize();

		dsf = new DataSourceFactory();
		DriverManager dm = new DriverManager();
		dm.registerDriver("readwritedriver", ReadDriver.class);
		SourceManager sourceManager = dsf.getSourceManager();
		sourceManager.setDriverManager(dm);

		sourceManager.register("readObject", new ObjectSourceDefinition(
				new ReadDriver()));
		sourceManager.register("readWriteObject", new ObjectSourceDefinition(
				new ReadAndWriteDriver()));
		sourceManager.register("readFile", new FakeFileSourceDefinition(
				new ReadDriver()));
		sourceManager.register("readWriteFile", new FakeFileSourceDefinition(
				new ReadAndWriteDriver()));
		sourceManager.register("readDB", new FakeDBTableSourceDefinition(
				new ReadDriver(), "jdbc:executefailing"));
		sourceManager.register("readWriteDB", new FakeDBTableSourceDefinition(
				new ReadAndWriteDriver(), "jdbc:closefailing"));
	}
}
