-- 관리자 생성
INSERT INTO users (login_id, password, email, phone, role) VALUES ('admin', '1234', 'admin@example.com', '010-0000-0001', 'ROLE_ADMIN');

-- 매니저 생성 및 식당 정보와 연결
INSERT INTO users (login_id, password, email, phone, role) VALUES ('manager1', '1234', 'manager1@example.com', '010-0000-0002', 'ROLE_MANAGER');
INSERT INTO users (login_id, password, email, phone, role) VALUES ('manager2', '1234', 'manager2@example.com', '010-0000-0003', 'ROLE_MANAGER');

-- MySQL의 LAST_INSERT_ID() 함수를 사용하여 가장 최근에 삽입된 ID를 가져오는 방법 예시입니다.
-- 사용하기 전에 MySQL 버전과 환경이 이 기능을 지원하는지 확인해야 합니다.
SET @manager1Id = LAST_INSERT_ID() - 1; -- 이전 ID를 가져오기 위해
SET @manager2Id = LAST_INSERT_ID();

-- 식당 정보 삽입
INSERT INTO restaurant (name, address, phone, restaurant_table, logo, recognize, user_id) VALUES ('한식당', '서울시 강남구 역삼동', '02-123-4567', 20, 'https://ganju-test.s3.ap-northeast-2.amazonaws.com/2/Group+16.png', 1, @manager1Id);
INSERT INTO restaurant (name, address, phone, restaurant_table, logo, recognize, user_id) VALUES ('중식당', '서울시 서초구 반포동', '02-765-4321', 15, 'https://ganju-test.s3.ap-northeast-2.amazonaws.com/3/484c569b-b2973c8ec-7profile.png', 0, @manager2Id);
SET @restaurantId1 = LAST_INSERT_ID() - 1; -- 이전 ID를 가져오기 위해
SET @restaurantId2 = LAST_INSERT_ID();

-- 카테고리 추가
INSERT INTO category (name, turn, restaurant_id) VALUES ('밥류', 1, @restaurantId1);
INSERT INTO category (name, turn, restaurant_id) VALUES ('국류', 2, @restaurantId1);
INSERT INTO category (name, turn, restaurant_id) VALUES ('음료', 3, @restaurantId1);
INSERT INTO category (name, turn, restaurant_id) VALUES ('반찬', 4, @restaurantId1);
INSERT INTO category (name, turn, restaurant_id) VALUES ('요리류', 5, @restaurantId1);

SET @categoryId1 = LAST_INSERT_ID() - 4;
SET @categoryId2 = LAST_INSERT_ID() - 3;
SET @categoryId3 = LAST_INSERT_ID() - 2;
SET @categoryId4 = LAST_INSERT_ID() - 1;
SET @categoryId5 = LAST_INSERT_ID();

-- '밥류' 카테고리에 메뉴 추가
INSERT INTO menu (name, price, category_id, menu_image, info, restaurant_id) VALUES
('쌀밥', 20, @categoryId1, 'ssalbap.png', '찰진 흰밥', @restaurantId1),
('콩밥', 30, @categoryId1, 'kongbap.png', '영양 가득 콩밥', @restaurantId1),
('보리밥', 50, @categoryId1, 'boribap.png', '영양 가득 보리밥', @restaurantId1),
('흑미밥', 60, @categoryId1, 'heukmibap.png', '구수한 흑미밥', @restaurantId1);

-- '국류' 카테고리에 메뉴 추가
INSERT INTO menu (name, price, category_id, menu_image, info, restaurant_id) VALUES
('김치찌개', 70, @categoryId2, 'kimchijjigae.png', '매콤하고 깊은 맛의 김치찌개', @restaurantId1),
('된장찌개', 80, @categoryId2, 'doenjangjjigae.png', '구수한 된장의 풍미가 가득한 된장찌개', @restaurantId1),
('콩나물국', 90, @categoryId2, 'kongnamulguk.png', '시원하고 깔끔한 맛의 콩나물국', @restaurantId1),
('육개장', 90, @categoryId2, 'yukgaejang.png', '얼큰하고 진한 맛의 육개장', @restaurantId1),
('미역국', 80, @categoryId2, 'miyeokguk.png', '영양 가득 미역국', @restaurantId1);

