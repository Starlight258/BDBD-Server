package bdbe.bdbd._core.errors.security;

import bdbe.bdbd.member.MemberResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static bdbe.bdbd._core.errors.security.JWTProvider.TOKEN_PREFIX;

@Service
@CacheConfig(cacheNames = "jwtTokens")
public class CacheService {

    private final CacheManager cacheManager;

    @Autowired
    public CacheService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
    private static final Logger logger = LoggerFactory.getLogger(CacheService.class);

    @Cacheable(key = "#token", condition = "#token != null")
    public String cacheToken(String token) {
        if (!token.startsWith(TOKEN_PREFIX)) {
            token = TOKEN_PREFIX + token;
        }
        logger.info("Caching token: {}", token);
        return token;
    }

    @CacheEvict(key = "#token")
    public void evictToken(String token) {
        if (!token.startsWith(TOKEN_PREFIX)) {
            token = TOKEN_PREFIX + token;
        }
        Cache cache = cacheManager.getCache("jwtTokens");
        if (cache != null && cache.get(token) != null) {
            cache.evict(token);
            logger.info("Token evicted from cache: {}", token);
        } else {
            logger.warn("Attempted to evict a token that does not exist in cache: {}", token);
        }
    }
    public boolean isTokenCached(String token) {
        if (!token.startsWith(TOKEN_PREFIX)) {
            token = TOKEN_PREFIX + token;
        }

        Cache cache = cacheManager.getCache("jwtTokens");
        if (cache != null) {
            boolean isCached = cache.get(token) != null;
            logger.info("Token cached status for '{}': {}", token, isCached);
            return isCached;
        } else {
            logger.error("Cache 'jwtTokens' is not available.");
            return false;
        }

    }
    public MemberResponse.LogoutResponse logout(String token) {
        evictToken(token);
        boolean isCachedPostEviction = isTokenCached(token);
        logger.debug("Token cache status post-eviction for '{}': {}", token, isCachedPostEviction);
        MemberResponse.LogoutResponse response = new MemberResponse.LogoutResponse();
        response.setSuccess(!isCachedPostEviction);
        return response;
    }
}
