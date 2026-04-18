```mermaid
graph TD
usecaseDiagram
    actor "会員" as user
    actor "在庫管理者" as staff
    actor "システム管理者" as master

    package "本屋システム" {
        usecase "ログイン" as UC1
        usecase "商品検索" as UC2
        usecase "購入" as UC3
        usecase "在庫確認" as UC4
        usecase "売上集計" as UC5
        usecase "購入履歴" as UC6
        usecase "ログアウト" as UC7
    }

    user --> UC1
    user --> UC2
    user --> UC3
    user --> UC6
    user --> UC7

    staff --> UC1
    staff --> UC4
    staff --> UC6
    staff --> UC7

    master --> UC1
    master --> UC5
    master --> UC7
```