-- '주류' 카테고리에 메뉴 추가
INSERT INTO menu (name, price, category_id, menu_image, info, restaurant_id) VALUES
('콜라', 20, @categoryId3, 'cola.png', '시원하게 제공되는 콜라', @restaurantId1),
('사이다', 20, @categoryId3, 'cider.png', '청량한 사이다', @restaurantId1),
('환타', 20, @categoryId3, 'fanta.png', '달콤한 오렌지맛 환타', @restaurantId1),
('소주', 40, @categoryId3, 'soju.png', '깔끔하고 시원한 소주', @restaurantId1),
('맥주', 50, @categoryId3, 'beer.png', '상쾌한 맥주', @restaurantId1);

-- '반찬' 카테고리에 메뉴 추가
INSERT INTO menu (name, price, category_id, menu_image, info, restaurant_id) VALUES
('메밀전병', 20, @categoryId4, 'memiljeonbyeong.png', '고소한 메밀전병', @restaurantId1),
('계란말이', 20, @categoryId4, 'eggroll.png', '부드러운 계란말이', @restaurantId1),
('강된장', 20, @categoryId4, 'strongdoenjang.png', '집에서 만든 강된장', @restaurantId1),
('장조림', 30, @categoryId4, 'jangjorim.png', '달콤 짭짤한 장조림', @restaurantId1);

-- '요리류' 카테고리에 메뉴 추가
INSERT INTO menu (name, price, category_id, menu_image, info, restaurant_id) VALUES
('불고기', 90, @categoryId5, 'bulgogi.png', '달콤하고 육즙 가득한 불고기', @restaurantId1),
('제육볶음', 100, @categoryId5, 'jeyukbokkeum.png', '매콤 달콤한 제육볶음', @restaurantId1),
('신선로', 300, @categoryId5, 'shinseonro.png', '다양한 재료가 가득한 신선로', @restaurantId1),
('고등어조림', 150, @categoryId5, 'mackereljorim.png', '양념이 잘 배인 고등어조림', @restaurantId1),
('삼겹살', 120, @categoryId5, 'samgyeopsal.png', '쫄깃한 삼겹살', @restaurantId1);

-- 메뉴 옵션 추가 예시 (김치찌개에 대한 옵션)
INSERT INTO menu_option (content, menu_id, menu_option_id) VALUES
('기본', (SELECT id FROM menu WHERE name = '김치찌개' AND restaurant_id = @restaurantId1), 'REQUIRED'),
('맵기', (SELECT id FROM menu WHERE name = '김치찌개' AND restaurant_id = @restaurantId1), 'REQUIRED'),
('추가선택', (SELECT id FROM menu WHERE name = '김치찌개' AND restaurant_id = @restaurantId1), 'OPTIONAL');

INSERT INTO menu_option_value (content, menu_option_id, price) VALUES
('기본', (SELECT id FROM menu_option WHERE content = '기본' AND menu_id = (SELECT id FROM menu WHERE name = '김치찌개')), 0),
('곱빼기', (SELECT id FROM menu_option WHERE content = '기본' AND menu_id = (SELECT id FROM menu WHERE name = '김치찌개')), 20);

INSERT INTO menu_option_value (content, menu_option_id, price) VALUES
('덜맵게', (SELECT id FROM menu_option WHERE content = '맵기' AND menu_id = (SELECT id FROM menu WHERE name = '김치찌개')), -10),
('기본', (SELECT id FROM menu_option WHERE content = '맵기' AND menu_id = (SELECT id FROM menu WHERE name = '김치찌개')), 0),
('더맵게', (SELECT id FROM menu_option WHERE content = '맵기' AND menu_id = (SELECT id FROM menu WHERE name = '김치찌개')), 0);

