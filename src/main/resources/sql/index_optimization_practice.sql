EXPLAIN format = traditional
select
    p1_0.content,
    cast(count(distinct c1_0.id) as signed)
from
    posts p1_0
        left join
    users u1_0
    on p1_0.user_id=u1_0.id
        left join
    comments c1_0
    on c1_0.post_id=p1_0.id
where
    u1_0.username=?
group by
    p1_0.id;

# 인덱스 추가해 성능 개선
CREATE INDEX idx_posts_user_id ON posts(user_id);
CREATE INDEX idx_comments_post_id ON comments(post_id);