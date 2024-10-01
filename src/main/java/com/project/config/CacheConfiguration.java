package com.project.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build()
        );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.project.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.project.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.project.domain.User.class.getName());
            createCache(cm, com.project.domain.Authority.class.getName());
            createCache(cm, com.project.domain.User.class.getName() + ".authorities");
            createCache(cm, com.project.domain.CusNote.class.getName());
            createCache(cm, com.project.domain.Customer.class.getName());
            createCache(cm, com.project.domain.Customer.class.getName() + ".customerEquipments");
            createCache(cm, com.project.domain.Customer.class.getName() + ".orders");
            createCache(cm, com.project.domain.Customer.class.getName() + ".cusNotes");
            createCache(cm, com.project.domain.CustomerEquipment.class.getName());
            createCache(cm, com.project.domain.District.class.getName());
            createCache(cm, com.project.domain.District.class.getName() + ".customers");
            createCache(cm, com.project.domain.District.class.getName() + ".orders");
            createCache(cm, com.project.domain.District.class.getName() + ".wards");
            createCache(cm, com.project.domain.Equipment.class.getName());
            createCache(cm, com.project.domain.Equipment.class.getName() + ".customerEquipments");
            createCache(cm, com.project.domain.HistoryOrder.class.getName());
            createCache(cm, com.project.domain.Order.class.getName());
            createCache(cm, com.project.domain.Order.class.getName() + ".historyOrders");
            createCache(cm, com.project.domain.Province.class.getName());
            createCache(cm, com.project.domain.Province.class.getName() + ".customers");
            createCache(cm, com.project.domain.Province.class.getName() + ".orders");
            createCache(cm, com.project.domain.Province.class.getName() + ".districts");
            createCache(cm, com.project.domain.Sale.class.getName());
            createCache(cm, com.project.domain.Sale.class.getName() + ".orders");
            createCache(cm, com.project.domain.SendSMS.class.getName());
            createCache(cm, com.project.domain.SourceOrder.class.getName());
            createCache(cm, com.project.domain.SourceOrder.class.getName() + ".orders");
            createCache(cm, com.project.domain.Staff.class.getName());
            createCache(cm, com.project.domain.Staff.class.getName() + ".orders");
            createCache(cm, com.project.domain.Ward.class.getName());
            createCache(cm, com.project.domain.Ward.class.getName() + ".customers");
            createCache(cm, com.project.domain.Ward.class.getName() + ".orders");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