INSERT INTO menu_option_value (content, menu_option_id, price) VALUES
('고기추가', (SELECT id FROM menu_option WHERE content = '추가선택' AND menu_id = (SELECT id FROM menu WHERE name = '김치찌개')), 30),
('참치추가', (SELECT id FROM menu_option WHERE content = '추가선택' AND menu_id = (SELECT id FROM menu WHERE name = '김치찌개')), 20),
('만두사리', (SELECT id FROM menu_option WHERE content = '추가선택' AND menu_id = (SELECT id FROM menu WHERE name = '김치찌개')), 15);

-- 메뉴 옵션 추가 예시 (된장찌개에 대한 옵션)
INSERT INTO menu_option (content, menu_id, menu_option_id) VALUES
('기본', (SELECT id FROM menu WHERE name = '된장찌개' AND restaurant_id = @restaurantId1), 'REQUIRED'),
('맵기', (SELECT id FROM menu WHERE name = '된장찌개' AND restaurant_id = @restaurantId1), 'REQUIRED'),
('추가선택', (SELECT id FROM menu WHERE name = '된장찌개' AND restaurant_id = @restaurantId1), 'OPTIONAL');

INSERT INTO menu_option_value (content, menu_option_id, price) VALUES
('기본', (SELECT id FROM menu_option WHERE content = '기본' AND menu_id = (SELECT id FROM menu WHERE name = '된장찌개')), 0),
('곱빼기', (SELECT id FROM menu_option WHERE content = '기본' AND menu_id = (SELECT id FROM menu WHERE name = '된장찌개')), 20);

INSERT INTO menu_option_value (content, menu_option_id, price) VALUES
('덜맵게', (SELECT id FROM menu_option WHERE content = '맵기' AND menu_id = (SELECT id FROM menu WHERE name = '된장찌개')), 0),
('기본', (SELECT id FROM menu_option WHERE content = '맵기' AND menu_id = (SELECT id FROM menu WHERE name = '된장찌개')), 0),
('더맵게', (SELECT id FROM menu_option WHERE content = '맵기' AND menu_id = (SELECT id FROM menu WHERE name = '된장찌개')), 0);

INSERT INTO menu_option_value (content, menu_option_id, price) VALUES
('고기추가', (SELECT id FROM menu_option WHERE content = '추가선택' AND menu_id = (SELECT id FROM menu WHERE name = '된장찌개')), 30),
('참치추가', (SELECT id FROM menu_option WHERE content = '추가선택' AND menu_id = (SELECT id FROM menu WHERE name = '된장찌개')), 20),
('만두사리', (SELECT id FROM menu_option WHERE content = '추가선택' AND menu_id = (SELECT id FROM menu WHERE name = '된장찌개')), 15);

-- 메뉴 옵션 추가 예시 (콩나물국에 대한 옵션)
INSERT INTO menu_option (content, menu_id, menu_option_id) VALUES
('기본', (SELECT id FROM menu WHERE name = '콩나물국' AND restaurant_id = @restaurantId1), 'REQUIRED'),
('맵기', (SELECT id FROM menu WHERE name = '콩나물국' AND restaurant_id = @restaurantId1), 'REQUIRED');

INSERT INTO menu_option_value (content, menu_option_id, price) VALUES
('기본', (SELECT id FROM menu_option WHERE content = '기본' AND menu_id = (SELECT id FROM menu WHERE name = '콩나물국')), 0),
('곱빼기', (SELECT id FROM menu_option WHERE content = '기본' AND menu_id = (SELECT id FROM menu WHERE name = '콩나물국')), 20);

INSERT INTO menu_option_value (content, menu_option_id, price) VALUES
('덜맵게', (SELECT id FROM menu_option WHERE content = '맵기' AND menu_id = (SELECT id FROM menu WHERE name = '콩나물국')), 0),
('기본', (SELECT id FROM menu_option WHERE content = '맵기' AND menu_id = (SELECT id FROM menu WHERE name = '콩나물국')), 0),
('더맵게', (SELECT id FROM menu_option WHERE content = '맵기' AND menu_id = (SELECT id FROM menu WHERE name = '콩나물국')), 0);

