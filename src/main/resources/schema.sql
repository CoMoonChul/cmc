CREATE TABLE IF NOT EXISTS User (
    user_num BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    refresh_token VARCHAR(2048),
    username VARCHAR(50) NOT NULL,
    email VARCHAR(50),
    user_role VARCHAR(10),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS GroupTable (
    group_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    group_name VARCHAR(50) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS GroupMember (
    group_member_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    group_id BIGINT NOT NULL,
    user_num BIGINT NOT NULL,
    group_role VARCHAR(10),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_group FOREIGN KEY (group_id) REFERENCES GroupTable(group_id),
    CONSTRAINT fk_user FOREIGN KEY (user_num) REFERENCES User(user_num),
    UNIQUE KEY unique_group_user (user_num, group_id)
);

CREATE TABLE IF NOT EXISTS Comment (
    comment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content TEXT NOT NULL,
    user_num BIGINT,
    target_id BIGINT NOT NULL,
    comment_target TINYINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_comment_user FOREIGN KEY (user_num) REFERENCES User(user_num)
);

CREATE TABLE IF NOT EXISTS Review (
    review_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_num BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_review_user FOREIGN KEY (user_num) REFERENCES User(user_num)
);

CREATE TABLE IF NOT EXISTS ReviewLike (
    user_num BIGINT NOT NULL,
    review_id BIGINT NOT NULL,
    liked_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_num, review_id),
    CONSTRAINT fk_review_like_user FOREIGN KEY (user_num) REFERENCES User(user_num),
    CONSTRAINT fk_review_like_review FOREIGN KEY (review_id) REFERENCES Review(review_id)
);

CREATE TABLE IF NOT EXISTS ReviewView (
    review_id BIGINT UNIQUE NOT NULL,
    view_count BIGINT DEFAULT 0 NOT NULL,
    CONSTRAINT fk_review_view FOREIGN KEY (review_id) REFERENCES Review(review_id)
);

CREATE TABLE IF NOT EXISTS CodeEditor (
    code_edit_num BIGINT AUTO_INCREMENT PRIMARY KEY,
    content LONGTEXT,
    language VARCHAR(20),
    user_num BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_code_editor_user FOREIGN KEY (user_num) REFERENCES User(user_num)
);

CREATE TABLE IF NOT EXISTS Battle (
    battle_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_num BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    content VARCHAR(3000) NOT NULL,
    code_1 BIGINT NOT NULL,
    code_2 BIGINT NOT NULL,
    end_time DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_battle_user FOREIGN KEY (user_num) REFERENCES User(user_num),
    CONSTRAINT fk_battle_code_1 FOREIGN KEY (code_1) REFERENCES CodeEditor(code_edit_num),
    CONSTRAINT fk_battle_code_2 FOREIGN KEY (code_2) REFERENCES CodeEditor(code_edit_num)
);

CREATE TABLE IF NOT EXISTS BattleView (
    battle_id BIGINT UNIQUE NOT NULL,
    view_count BIGINT DEFAULT 0 NOT NULL,
    CONSTRAINT fk_battle_view FOREIGN KEY (battle_id) REFERENCES Battle(battle_id)
);

CREATE TABLE IF NOT EXISTS NotificationTemplate (
    noti_template_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    noti_template_nm VARCHAR(100),
    noti_title VARCHAR(100),
    noti_content VARCHAR(1000),
    noti_type CHAR(10) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    create_user BIGINT,
    CONSTRAINT fk_noti_template_user FOREIGN KEY (create_user) REFERENCES User(user_num)
);

CREATE TABLE IF NOT EXISTS Notification (
    noti_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_num BIGINT NOT NULL,
    noti_template_id BIGINT NOT NULL,
    send_at DATETIME,
    send_state CHAR(1) DEFAULT 'N' NOT NULL,
    link_url VARCHAR(50),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    create_user BIGINT NOT NULL,
    CONSTRAINT fk_noti_user FOREIGN KEY (user_num) REFERENCES User(user_num),
    CONSTRAINT fk_noti_template FOREIGN KEY (noti_template_id) REFERENCES NotificationTemplate(noti_template_id),
    CONSTRAINT fk_noti_create_user FOREIGN KEY (create_user) REFERENCES User(user_num)
);

CREATE TABLE IF NOT EXISTS Tag (
    tag_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tag_name VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS ReviewTagRelation (
    relation_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    review_id BIGINT,
    tag_id BIGINT,
    CONSTRAINT fk_review_tag_review FOREIGN KEY (review_id) REFERENCES Review(review_id),
    CONSTRAINT fk_review_tag_tag FOREIGN KEY (tag_id) REFERENCES Tag(tag_id)
);

CREATE TABLE IF NOT EXISTS Vote (
    vote_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    battle_id BIGINT NOT NULL,
    user_num BIGINT,
    vote_value TINYINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_vote_battle FOREIGN KEY (battle_id) REFERENCES Battle(battle_id),
    CONSTRAINT fk_vote_user FOREIGN KEY (user_num) REFERENCES User(user_num)
);

-- ReviewLike 테이블의 user_num, review_id 복합 인덱스
SELECT COUNT(1) INTO @idxExists FROM information_schema.statistics
WHERE table_schema = DATABASE() AND table_name = 'ReviewLike' AND index_name = 'idx_review_like';
SET @createIndex = IF(@idxExists = 0, 'CREATE INDEX idx_review_like ON ReviewLike(user_num, review_id)', 'SELECT "Index already exists";');
PREPARE stmt FROM @createIndex;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Notification 테이블의 user_num 인덱스
SELECT COUNT(1) INTO @idxExists FROM information_schema.statistics
WHERE table_schema = DATABASE() AND table_name = 'Notification' AND index_name = 'idx_notification_user';
SET @createIndex = IF(@idxExists = 0, 'CREATE INDEX idx_notification_user ON Notification(user_num)', 'SELECT "Index already exists";');
PREPARE stmt FROM @createIndex;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Notification 테이블의 send_state 인덱스
SELECT COUNT(1) INTO @idxExists FROM information_schema.statistics
WHERE table_schema = DATABASE() AND table_name = 'Notification' AND index_name = 'idx_notification_state';
SET @createIndex = IF(@idxExists = 0, 'CREATE INDEX idx_notification_state ON Notification(send_state)', 'SELECT "Index already exists";');
PREPARE stmt FROM @createIndex;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Review 테이블의 user_num 인덱스
SELECT COUNT(1) INTO @idxExists FROM information_schema.statistics
WHERE table_schema = DATABASE() AND table_name = 'Review' AND index_name = 'idx_review_user';
SET @createIndex = IF(@idxExists = 0, 'CREATE INDEX idx_review_user ON Review(user_num)', 'SELECT "Index already exists";');
PREPARE stmt FROM @createIndex;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Battle 테이블의 created_at 인덱스
SELECT COUNT(1) INTO @idxExists FROM information_schema.statistics
WHERE table_schema = DATABASE() AND table_name = 'Battle' AND index_name = 'idx_battle_created';
SET @createIndex = IF(@idxExists = 0, 'CREATE INDEX idx_battle_created ON Battle(created_at)', 'SELECT "Index already exists";');
PREPARE stmt FROM @createIndex;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Vote 테이블의 battle_id 인덱스
SELECT COUNT(1) INTO @idxExists FROM information_schema.statistics
WHERE table_schema = DATABASE() AND table_name = 'Vote' AND index_name = 'idx_vote_battle';
SET @createIndex = IF(@idxExists = 0, 'CREATE INDEX idx_vote_battle ON Vote(battle_id)', 'SELECT "Index already exists";');
PREPARE stmt FROM @createIndex;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- GroupMember 테이블의 user_num, group_id 복합 인덱스
SELECT COUNT(1) INTO @idxExists FROM information_schema.statistics
WHERE table_schema = DATABASE() AND table_name = 'GroupMember' AND index_name = 'idx_group_member';
SET @createIndex = IF(@idxExists = 0, 'CREATE INDEX idx_group_member ON GroupMember(user_num, group_id)', 'SELECT "Index already exists";');
PREPARE stmt FROM @createIndex;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

---- Vote 테이블의 user_num 인덱스 (유저별 투표 내역 조회 속도 개선)
--CREATE INDEX idx_vote_user ON Vote (user_num);

---- Battle 테이블의 user_num 인덱스
--CREATE INDEX idx_battle_user ON Battle (user_num);

---- Review 테이블의 created_at 인덱스 (최신순 정렬 속도 개선)
--CREATE INDEX idx_review_created ON Review (created_at);
