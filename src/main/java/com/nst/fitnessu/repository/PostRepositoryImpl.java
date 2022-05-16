package com.nst.fitnessu.repository;

import com.nst.fitnessu.domain.Post;
import com.nst.fitnessu.domain.Type;
import com.nst.fitnessu.dto.post.PostListResponseDto;
import com.nst.fitnessu.dto.post.SearchPostRequestDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static com.nst.fitnessu.domain.QPost.post;

@Slf4j
public class PostRepositoryImpl extends QuerydslRepositorySupport implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PostRepositoryImpl(EntityManager em) {
        super(Post.class);
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public List<PostListResponseDto> findAllByCondition(SearchPostRequestDto requestDto, Pageable pageable) {
        return getQuerydsl().applyPagination(pageable,queryFactory.select(Projections.constructor(PostListResponseDto.class,
                post.id, post.title, post.nickname,post.area,post.category,post.content,post.type,post.postDate))
                .from(post)
                .join(post.user)
                .where(createPredicate(requestDto))
                .orderBy(post.postDate.desc())
        ).fetch();
    }

    private Predicate createPredicate(SearchPostRequestDto requestDto) { // 8
        return new BooleanBuilder()
                .and(orConditionsByContainCategory(requestDto.getCategory()))
                .and(orConditionsByContainArea(requestDto.getArea()))
                .and(containContent(requestDto.getKeyword()))
                .and(containTitle(requestDto.getKeyword()))
                .and(eqType(requestDto.getType()));
    }

    private Predicate orConditionsByContainCategory(String request) { // 9
        if (StringUtils.isNullOrEmpty(request)) {
            return null;
        }
        List<String> category= Arrays.asList(request.split("#"));
        return orConditions(category, post.category::contains);
    }

    private Predicate orConditionsByContainArea(String request) { // 9
        if (StringUtils.isNullOrEmpty(request)) {
            return null;
        }
        List<String> area= Arrays.asList(request.split("#"));
        return orConditions(area, post.area::contains);
    }

    private <T> Predicate orConditions(List<T> values, Function<T, BooleanExpression> term) { // 11
        return values.stream()
                .map(term)
                .reduce(BooleanExpression::or)
                .orElse(null);
    }

    private Predicate containTitle(String request) {
        if (StringUtils.isNullOrEmpty(request)) {
            return null;
        }
        return post.title.contains(request);
    }

    private Predicate containContent(String request) {
        if (StringUtils.isNullOrEmpty(request)) {
            return null;
        }
        return post.content.contains(request);
    }

    private Predicate eqType(String request) {
        if (StringUtils.isNullOrEmpty(request)) {
            return null;
        }
        if(request.equals(Type.coach.toString()))
            return post.type.eq(Type.coach);
        if(request.equals(Type.player.toString()))
            return post.type.eq(Type.player);
        else return null;
    }
}