-- 메뉴 옵션 추가 예시 (육개장에 대한 옵션)
INSERT INTO menu_option (content, menu_id, menu_option_id) VALUES
('기본', (SELECT id FROM menu WHERE name = '육개장' AND restaurant_id = @restaurantId1), 'REQUIRED'),
('맵기', (SELECT id FROM menu WHERE name = '육개장' AND restaurant_id = @restaurantId1), 'REQUIRED'),
('추가선택', (SELECT id FROM menu WHERE name = '육개장' AND restaurant_id = @restaurantId1), 'OPTIONAL');

INSERT INTO menu_option_value (content, menu_option_id, price) VALUES
('기본', (SELECT id FROM menu_option WHERE content = '기본' AND menu_id = (SELECT id FROM menu WHERE name = '육개장')), 0),
('곱빼기', (SELECT id FROM menu_option WHERE content = '기본' AND menu_id = (SELECT id FROM menu WHERE name = '육개장')), 20);

INSERT INTO menu_option_value (content, menu_option_id, price) VALUES
('덜맵게', (SELECT id FROM menu_option WHERE content = '맵기' AND menu_id = (SELECT id FROM menu WHERE name = '육개장')), 0),
('기본', (SELECT id FROM menu_option WHERE content = '맵기' AND menu_id = (SELECT id FROM menu WHERE name = '육개장')), 0),
('더맵게', (SELECT id FROM menu_option WHERE content = '맵기' AND menu_id = (SELECT id FROM menu WHERE name = '육개장')), 0);

INSERT INTO menu_option_value (content, menu_option_id, price) VALUES
('고기추가', (SELECT id FROM menu_option WHERE content = '추가선택' AND menu_id = (SELECT id FROM menu WHERE name = '육개장')), 30),
('참치추가', (SELECT id FROM menu_option WHERE content = '추가선택' AND menu_id = (SELECT id FROM menu WHERE name = '육개장')), 20),
('라면사리', (SELECT id FROM menu_option WHERE content = '추가선택' AND menu_id = (SELECT id FROM menu WHERE name = '육개장')), 15);

-- 메뉴 옵션 추가 예시 (미역국에 대한 옵션)
INSERT INTO menu_option (content, menu_id, menu_option_id) VALUES
('기본', (SELECT id FROM menu WHERE name = '미역국' AND restaurant_id = @restaurantId1), 'REQUIRED'),
('추가선택', (SELECT id FROM menu WHERE name = '미역국' AND restaurant_id = @restaurantId1), 'OPTIONAL');

INSERT INTO menu_option_value (content, menu_option_id, price) VALUES
('기본', (SELECT id FROM menu_option WHERE content = '기본' AND menu_id = (SELECT id FROM menu WHERE name = '미역국')), 0),
('곱빼기', (SELECT id FROM menu_option WHERE content = '기본' AND menu_id = (SELECT id FROM menu WHERE name = '미역국')), 20);

INSERT INTO menu_option_value (content, menu_option_id, price) VALUES
('고기추가', (SELECT id FROM menu_option WHERE content = '추가선택' AND menu_id = (SELECT id FROM menu WHERE name = '미역국')), 30);

-- 주류 카테고리의 음료 사이즈 옵션 추가
INSERT INTO menu_option (content, menu_id, menu_option_id) VALUES
('사이즈', (SELECT id FROM menu WHERE name = '콜라' AND restaurant_id = @restaurantId1), 'REQUIRED'),
('사이즈', (SELECT id FROM menu WHERE name = '사이다' AND restaurant_id = @restaurantId1), 'REQUIRED'),
('사이즈', (SELECT id FROM menu WHERE name = '환타' AND restaurant_id = @restaurantId1), 'REQUIRED');

