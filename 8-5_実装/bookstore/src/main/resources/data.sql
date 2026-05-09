-- 書籍データの初期投入
INSERT INTO books (title, price, category) VALUES ('Java入門', 500, 'プログラミング') ON CONFLICT DO NOTHING;
INSERT INTO books (title, price, category) VALUES ('Spring Boot実践', 1000, 'エンジニア') ON CONFLICT DO NOTHING;
INSERT INTO books (title, price, category) VALUES ('SQLの基本', 1500, 'データベース') ON CONFLICT DO NOTHING;

-- 在庫データの初期投入 (book_id 1, 2, 3 に対して)
INSERT INTO stocks (book_id, stock) VALUES (1, 10) ON CONFLICT DO NOTHING;
INSERT INTO stocks (book_id, stock) VALUES (2, 5) ON CONFLICT DO NOTHING;
INSERT INTO stocks (book_id, stock) VALUES (3, 0) ON CONFLICT DO NOTHING;