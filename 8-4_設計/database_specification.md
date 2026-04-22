# テーブル名　: users
##　概要
ユーザーの基本情報を管理するテーブル

## カラム定義
|カラム名| データ型| 制約| 説明| 
|:-----|:-------|:---|:----|
| id | SERIAL | PRIMARY KEY | ID（自動採番） |
| user_name | VARCHAR(10) | NOT NULL | 名前 |
| password | VARCHAR(100) | NOT NULL | パスワード |
| role | VARCHAR(10) | NOT NULL | 役割 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 登録日時 |
| deleted_at | TIMESTAMP | NOT NULL | 退会日時|
| updated_at | TIMESTAMP | NOT NULL | 更新日時|


# テーブル名　: books
##　概要
本の基本情報を管理するテーブル

## カラム定義
|カラム名| データ型| 制約| 説明| 
|:-----|:-------|:---|:----|
| id | SERIAL | PRIMARY KEY | ID（自動採番） |
| title | VARCHAR(20) | NOT NULL | タイトル |
| price | VARCHAR(10) | NOT NULL | 価格 |
| category | VARCHAR(10) | NOT NULL | カテゴリ |

# テーブル名　: stocks
##　概要
商品在庫の基本情報を管理するテーブル

## カラム定義
|カラム名| データ型| 制約| 説明| 
|:-----|:-------|:---|:----|
| id | SERIAL | PRIMARY KEY | ID（自動採番） |
| book_id |INT | NOT NULL | 本のid |
| stock | INT | NOT NULL | 在庫数 |


# テーブル名　: sales
##　概要
売上の基本情報を管理するテーブル

## カラム定義
|カラム名| データ型| 制約| 説明| 
|:-----|:-------|:---|:----|
| id | SERIAL | PRIMARY KEY |ID（自動採番） |
| user_id | INT | NOT NULL | ユーザーのid |
| book_id | INT | NOT NULL | 本のid |
| quantity | INT | NOT NULL | 購買数 |
| status | VARCHAR(10) | NOT NULL | 注文状態 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 登録日時 |