-- 주류 카테고리의 음료 사이즈 옵션 값 추가
INSERT INTO menu_option_value (content, menu_option_id, price) VALUES
('500ml', (SELECT id FROM menu_option WHERE content = '사이즈' AND menu_id = (SELECT id FROM menu WHERE name = '콜라')), 0),
('1.25L', (SELECT id FROM menu_option WHERE content = '사이즈' AND menu_id = (SELECT id FROM menu WHERE name = '콜라')), 10),
('500ml', (SELECT id FROM menu_option WHERE content = '사이즈' AND menu_id = (SELECT id FROM menu WHERE name = '사이다')), 0),
('1.25L', (SELECT id FROM menu_option WHERE content = '사이즈' AND menu_id = (SELECT id FROM menu WHERE name = '사이다')), 10),
('500ml', (SELECT id FROM menu_option WHERE content = '사이즈' AND menu_id = (SELECT id FROM menu WHERE name = '환타')), 0),
('1.25L', (SELECT id FROM menu_option WHERE content = '사이즈' AND menu_id = (SELECT id FROM menu WHERE name = '환타')), 10);

-- 요리류 카테고리의 메뉴에 대한 기본, 맵기, 추가선택 옵션 추가
-- '불고기' 메뉴에 대한 예시
INSERT INTO menu_option (content, menu_id, menu_option_id) VALUES
('기본', (SELECT id FROM menu WHERE name = '불고기' AND restaurant_id = @restaurantId1), 'REQUIRED'),
('추가선택', (SELECT id FROM menu WHERE name = '불고기' AND restaurant_id = @restaurantId1), 'OPTIONAL');

-- '불고기' 메뉴의 기본 옵션 값 추가
INSERT INTO menu_option_value (content, menu_option_id, price) VALUES
('기본', (SELECT id FROM menu_option WHERE content = '기본' AND menu_id = (SELECT id FROM menu WHERE name = '불고기')), 0),
('곱빼기', (SELECT id FROM menu_option WHERE content = '기본' AND menu_id = (SELECT id FROM menu WHERE name = '불고기')), 10);

-- '불고기' 메뉴의 추가선택 옵션 값 추가
INSERT INTO menu_option_value (content, menu_option_id, price) VALUES
('고기 추가', (SELECT id FROM menu_option WHERE content = '추가선택' AND menu_id = (SELECT id FROM menu WHERE name = '불고기')), 30),
('버섯 추가', (SELECT id FROM menu_option WHERE content = '추가선택' AND menu_id = (SELECT id FROM menu WHERE name = '불고기')), 15);


-- '제육볶음' 메뉴에 대한 예시
INSERT INTO menu_option (content, menu_id, menu_option_id) VALUES
('기본', (SELECT id FROM menu WHERE name = '제육볶음' AND restaurant_id = @restaurantId1), 'REQUIRED'),
('추가선택', (SELECT id FROM menu WHERE name = '제육볶음' AND restaurant_id = @restaurantId1), 'OPTIONAL');

-- '제육볶음' 메뉴의 기본 옵션 값 추가
INSERT INTO menu_option_value (content, menu_option_id, price) VALUES
('기본', (SELECT id FROM menu_option WHERE content = '기본' AND menu_id = (SELECT id FROM menu WHERE name = '제육볶음')), 0),
('곱빼기', (SELECT id FROM menu_option WHERE content = '기본' AND menu_id = (SELECT id FROM menu WHERE name = '제육볶음')), 10);

-- '제육볶음' 메뉴의 추가선택 옵션 값 추가
INSERT INTO menu_option_value (content, menu_option_id, price) VALUES
('고기 추가', (SELECT id FROM menu_option WHERE content = '추가선택' AND menu_id = (SELECT id FROM menu WHERE name = '제육볶음')), 30),
('버섯 추가', (SELECT id FROM menu_option WHERE content = '추가선택' AND menu_id = (SELECT id FROM menu WHERE name = '제육볶음')), 15);

