package com.sololiving.global.config;

import com.sololiving.global.handlers.MybatisEnumHandler;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.sololiving.**.**.mapper")
public class MybatisConfig {

        @Bean
        public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
                SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
                sqlSessionFactoryBean.setDataSource(dataSource);

                // 설정 파일의 내용을 Java 설정으로 변환
                org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
                configuration.setCacheEnabled(true);
                configuration.setLazyLoadingEnabled(true);

                // Underscore To CamelCase
                configuration.setMapUnderscoreToCamelCase(true);

                // TypeHandlers 설정
                configuration.getTypeHandlerRegistry().register(
                                com.sololiving.global.security.jwt.enums.TokenStatus.class,
                                MybatisEnumHandler.class);
                configuration.getTypeHandlerRegistry().register(com.sololiving.global.security.jwt.enums.ClientId.class,
                                MybatisEnumHandler.class);
                configuration.getTypeHandlerRegistry().register(com.sololiving.domain.user.enums.Status.class,
                                MybatisEnumHandler.class);
                configuration.getTypeHandlerRegistry().register(com.sololiving.domain.user.enums.UserType.class,
                                MybatisEnumHandler.class);
                configuration.getTypeHandlerRegistry().register(com.sololiving.domain.user.enums.Gender.class,
                                MybatisEnumHandler.class);
                configuration.getTypeHandlerRegistry().register(com.sololiving.domain.article.enums.Status.class,
                                MybatisEnumHandler.class);
                configuration.getTypeHandlerRegistry().register(com.sololiving.domain.article.enums.MediaType.class,
                                MybatisEnumHandler.class);
                configuration.getTypeHandlerRegistry().register(com.sololiving.domain.report.enums.ReportStatus.class,
                                MybatisEnumHandler.class);
                configuration.getTypeHandlerRegistry().register(com.sololiving.domain.report.enums.ReportType.class,
                                MybatisEnumHandler.class);
                configuration.getTypeHandlerRegistry().register(com.sololiving.domain.report.enums.SubjectType.class,
                                MybatisEnumHandler.class);
                configuration.getTypeHandlerRegistry().register(com.sololiving.domain.log.enums.ActivityType.class,
                                MybatisEnumHandler.class);
                configuration.getTypeHandlerRegistry().register(com.sololiving.domain.log.enums.AuthMethod.class,
                                MybatisEnumHandler.class);
                configuration.getTypeHandlerRegistry().register(com.sololiving.domain.log.enums.BoardMethod.class,
                                MybatisEnumHandler.class);
                configuration.getTypeHandlerRegistry().register(com.sololiving.domain.log.enums.BlockMethod.class,
                                MybatisEnumHandler.class);
                configuration.getTypeHandlerRegistry().register(com.sololiving.domain.log.enums.FollowMethod.class,
                                MybatisEnumHandler.class);

                sqlSessionFactoryBean.setConfiguration(configuration);

                // TypeAliases 설정
                sqlSessionFactoryBean.setTypeAliasesPackage("com.sololiving.domain.vo");

                // 매퍼 파일 설정
                sqlSessionFactoryBean.setMapperLocations(
                                new PathMatchingResourcePatternResolver().getResources("classpath:mappers/**/*.xml"));

                return sqlSessionFactoryBean.getObject();
        }

        @Bean
        public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
                return new SqlSessionTemplate(sqlSessionFactory);
        }

        @Bean
        public PlatformTransactionManager transactionManager(DataSource dataSource) {
                return new DataSourceTransactionManager(dataSource);
        }
}
