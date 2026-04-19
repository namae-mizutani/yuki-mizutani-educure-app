```mermaid
erDiagram
   users ||--o{ sales :""}
   books ||--o{ sales:""}
   books ||--|| stocks:""

    users {
        int id PK
        string user_name
        string password
        string role
        timestamp created_at
        timestamp updated_at

    }
    books {
        int id Pk
        string title
        int price
        string category
        timestamp created_at
        timestamp updated_at
    }

    stocks{
        int id Pk
        int book_id FK
        int stock
        timestamp created_at
        timestamp updated_at
    }

    sales {
        int id  
        int user_id FK
        int book_id FK
        int quantity
        timestamp created_at
        timestamp updated_at
    }
```