-- '고등어조림' 메뉴에 대한 예시
INSERT INTO menu_option (content, menu_id, menu_option_id) VALUES
('기본', (SELECT id FROM menu WHERE name = '고등어조림' AND restaurant_id = @restaurantId1), 'REQUIRED');

-- '고등어조림' 메뉴의 기본 옵션 값 추가
INSERT INTO menu_option_value (content, menu_option_id, price) VALUES
('기본', (SELECT id FROM menu_option WHERE content = '기본' AND menu_id = (SELECT id FROM menu WHERE name = '고등어조림')), 0),
('곱빼기', (SELECT id FROM menu_option WHERE content = '기본' AND menu_id = (SELECT id FROM menu WHERE name = '고등어조림')), 30);

-- '삼겹살' 메뉴에 대한 예시
INSERT INTO menu_option (content, menu_id, menu_option_id) VALUES
('기본', (SELECT id FROM menu WHERE name = '삼겹살' AND restaurant_id = @restaurantId1), 'REQUIRED');

-- '삼겹살' 메뉴의 기본 옵션 값 추가
INSERT INTO menu_option_value (content, menu_option_id, price) VALUES
('기본', (SELECT id FROM menu_option WHERE content = '기본' AND menu_id = (SELECT id FROM menu WHERE name = '삼겹살')), 0),
('고기추가(500g)', (SELECT id FROM menu_option WHERE content = '기본' AND menu_id = (SELECT id FROM menu WHERE name = '삼겹살')), 50);

-- 공지사항 추가 (10개)
INSERT INTO board (name, title, content, reg_date, board_category, restaurant_id)
VALUES
    ('운영자', '추석 연휴 휴무 안내', '추석 연휴 기간 동안 휴무합니다. 가족들과 즐거운 시간 보내세요.', '2020-09-15', 'NOTICE', @restaurantId1),
    ('운영자', '새로운 메뉴 출시 안내', '다양한 신메뉴가 출시되었습니다. 많은 이용 바랍니다.', '2021-03-22', 'NOTICE', @restaurantId1),
    ('운영자', '영업 시간 변경 안내', '영업 시간이 변경되었습니다. 변경된 시간을 확인해주세요.', '2021-07-01', 'NOTICE', @restaurantId1),
    ('운영자', '리모델링 공사 안내', '더 나은 환경을 제공하기 위해 리모델링 공사를 진행합니다.', '2022-01-10', 'NOTICE', @restaurantId1),
    ('운영자', '할인 이벤트 안내', '이번 주말 특별 할인 이벤트를 진행합니다. 많은 참여 바랍니다.', '2022-06-20', 'NOTICE', @restaurantId1),
    ('운영자', '신규 직원 채용 안내', '함께할 새로운 직원을 채용합니다. 관심 있으신 분들의 많은 지원 바랍니다.', '2022-10-05', 'NOTICE', @restaurantId1),
    ('운영자', '고객 감사 이벤트', '고객님들의 성원에 감사드리며, 특별 이벤트를 준비했습니다.', '2023-02-14', 'NOTICE', @restaurantId1),
    ('운영자', '위생 관리 강화 안내', '위생 관리를 더욱 강화하여 안전한 식사 환경을 제공하겠습니다.', '2023-05-29', 'NOTICE', @restaurantId1),
    ('운영자', '메뉴 가격 조정 안내', '원자재 가격 상승으로 일부 메뉴의 가격이 조정됩니다.', '2023-08-23', 'NOTICE', @restaurantId1),
    ('운영자', '정기 점검 일정 안내', '더 나은 서비스 제공을 위한 정기 점검을 실시합니다.', '2023-12-01', 'NOTICE', @restaurantId1);

-- 신고 추가
INSERT INTO board (title, content, reg_date, board_category, restaurant_id)
VALUES
    ('', '위생 불량으로 신고합니다', '2020-09-15', 'REPORT', @restaurantId1),
    ('', '직원 태도 불량으로 신고합니다', '2020-09-15', 'REPORT', @restaurantId1),
    ('', '음식이 너무 맛이 없습니다', '2020-09-15', 'REPORT', @restaurantId1),
    ('', '허위신고', '2020-09-15', 'REPORT', @restaurantId1);

