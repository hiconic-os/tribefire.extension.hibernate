package com.braintribe.persistence.hibernate;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.cfg.EnvironmentSettings;
import org.hibernate.cfg.JdbcSettings;
import org.hibernate.cfg.SchemaToolingSettings;
import org.hibernate.cfg.SessionEventSettings;
import org.hibernate.service.ServiceRegistry;

import com.braintribe.cfg.Configurable;
import com.braintribe.model.access.hibernate.interceptor.GmAdaptionInterceptor;
import com.braintribe.model.generic.GMF;
import com.braintribe.model.processing.deployment.hibernate.mapping.HbmXmlGeneratingService;
import com.braintribe.model.processing.meta.cmd.CmdResolver;
import com.braintribe.utils.FileTools;
import com.braintribe.utils.stream.ReaderInputStream;

public class HibernateModelSessionFactoryBuilder {
	private CmdResolver cmdResolver;
	private DataSource dataSource;
	private File ormDebugOutputFolder;

	public HibernateModelSessionFactoryBuilder(CmdResolver cmdResolver, DataSource dataSource) {
		super();
		this.cmdResolver = cmdResolver;
		this.dataSource = dataSource;
	}
	
	@Configurable
	public void setOrmDebugOutputFolder(File ormDebugOutputFolder) {
		this.ormDebugOutputFolder = ormDebugOutputFolder;
	}
	
	private ClassLoader itwOrModuleClassLoader() {
		if (isLoadedByModule())
			return getClass().getClassLoader();
		else
			return (ClassLoader) GMF.getTypeReflection().getItwClassLoader();
	}

	private boolean isLoadedByModule() {
		return getClass().getClassLoader().getClass().getSimpleName().startsWith("ModuleClassLoader");
	}
	
	public SessionFactory build() {
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
				.applySetting(JdbcSettings.JAKARTA_JTA_DATASOURCE, dataSource)
				.applySetting(SessionEventSettings.INTERCEPTOR, new GmAdaptionInterceptor())
				.applySetting(EnvironmentSettings.TC_CLASSLOADER, itwOrModuleClassLoader())
				.applySetting(SchemaToolingSettings.HBM2DDL_AUTO, "update")
				.build();
				
		MetadataSources sources = new MetadataSources(serviceRegistry);
		
		generateMappings(sources);
		
		MetadataImplementor metadata = (MetadataImplementor) sources.buildMetadata();
		SessionFactory sessionFactory = metadata.buildSessionFactory();
		
		return sessionFactory;
    }

	private void generateMappings(MetadataSources sources) {
		
		new HbmXmlGeneratingService() //
				.cmdResolverAndModel(cmdResolver) //
				//.generateJpaOrm() //
				.entityMappingConsumer(sd -> {
					if (ormDebugOutputFolder != null) {
						File outputFile = new File(ormDebugOutputFolder, sd.sourceRelativePath);
						
						outputFile.getParentFile().mkdirs();
						
						FileTools.write(outputFile) //
							.withCharset(StandardCharsets.UTF_8) //
							.string(sd.sourceCode);
					}
						
					try (ReaderInputStream in = new ReaderInputStream(new StringReader(sd.sourceCode))) {
						sources.addInputStream(in);
					} catch (IOException e) {
						throw new UncheckedIOException("Error while applying " + sd.sourceRelativePath + " as InputStream hibernate configuration", e);
					}
				}).renderMappings();
	}
}
