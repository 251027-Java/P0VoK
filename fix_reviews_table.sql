-- Fix the reviews table to make reviewID auto-increment
-- Run this SQL script in your PostgreSQL database

-- Step 1: Create a sequence for reviewID
CREATE SEQUENCE IF NOT EXISTS reviews_reviewid_seq;

-- Step 2: Set the sequence to start from the current max reviewID + 1
SELECT setval('reviews_reviewid_seq', COALESCE((SELECT MAX("reviewID") FROM reviews), 0) + 1, false);

-- Step 3: Alter the reviewID column to use the sequence as default
ALTER TABLE reviews ALTER COLUMN "reviewID" SET DEFAULT nextval('reviews_reviewid_seq');

-- Step 4: Make sure the sequence is owned by the column (optional but recommended)
ALTER SEQUENCE reviews_reviewid_seq OWNED BY reviews."reviewID";