-- 문의 추가
INSERT INTO board (title, content, reg_date, board_category, restaurant_id)
VALUES
    ('문의 드립니다', '무분별한 손님의 신고로 인해 가게의 피해가 많습니다. 신고 시스템 개편을 요구합니다.', '2020-09-15', 'QUESTION', @restaurantId1),
    ('문의', '메뉴는 어디서 추가할 수 있나요? 아무리 찾아도 보이질 않네요', '2020-09-15', 'QUESTION', @restaurantId1),
    ('질문 있습니다', '주문이 들어왔을 대 어디서 확인할 수 있나요?', '2020-09-15', 'QUESTION', @restaurantId2),
    ('답변 부탁드립니다', '허위신고가 많이 접수되고 있습니다. 제대로 확인을 하고 처리해주세요.', '2020-09-15', 'QUESTION', @restaurantId1);

-- Orders 테이블에 새로운 데이터 추가
INSERT INTO orders (restaurant_table_no, restaurant_id, price, reg_date, content, uid, division)
VALUES
    (5, 1, 32000, '2024-04-18 14:00:00', '스테이크, 맥주 주문', 'user5', 'WAIT'),
    (6, 2, 45000, '2024-04-18 18:30:00', '짜장면, 탕수육, 맥주 주문', 'user6', 'WAIT'),
    (7, 1, 29000, '2024-04-18 17:00:00', '피자, 샐러드, 콜라 주문', 'user7', 'WAIT'),
    (8, 2, 38000, '2024-04-19 08:30:00', '파스타, 스프, 와인 주문', 'user8', 'WAIT'),
    (9, 1, 35000, '2024-04-19 09:00:00', '스테이크, 맥주, 감자튀김 주문', 'user9', 'WAIT'),
    (10, 2, 42000, '2024-04-19 10:30:00', '짜장면, 짬뽕, 맥주, 탕수육 주문', 'user10', 'WAIT'),
    (11, 1, 28000, '2024-04-10 14:00:00', '스파게티, 샐러드 주문', 'user11', 'WAIT'),
    (12, 2, 41000, '2024-04-11 15:30:00', '피자, 맥주, 스프 주문', 'user12', 'WAIT'),
    (13, 1, 34000, '2024-04-12 17:00:00', '스테이크, 와인 주문', 'user13', 'WAIT'),
    (14, 2, 39000, '2024-04-13 18:30:00', '짜장면, 짬뽕, 맥주 주문', 'user14', 'WAIT'),
    (15, 1, 32000, '2024-04-14 20:00:00', '피자, 감자튀김 주문', 'user15', 'WAIT'),
    (16, 2, 43000, '2024-04-15 21:30:00', '파스타, 샐러드, 와인 주문', 'user16', 'WAIT'),
    (17, 1, 30000, '2024-04-16 14:00:00', '짜장면, 탕수육, 콜라 주문', 'user17', 'WAIT'),
    (18, 2, 42000, '2024-04-17 15:30:00', '그릴드 스테이크, 맥주, 감자튀김 주문', 'user18', 'WAIT');

