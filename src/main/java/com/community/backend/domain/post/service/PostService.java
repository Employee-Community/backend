package com.community.backend.domain.post.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.community.backend.common.dto.CommonPagingResponseDto;
import com.community.backend.domain.post.dto.request.PostPagingRequestDto;
import com.community.backend.domain.post.dto.response.PostResponseDto;
import com.community.backend.domain.post.entity.Post;
import com.community.backend.domain.post.repository.PostJpaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostJpaRepository postJpaRepository;
    private final StringRedisTemplate stringRedisTemplate;

    private final static String REDIS_KEY_PREFIX_VIEW_COUNT = "post:views";

    @Transactional
    public PostResponseDto getPost(Long postIdx) {
        if (postIdx == null || postIdx <= 0)
            return null;

        PostResponseDto result = null;

        Post post = postJpaRepository.findById(postIdx).orElse(null);

        if (post != null) {
            // TODO : 사용자마다 조회 쿨타임? 적용해야 함
            post.setViewCount(post.getViewCount() + 1);
            postJpaRepository.save(post);

            // View Count Redis : 키 - post:views:{postIdx}:{HHmm}
            // 인기글을 위한 조회수 캐싱(1분 단위, 만료 기한은 1시간)
            LocalDateTime now = LocalDateTime.now();
            String redisKey = String.format("%s:%d:%s", REDIS_KEY_PREFIX_VIEW_COUNT, post.getIdx(),
                    now.format(DateTimeFormatter.ofPattern("HHmm")));
            stringRedisTemplate.opsForValue().increment(redisKey, 1);
            stringRedisTemplate.expire(redisKey, java.time.Duration.ofHours(1));

            result = new PostResponseDto(post);
        }

        return result;
    }

    public CommonPagingResponseDto<List<PostResponseDto>> getPosts(PostPagingRequestDto requestDto) {
        requestDto.clean();

        Pageable pageable = PageRequest.of(requestDto.getPage(), requestDto.getSize());

        List<PostResponseDto> result = new ArrayList<>();

        switch (requestDto.getSearch()) {
            case "title":
                result = postJpaRepository.findByTitleContaining(requestDto.getKeyword(), pageable).stream()
                        .map(post -> new PostResponseDto(post)).toList();
                break;
            case "contents":
                result = postJpaRepository.findByContentsContaining(requestDto.getKeyword(), pageable).stream()
                        .map(post -> new PostResponseDto(post)).toList();
                break;
            case "member":
                result = postJpaRepository.findByMember_NameContaining(requestDto.getKeyword(), pageable).stream()
                        .map(post -> new PostResponseDto(post)).toList();
                break;
            default:
                result = postJpaRepository.findAll(pageable).stream()
                        .map(post -> new PostResponseDto(post)).toList();
                break;
        }

        return new CommonPagingResponseDto<>(requestDto, result);
    }

    public List<PostResponseDto> getPopularPosts() {
        List<PostResponseDto> result = new ArrayList<>();

        String redisKey = String.format("%s:*:*", REDIS_KEY_PREFIX_VIEW_COUNT);

        Set<String> keys = stringRedisTemplate.keys(redisKey);

        Map<Long, Long> viewCountMap = new HashMap<>();
        if (keys != null && !keys.isEmpty()) {
            for (String key : keys) {
                if (key == null || key.isEmpty())
                    continue;

                // Key - post:views:{postIdx}:{HHmm}
                String[] parts = key.split(":");
                if (parts.length == 4) {
                    Long postIdx = Long.parseLong(parts[2]);
                    Integer viewCount = Integer.parseInt(stringRedisTemplate.opsForValue().get(key));

                    if (viewCountMap.containsKey(postIdx)) {
                        viewCountMap.put(postIdx, viewCountMap.get(postIdx) + viewCount);
                    } else {
                        viewCountMap.put(postIdx, (long) viewCount);
                    }
                }
            }
        }

        result = viewCountMap.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(10)
                .map(Map.Entry::getKey)
                .toList().stream()
                .map(postIdx -> new PostResponseDto(postJpaRepository.findById(postIdx).orElse(null)))
                .toList();

        return result;
    }
}
