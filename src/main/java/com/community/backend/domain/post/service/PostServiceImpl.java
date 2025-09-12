package com.community.backend.domain.post.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.community.backend.common.dto.CommonPagingResponseDto;
import com.community.backend.common.exception.BaseException;
import com.community.backend.common.security.jwt.JwtPayload;
import com.community.backend.common.util.BaseInternalWebClient;
import com.community.backend.common.util.RedisService;
import com.community.backend.domain.member.dto.response.MemberResponseDto;
import com.community.backend.domain.member.entity.Member;
import com.community.backend.domain.post.dto.request.PostCreateRequestDto;
import com.community.backend.domain.post.dto.request.PostModifyRequestDto;
import com.community.backend.domain.post.dto.request.PostPagingRequestDto;
import com.community.backend.domain.post.dto.response.PostResponseDto;
import com.community.backend.domain.post.entity.Post;
import com.community.backend.domain.post.exception.PostExceptionEnum;
import com.community.backend.domain.post.repository.PostRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final RedisService<Long> redisService;
    private final BaseInternalWebClient webClient;

    private final static String REDIS_KEY_PREFIX_VIEW_COUNT = "post:views";
    private final static Long REDIS_DURATION_VIEW_COUNT = 60L * 60L;

    @Transactional
    public PostResponseDto getPost(Long postIdx) {
        PostResponseDto result = null;

        Post post = existPost(postIdx);

        MemberResponseDto member = null;
        
        try {
            member = Objects.requireNonNull(webClient.get("/v1/member/" + post.getMemberIdx(), MemberResponseDto.class).block()).getData();
        } catch(Exception e) {
            e.getStackTrace();
            e.printStackTrace();
        }

        // TODO : 사용자마다 조회 쿨타임? 적용해야 함
        post.setViewCount(post.getViewCount() + 1);

        // View Count Redis : 키 - post:views:{postIdx}:{HHmm}
        // 인기글을 위한 조회수 캐싱(1분 단위, 만료 기한은 1시간)
        LocalDateTime now = LocalDateTime.now();
        String redisKey = String.format("%s:%d:%s", REDIS_KEY_PREFIX_VIEW_COUNT, post.getIdx(),
                now.format(DateTimeFormatter.ofPattern("HHmm")));
        if (redisService.hasKey(redisKey) == false) {
            redisService.setValue(redisKey, 1L, REDIS_DURATION_VIEW_COUNT);
        } else {
            redisService.incrementValue(redisKey, 1L);
        }

        result = new PostResponseDto(post, member.idx(), member.nickname());

        return result;
    }

    public CommonPagingResponseDto<List<PostResponseDto>> getPosts(PostPagingRequestDto requestDto) {
        requestDto.clean();

        Pageable pageable = PageRequest.of(requestDto.getPage(), requestDto.getSize());

        List<PostResponseDto> responseList = new ArrayList<>();
        Long totalCount = 0L;
        int totalPage = 0;

        Page<Post> result = postRepository.getPosts(requestDto, pageable);

        // Member 리스트 불러오는 로직
        // String memberIdxs = result.stream().map(p -> p.getMemberIdx()).distinct().map(String::valueOf)
        //         .collect(Collectors.joining(","));
        // List<MemberResponseDto> memberList = webClient.getList("/v1/member/list?idxs=" + memberIdxs, MemberResponseDto.class).block()
        //         .getData();
        // Map<Long, MemberResponseDto> memberMap = memberList.stream()
        //         .collect(Collectors.toMap(MemberResponseDto::idx, m -> m));

        Map<Long, MemberResponseDto> memberMap = new HashMap<>();
        // List<Long> memberIdxList = result.stream().map(p -> p.getMemberIdx()).distinct().toList();
        // for(Long idx : memberIdxList) {
        //     MemberResponseDto member = webClient.get("/v1/member/" + idx, MemberResponseDto.class).block().getData();
        //     memberMap.put(member.idx(), member);
        // }

        responseList = result.stream()
                .map(post -> new PostResponseDto(post, 0L, "")).toList();
        totalCount = result.getTotalElements();
        totalPage = (int) Math.ceil((double) totalCount / requestDto.getSize());

        CommonPagingResponseDto<List<PostResponseDto>> response = new CommonPagingResponseDto<>(requestDto,
                responseList);
        response.setTotalCount(totalCount);
        response.setTotalPage(totalPage);

        return response;
    }

    public List<PostResponseDto> getPopularPosts() {
        List<PostResponseDto> result = new ArrayList<>();

        String keyPattern = String.format("%s:*:*", REDIS_KEY_PREFIX_VIEW_COUNT);
        Set<String> keys = redisService.getKeys(keyPattern);

        Map<Long, Long> viewCountMap = new HashMap<>();
        if (keys != null && !keys.isEmpty()) {
            for (String key : keys) {
                if (key == null || key.isEmpty())
                    continue;

                // Key - post:views:{postIdx}:{HHmm}
                String[] parts = key.split(":");
                if (parts.length == 4) {
                    Long postIdx = Long.parseLong(parts[2]);
                    Integer viewCount = Integer.parseInt(redisService.getValue(key).toString());

                    viewCountMap.put(postIdx, viewCountMap.getOrDefault(postIdx, 0L) + viewCount);
                }
            }
        }

        List<Long> topPostIdxs = viewCountMap.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(10)
                .map(Map.Entry::getKey)
                .toList();

        if (topPostIdxs.isEmpty())
            return result;

        // Post 리스트 조회
        List<Post> posts = postRepository.getPostsByIdxs(topPostIdxs);

        // Member 리스트 불러오기
        String memberIdxs = posts.stream()
                .map(Post::getMemberIdx)
                .distinct()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        List<Member> memberList = webClient.getList("/v1/member/list?idxs=" + memberIdxs, Member.class)
                .block().getData();
        Map<Long, Member> memberMap = memberList.stream()
                .collect(Collectors.toMap(Member::getIdx, m -> m));

        result = posts.stream()
                .map(post -> new PostResponseDto(post, memberMap.getOrDefault(post.getMemberIdx(), null)))
                .toList();

        return result;
    }

    @Transactional
    public void createPost(PostCreateRequestDto requestDto, JwtPayload jwtPayload) {
        Post post = new Post();
        post.setTitle(requestDto.title());
        post.setContents(requestDto.contents());
        post.setViewCount(0);
        post.setState(1);

        postRepository.createPost(post, jwtPayload.idx(), requestDto.categoryIdx());
    }

    @Transactional
    public void modifyPost(PostModifyRequestDto requestDto, Long postIdx, JwtPayload jwtPayload) {
        Post post = existPost(postIdx);

        isPostMemberMatch(post, jwtPayload.idx());

        post.setTitle(requestDto.title());
        post.setContents(requestDto.contents());

        postRepository.savePost(post);
    }

    @Transactional
    public void deletePost(Long postIdx, JwtPayload jwtPayload) {
        Post post = existPost(postIdx);

        isPostMemberMatch(post, jwtPayload.idx());

        post.setState(2);

        postRepository.savePost(post);
    }

    // 검증 메소드
    private Post existPost(Long postIdx) {
        if (postIdx == null || postIdx <= 0)
            throw new BaseException(PostExceptionEnum.POST_NOT_FOUND);

        Post post = postRepository.getPostByIdx(postIdx).orElse(null);

        if (post == null || post.getState() != 1)
            throw new BaseException(PostExceptionEnum.POST_NOT_FOUND);

        return post;
    }

    private void isPostMemberMatch(Post post, Long memberIdx) {
        if (post.getMemberIdx().equals(memberIdx) == false)
            throw new BaseException(PostExceptionEnum.POST_MEMBER_NOT_MATCH);
    }
}
