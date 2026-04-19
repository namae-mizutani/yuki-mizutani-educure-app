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
    }

    books {
        int id PK
        string title
        int price
        string category
    }

    stocks{
        int id PK
        int book_id FK
        int stock
        timestamp created_at
    }

    sales {
        int id  
        int book_id FK
        int quantity
        timestamp created_at
    }
```