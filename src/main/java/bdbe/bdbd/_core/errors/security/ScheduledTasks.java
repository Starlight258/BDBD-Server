package bdbe.bdbd._core.errors.security;

import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class ScheduledTasks {

    @Autowired
    private CacheManager cacheManager;

    @Scheduled(fixedRate = 10800000)  // 3시간마다 실행
    public void evictExpiredTokens() {
        Cache cache = cacheManager.getCache("jwtTokens");
        if (cache != null) {
            Object nativeCache = cache.getNativeCache();
            if (nativeCache instanceof ConcurrentMapCache) {
                ConcurrentMapCache mapCache = (ConcurrentMapCache) nativeCache;
                mapCache.getNativeCache().keySet().forEach(key -> {
                    String token = (String) key;
                    try {
                        JWTProvider.verify(token);
                    } catch (TokenExpiredException tee) {
                        mapCache.evict(key);
                    }
                });
            }
        }
    }
}
