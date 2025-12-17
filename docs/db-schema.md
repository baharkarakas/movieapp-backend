# Movie Collection System – Database Schema

# Movie Collection System – Database Schema

## Tables

### 1. users

- id (PK, INT, AUTO_INCREMENT)
- name (VARCHAR)
- email (VARCHAR, UNIQUE)
- password_hash (VARCHAR)
- role (ENUM: 'user', 'admin', default 'user')
- created_at (DATETIME)

### 2. movies

- id (PK, INT, AUTO_INCREMENT)
- tmdb_id (INT or VARCHAR)
- title (VARCHAR)
- year (INT)
- poster_url (VARCHAR)
- rating (DECIMAL(3,1))
- created_at (DATETIME)

### 3. categories

- id (PK, INT, AUTO_INCREMENT)
- name (VARCHAR, UNIQUE)

### 4. movie_categories

- id (PK, INT, AUTO_INCREMENT)
- movie_id (FK → movies.id)
- category_id (FK → categories.id)
- UNIQUE(movie_id, category_id)

### 5. user_movies

- id (PK, INT, AUTO_INCREMENT)
- user_id (FK → users.id)
- movie_id (FK → movies.id)
- status (ENUM: 'watchlist', 'watched')
- added_at (DATETIME)

### 6. reviews

- id (PK, INT, AUTO_INCREMENT)
- user_id (FK → users.id)
- movie_id (FK → movies.id)
- rating (TINYINT)
- comment (TEXT)
- created_at (DATETIME)

## Relationships

- One **user** can have many **user_movies** records.
- One **movie** can appear in many users’ **user_movies**.
- Users and movies have a many-to-many relationship through **user_movies**.

- One **movie** can have many **categories** and  
  one **category** can belong to many **movies**.  
  This many-to-many relationship is represented by **movie_categories**.

- One **user** can write many **reviews** and  
  one **movie** can have many **reviews**.  
  Each review belongs to exactly one user and one movie.