-- OrderMenu 테이블 더미 데이터
INSERT INTO order_menu (menu_name, quantity, price, orders_id)
VALUES
    ('그릴드 스테이크', 1, 25000, 1),
    ('그린 샐러드', 1, 5000, 1),
    ('클래식 피자', 1, 18000, 3),
    ('시저 샐러드', 1, 5000, 3),
    ('크림 파스타', 1, 20000, 4),
    ('토마토 스프', 1, 6000, 4),
    ('그릴드 스테이크', 1, 23000, 5),
    ('감자 튀김', 1, 3000, 5),
    ('짜장면', 1, 12000, 6),
    ('짬뽕', 1, 15000, 6),
    ('탕수육', 1, 18000, 6),
    ('스파게티', 1, 15000, 7),
    ('그린 샐러드', 1, 5000, 7),
    ('클래식 피자', 1, 18000, 8),
    ('토마토 스프', 1, 6000, 8),
    ('그릴드 스테이크', 1, 23000, 9),
    ('와인', 1, 11000, 9),
    ('짜장면', 1, 12000, 10),
    ('짬뽕', 1, 15000, 10),
    ('피자', 1, 16000, 11),
    ('감자 튀김', 1, 3000, 11),
    ('크림 파스타', 1, 20000, 12),
    ('그린 샐러드', 1, 5000, 12),
    ('짜장면', 1, 12000, 13),
    ('탕수육', 1, 18000, 13),
    ('그릴드 스테이크', 1, 25000, 14),
    ('감자 튀김', 1, 3000, 14);

-- OrderOption 테이블 더미 데이터
INSERT INTO order_option (option_name, price, order_menu_id)
VALUES
    ('감자 튀김 추가', 3000, 1),
    ('소스 변경', 1000, 1),
    ('소스 추가', 500, 2),
    ('샐러드 소스 변경', 800, 2),
    ('감자 튀김 추가', 3000, 3),
    ('소스 변경', 1000, 3),
    ('소스 추가', 500, 4),
    ('샐러드 소스 변경', 800, 4),
    ('샐러드 소스 변경', 800, 5),
    ('소스 추가', 500, 6),
    ('소스 변경', 1000, 7),
    ('감자 튀김 추가', 3000, 8),
    ('소스 추가', 500, 9),
    ('샐러드 소스 변경', 800, 10),
    ('소스 추가', 500, 11),
    ('감자 튀김 추가', 3000, 11),
    ('소스 변경', 1000, 12),
    ('샐러드 소스 변경', 800, 13),
    ('소스 추가', 500, 14),
    ('샐러드 소스 변경', 800, 14),
    ('소스 변경', 1000, 15),
    ('감자 튀김 추가', 3000, 15);

-- 호출 추가
INSERT INTO orders (restaurant_table_no, price, reg_date, restaurant_id, content, division)
VALUES
    (1, 0, '2024-04-16 10:30:00',@restaurantId1, '물 주세요', 'CALL'),
    (5, 0, '2024-04-17 09:30:00',@restaurantId1, '와주세요', 'CALL'),
    (2, 0, '2024-04-17 18:30:00',@restaurantId1, '티슈 주세요', 'CALL'),
    (5, 0, '2024-04-17 17:20:00',@restaurantId1, '와주세요', 'CALL');

-- 리뷰 추가
INSERT INTO review (name, content, star, reg_date, restaurant_id, order_id, secret)
VALUES
    ('김철수', '정말 맛있어요!', 5, '2024-04-01 12:30:00', 1, 1, 0),
    ('이영희', '분위기가 너무 좋았어요.', 4, '2024-04-01 12:40:00', 1, 2, 0),
    ('박민수', '서비스가 만족스러웠습니다.', 5, '2024-04-02 13:30:00', 1, 3, 0),
    ('최유리', '음식이 너무 늦게 나와요.', 2, '2024-04-02 13:50:00', 1, 4, 0),
    ('정태욱', '다음에 또 방문하고 싶네요.', 5, '2024-04-03 18:30:00', 1, 5, 0),
    ('박지영', '맛이 없어요...', 1, '2024-04-03 19:00:00', 1, 6, 0),
    ('김태현', '가성비가 좋았습니다.', 4, '2024-04-04 19:30:00', 1, 7, 0),
    ('조민서', '재방문 의사 있습니다!', 5, '2024-04-04 20:00:00', 1, 8, 0),
    ('윤소희', '친절하셨어요.', 5, '2024-04-05 20:30:00', 1, 9, 0),
    ('이준호', '음식이 식어서 나왔어요.', 3, '2024-04-05 21:00:00', 1, 10, 0);

