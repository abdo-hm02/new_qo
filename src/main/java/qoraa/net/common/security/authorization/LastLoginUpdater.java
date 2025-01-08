package qoraa.net.common.security.authorization;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.time.LocalDateTime;

@CacheConfig(cacheNames = "lastLoginTimeCache")
public interface LastLoginUpdater {

    @Cacheable(key = "#email")
    LocalDateTime getLastLogin(String email);

    @CacheEvict(key = "#email")
    void updateLastLogin(String email, LocalDateTime lastLogin);
}
