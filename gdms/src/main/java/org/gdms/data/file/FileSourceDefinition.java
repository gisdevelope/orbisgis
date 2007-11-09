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
package org.gdms.data.file;

import java.io.File;

import org.gdms.data.AbstractDataSourceDefinition;
import org.gdms.data.DataSource;
import org.gdms.data.DataSourceCreationException;
import org.gdms.data.DataSourceDefinition;
import org.gdms.driver.DriverException;
import org.gdms.driver.DriverUtilities;
import org.gdms.driver.FileDriver;
import org.gdms.driver.FileReadWriteDriver;
import org.gdms.driver.ReadOnlyDriver;
import org.gdms.source.directory.DefinitionType;
import org.gdms.source.directory.FileDefinitionType;

/**
 * Definition of file sources
 *
 * @author Fernando Gonzalez Cortes
 */
public class FileSourceDefinition extends AbstractDataSourceDefinition {
	public File file;

	public FileSourceDefinition(File file) {
		this.file = file;
	}

	public FileSourceDefinition(String fileName) {
		this.file = new File(fileName);
	}

	public DataSource createDataSource(String tableName)
			throws DataSourceCreationException {
		if (!file.exists()) {
			throw new DataSourceCreationException(file + " does not exists");
		}
		((ReadOnlyDriver) getDriver())
				.setDataSourceFactory(getDataSourceFactory());

		FileDataSourceAdapter ds = new FileDataSourceAdapter(
				getSource(tableName), tableName, file, (FileDriver) getDriver());
		return ds;
	}

	protected ReadOnlyDriver getDriverInstance() {
		return DriverUtilities.getDriver(getDataSourceFactory()
				.getSourceManager().getDriverManager(), file);
	}

	public File getFile() {
		return file;
	}

	public void createDataSource(DataSource contents) throws DriverException {
		FileReadWriteDriver d = (FileReadWriteDriver) getDriver();
		d.setDataSourceFactory(getDataSourceFactory());
		contents.open();
		try {
			d.writeFile(file, contents);
		} catch (DriverException e) {
			contents.cancel();
			throw e;
		}
		contents.cancel();
	}

	public DefinitionType getDefinition() {
		FileDefinitionType ret = new FileDefinitionType();
		ret.setPath(file.getAbsolutePath());

		return ret;
	}

	public static DataSourceDefinition createFromXML(
			FileDefinitionType definitionType) {
		return new FileSourceDefinition(definitionType.getPath());
	}

	@Override
	public String calculateChecksum() throws DriverException {
		long lastModified = file.lastModified();
		return Long.toString(lastModified);
	}
}
