package org.example.plus.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.example.plus.domain.post.model.dto.PostDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PostCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String CACHE_POST_PREFIX = "post:";

    // 캐시 조회
    public PostDto getPostCache(long postId) {
        String key = CACHE_POST_PREFIX + postId;
        return (PostDto) redisTemplate.opsForValue().get(key);
    }

    // 캐시 저장
    public void savePostCache(long postId, PostDto postDto) {
        String key = CACHE_POST_PREFIX + postId;
        redisTemplate.opsForValue().set(key, postDto, 10, TimeUnit.MINUTES);
    }

    // 캐시 삭제
    public void deletePostCache(long postId) {
        String key = CACHE_POST_PREFIX + postId;
        redisTemplate.delete(key);
    }
}
