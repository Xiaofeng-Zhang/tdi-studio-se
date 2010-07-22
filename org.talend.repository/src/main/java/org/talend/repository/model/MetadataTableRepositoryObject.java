// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.model;

import java.util.Iterator;

import org.eclipse.emf.common.util.EList;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.metadata.builder.connection.AbstractMetadataObject;
import org.talend.core.model.metadata.builder.connection.Concept;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.MDMConnection;
import org.talend.core.model.metadata.builder.connection.SAPFunctionUnit;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.IRepositoryViewObject;

/**
 * DOC nrousseau class global comment. Detailled comment
 */
public class MetadataTableRepositoryObject extends MetadataTable implements ISubRepositoryObject {

    private final IRepositoryViewObject repObj;

    private final org.talend.core.model.metadata.builder.connection.MetadataTable table;

    public MetadataTableRepositoryObject(IRepositoryViewObject repObj,
            org.talend.core.model.metadata.builder.connection.MetadataTable table) {
        this.repObj = repObj;
        this.table = table;
    }

    @Override
    public Object getAdapter(Class adapter) {
        if (adapter == org.talend.core.model.metadata.builder.connection.MetadataTable.class) {
            return table;
        }
        return null;
    }

    @Override
    public Property getProperty() {
        return repObj.getProperty();
    }

    // @Override
    // public void setProperty(Property property) {
    // repObj.setProperty(property);
    // }

    @Override
    public String getVersion() {
        return repObj.getVersion();
    }

    @Override
    public String getLabel() {
        return table.getLabel();
    }

    @Override
    public String getId() {
        return table.getId();
    }

    public String getTableType() {
        return table.getTableType();
    }

    public org.talend.core.model.metadata.builder.connection.MetadataTable getTable() {
        return this.table;
    }

    public AbstractMetadataObject getAbstractMetadataObject() {
        return getTable();
    }

    public void removeFromParent() {

        if (table.eContainer() instanceof SAPFunctionUnit) {
            SAPFunctionUnit funUnit = (SAPFunctionUnit) table.eContainer();
            funUnit.getTables().remove(table);
            return;
        }
        table.getConnection().getTables().remove(table);
        return;
    }

    public void removeFromParent(Connection connection) {
        EList tables = connection.getTables();
        if (tables != null) {
            Iterator iterator = tables.iterator();
            while (iterator.hasNext()) {
                Object obj = iterator.next();
                if (obj instanceof org.talend.core.model.metadata.builder.connection.MetadataTable) {
                    org.talend.core.model.metadata.builder.connection.MetadataTable repObj = (org.talend.core.model.metadata.builder.connection.MetadataTable) obj;
                    if (table != null && table.getLabel() != null && table.getLabel().equals(repObj.getLabel())) {
                        iterator.remove();
                        break;
                    }
                }
            }
            if (connection instanceof MDMConnection) {
                EList schemas = ((MDMConnection) connection).getSchemas();
                Iterator iterator2 = schemas.iterator();
                while (iterator2.hasNext()) {
                    Object object = iterator2.next();
                    if (object instanceof Concept) {
                        Concept concept = (Concept) object;
                        if (concept.getLabel() != null && concept.getLabel().equals(table.getLabel())) {
                            iterator2.remove();
                            break;
                        }
                    }
                }

            }
        }

    }
}
