package com.braintribe.model.access.hibernate.interceptor;

import java.io.Serializable;

import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.tuple.Instantiator;
import org.hibernate.tuple.entity.EntityMetamodel;
import org.hibernate.tuple.entity.PojoEntityTuplizer;

import com.braintribe.model.generic.GMF;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.GenericModelTypeReflection;

/**
 * Replacement for GmAdaptionInterceptor (Hibernate 5.6).
 * Handles both entity name resolution and instantiation.
 */
public class GmTuplizer extends PojoEntityTuplizer {

    private static final GenericModelTypeReflection typeReflection = GMF.getTypeReflection();

    public GmTuplizer(EntityMetamodel entityMetamodel, PersistentClass persistentClass) {
        super(entityMetamodel, persistentClass);
    }

    /**
     * Dynamic entity-name resolution for GenericEntity instances.
     * Equivalent to Interceptor#getEntityName(Object).
     */
    @Override
    public String determineConcreteSubclassEntityName(Object entityInstance,
                                                      SessionFactoryImplementor factory) {
        if (entityInstance instanceof GenericEntity ge) {
            return ge.entityType().getTypeSignature();
        }
        return super.determineConcreteSubclassEntityName(entityInstance, factory);
    }

    /**
     * Custom instantiation logic (equivalent to Interceptor#instantiate).
     * Correct Hibernate 5.6 signature.
     */
    @Override
    protected Instantiator buildInstantiator(EntityMetamodel entityMetamodel,
                                             PersistentClass persistentClass) {
        final String entityName = persistentClass.getEntityName();
        EntityType<?> type = typeReflection.getType(entityName);

        return new Instantiator() {
            @Override
            public Object instantiate(Serializable id) {
                GenericEntity ge = type.createPlain();
                if (id != null)
                    ge.setId(id);
                return ge;
            }

            @Override
            public Object instantiate() {
                return instantiate(null);
            }

            @Override
            public boolean isInstance(Object object) {
                return type.isInstance(object);
            }
        };
    }
}
