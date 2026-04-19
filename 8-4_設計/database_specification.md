# テーブル名　: users
##　概要
ユーザーの基本情報を管理するテーブル

## カラム定義
|カラム名| データ型| 制約| 説明| 
|:-----|:-------|:---|:----|
| id | SERIAL | PRIMARY KEY | ID（自動採番） |
| user_name | VARCHAR(10) | NOT NULL | 名前 |
| password | VARCHAR(20) | NOT NULL | パスワード |
| role | VARCHAR(10) | NOT NULL | 役割 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 登録日時 |


# テーブル名　: books
##　概要
本の基本情報を管理するテーブル

## カラム定義
|カラム名| データ型| 制約| 説明| 
|:-----|:-------|:---|:----|
| id | SERIAL | PRIMARY KEY | ID（自動採番） |
| title | VARCHAR(20) | NOT NULL | タイトル |
| orice | VARCHAR(10) | NOT NULL | 価格 |
| category | VARCHAR(10) | NOT NULL | カテゴリ |


# テーブル名　: stocks
##　概要
商品在庫の基本情報を管理するテーブル

## カラム定義
|カラム名| データ型| 制約| 説明| 
|:-----|:-------|:---|:----|
| id | SERIAL | PRIMARY KEY | ID（自動採番） |
| book_id | VARCHAR(20) | NOT NULL | 本のid |
| stock | INT(100) | NOT NULL | 在庫数 |


# テーブル名　: sales
##　概要
売上の基本情報を管理するテーブル

## カラム定義
|カラム名| データ型| 制約| 説明| 
|:-----|:-------|:---|:----|
| id | SERIAL | PRIMARY KEY |ID（自動採番） |
| user_id | VARCHAR(20) | NOT NULL | ユーザーのid |
| book_id | VARCHAR(20) | NOT NULL | 本のid |
| quantity | INT(10) | NOT NULL | 購買数 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 登録日時 |

