```mermaid
erDiagram
   users ||--o{ sales :""}
   books ||--o{ sales:""}
   books ||--|| stocks:""

    users {
        string user_name
        string password
        int id PK
        string role
        datetime created_at
        datetime updated_at

    }
    books {
        string title
        int price
        int id Pk
        string category
        datetime created_at
        datetime updated_at
    }

    stocks{
        int stock
        int id Pk
        int book_id FK
        datetime created_at
        datetime updated_at
    }

    sales {
        int sale_id   
        int id  
        int user_id FK
        int book_id FK
        int quantity
        datetime created_at
        datetime updated_at
    }
```