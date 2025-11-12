package com.braintribe.model.access.hibernate.interceptor;
import org.hibernate.EntityMode;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

public final class GmTuplizerIntegrator implements Integrator {

    @Override
    public void integrate(
            org.hibernate.boot.Metadata metadata,
            SessionFactoryImplementor sessionFactory,
            SessionFactoryServiceRegistry serviceRegistry) {

        MetadataImplementor mi = (MetadataImplementor) metadata;
        for (PersistentClass pc : mi.getEntityBindings()) {
            Class<?> mapped = pc.getMappedClass();
            if (mapped != null
                && com.braintribe.model.generic.GenericEntity.class.isAssignableFrom(mapped)) {
                pc.addTuplizer(EntityMode.POJO, GmTuplizer.class.getName());
            }
        }
    }

    @Override
    public void disintegrate(
            SessionFactoryImplementor sessionFactory,
            SessionFactoryServiceRegistry serviceRegistry) {
        // no-op
    }
}
