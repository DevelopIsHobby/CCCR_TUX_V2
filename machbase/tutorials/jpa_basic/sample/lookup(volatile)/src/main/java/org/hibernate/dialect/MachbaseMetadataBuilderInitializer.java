package org.hibernate.dialect;

import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.spi.MetadataBuilderInitializer;
import org.hibernate.engine.jdbc.dialect.internal.DialectResolverSet;
import org.hibernate.engine.jdbc.dialect.spi.DialectResolutionInfo;
import org.hibernate.engine.jdbc.dialect.spi.DialectResolver;
import org.jboss.logging.Logger;

public class MachbaseMetadataBuilderInitializer implements MetadataBuilderInitializer {
    private static final Logger logger = Logger.getLogger(MachbaseMetadataBuilderInitializer.class);
    private static final MachbaseDialect dialect = new MachbaseDialect();
    private static final DialectResolver resolver = new DialectResolver() {
        public Dialect resolveDialect(DialectResolutionInfo info) {
            return info.getDatabaseName().equals("Machbase") ? MachbaseMetadataBuilderInitializer.dialect : null;
        }
    };

    public MachbaseMetadataBuilderInitializer() {
    }

    public void contribute(MetadataBuilder metadataBuilder, StandardServiceRegistry serviceRegistry) {
        DialectResolver dialectResolver = (DialectResolver)serviceRegistry.getService(DialectResolver.class);
        if (!(dialectResolver instanceof DialectResolverSet)) {
            logger.warnf("DialectResolver '%s' is not an instance of DialectResolverSet, not registering MachbaseDialect", dialectResolver);
        } else {
            ((DialectResolverSet)dialectResolver).addResolver(resolver);
        }
    }
}
